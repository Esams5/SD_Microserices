import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { UsuarioAutenticado } from './models/auth.model';
import { Prato, PratoView } from './models/prato.model';
import { AuthService } from './services/auth.service';
import { AvaliacoesService } from './services/avaliacoes.service';
import { CardapioService } from './services/cardapio.service';

type PainelAberto = 'login' | 'cadastro' | 'prato' | null;

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly fb = inject(FormBuilder);
  private readonly cardapioService = inject(CardapioService);
  private readonly avaliacoesService = inject(AvaliacoesService);
  private readonly authService = inject(AuthService);

  pratos: PratoView[] = [];
  carregando = false;
  salvando = false;
  autenticando = false;
  editandoId: number | null = null;
  painelAberto: PainelAberto = null;
  mensagem = '';
  erro = '';
  authMensagem = '';
  authErro = '';
  authDisponivel = true;
  usuario: UsuarioAutenticado | null = this.authService.usuarioAtual();

  readonly pratoForm = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.minLength(2)]],
    descricao: ['', [Validators.required, Validators.minLength(5)]],
    preco: [0, [Validators.required, Validators.min(0.01)]]
  });

  readonly loginForm = this.fb.nonNullable.group({
    email: ['admin@restaurante.com', [Validators.required, Validators.email]],
    senha: ['1234', [Validators.required, Validators.minLength(4)]]
  });

  readonly cadastroForm = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(4)]]
  });

  constructor() {
    this.verificarAuth();
    this.carregarPratos();
  }

  get gerenciamentoDisponivel(): boolean {
    return this.authDisponivel && !!this.usuario;
  }

  verificarAuth(): void {
    this.authService.verificarDisponibilidade().subscribe((disponivel) => {
      this.authDisponivel = disponivel;

      if (!disponivel) {
        this.painelAberto = null;
        this.authErro = 'Login e cadastro indisponiveis. O cardapio continua publico.';
      } else if (this.authErro.includes('indisponiveis')) {
        this.authErro = '';
      }
    });
  }

  carregarPratos(): void {
    this.carregando = true;
    this.erro = '';

    this.cardapioService
      .listar()
      .pipe(finalize(() => (this.carregando = false)))
      .subscribe({
        next: (pratos) => {
          this.pratos = pratos.map((prato) => ({
            ...prato,
            avaliacaoTexto: 'Carregando avaliacoes...',
            avaliacaoDisponivel: true,
            avaliacoes: []
          }));
          this.carregarAvaliacoes();
        },
        error: () => {
          this.erro = 'Nao foi possivel carregar o cardapio.';
        }
      });
  }

  carregarAvaliacoes(): void {
    this.pratos.forEach((prato) => {
      if (!prato.id) {
        return;
      }

      this.avaliacoesService.buscarResumo(prato.id).subscribe((resumo) => {
        prato.avaliacaoTexto = resumo.texto;
        prato.avaliacaoDisponivel = resumo.disponivel;
      });
    });
  }

  abrirPainel(painel: Exclude<PainelAberto, null>): void {
    if ((painel === 'login' || painel === 'cadastro') && !this.authDisponivel) {
      this.authErro = 'Servico de login/cadastro indisponivel.';
      return;
    }

    if (painel === 'prato' && !this.gerenciamentoDisponivel) {
      this.authErro = this.authDisponivel
        ? 'Faca login para gerenciar pratos.'
        : 'Servico de login/cadastro indisponivel.';
      return;
    }

    this.mensagem = '';
    this.erro = '';
    this.authMensagem = '';
    this.authErro = '';
    this.painelAberto = painel;
  }

  fecharPainel(): void {
    this.painelAberto = null;
    this.cancelarEdicao();
  }

  salvar(): void {
    if (!this.gerenciamentoDisponivel) {
      this.authErro = 'Faca login para gerenciar pratos.';
      return;
    }

    if (this.pratoForm.invalid) {
      this.pratoForm.markAllAsTouched();
      return;
    }

    this.salvando = true;
    this.mensagem = '';
    this.erro = '';

    const prato: Prato = this.pratoForm.getRawValue();
    const requisicao = this.editandoId
      ? this.cardapioService.atualizar(this.editandoId, prato)
      : this.cardapioService.criar(prato);

    requisicao.pipe(finalize(() => (this.salvando = false))).subscribe({
      next: () => {
        this.mensagem = this.editandoId
          ? 'Prato atualizado com sucesso.'
          : 'Prato cadastrado com sucesso.';
        this.fecharPainel();
        this.carregarPratos();
      },
      error: () => {
        this.erro = 'Nao foi possivel salvar o prato.';
      }
    });
  }

  editar(prato: PratoView): void {
    if (!this.gerenciamentoDisponivel) {
      return;
    }

    this.editandoId = prato.id ?? null;
    this.pratoForm.setValue({
      nome: prato.nome,
      descricao: prato.descricao,
      preco: prato.preco
    });
    this.abrirPainel('prato');
  }

  excluir(prato: PratoView): void {
    if (!this.gerenciamentoDisponivel || !prato.id || !confirm(`Excluir o prato "${prato.nome}"?`)) {
      return;
    }

    this.mensagem = '';
    this.erro = '';

    this.cardapioService.excluir(prato.id).subscribe({
      next: () => {
        this.mensagem = 'Prato removido com sucesso.';
        this.carregarPratos();
      },
      error: () => {
        this.erro = 'Nao foi possivel excluir o prato.';
      }
    });
  }

  login(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.autenticando = true;
    this.authMensagem = '';
    this.authErro = '';

    this.authService
      .login(this.loginForm.getRawValue())
      .pipe(finalize(() => (this.autenticando = false)))
      .subscribe({
        next: (usuario) => {
          this.usuario = usuario;
          this.authMensagem = `Bem-vindo, ${usuario.nome}.`;
          this.painelAberto = null;
        },
        error: () => {
          this.authErro = this.authDisponivel
            ? 'Nao foi possivel realizar o login.'
            : 'Servico de login indisponivel.';
        }
      });
  }

  cadastrar(): void {
    if (this.cadastroForm.invalid) {
      this.cadastroForm.markAllAsTouched();
      return;
    }

    this.autenticando = true;
    this.authMensagem = '';
    this.authErro = '';

    this.authService
      .cadastrar(this.cadastroForm.getRawValue())
      .pipe(finalize(() => (this.autenticando = false)))
      .subscribe({
        next: (usuario) => {
          this.authMensagem = `${usuario.nome} cadastrado com sucesso. Agora faca login.`;
          this.cadastroForm.reset({
            nome: '',
            email: '',
            senha: ''
          });
          this.painelAberto = 'login';
        },
        error: () => {
          this.authErro = this.authDisponivel
            ? 'Nao foi possivel cadastrar o usuario.'
            : 'Servico de cadastro indisponivel.';
        }
      });
  }

  logout(): void {
    this.authService.logout();
    this.usuario = null;
    this.painelAberto = null;
    this.cancelarEdicao();
    this.authMensagem = 'Sessao encerrada.';
  }

  get podeAvaliar(): boolean {
    return !!this.usuario && !!this.authService.tokenAtual();
  }

  cancelarEdicao(): void {
    this.editandoId = null;
    this.pratoForm.reset({
      nome: '',
      descricao: '',
      preco: 0
    });
  }

  campoInvalido(nomeCampo: 'nome' | 'descricao' | 'preco'): boolean {
    const campo = this.pratoForm.controls[nomeCampo];
    return campo.invalid && (campo.dirty || campo.touched);
  }

  campoLoginInvalido(nomeCampo: 'email' | 'senha'): boolean {
    const campo = this.loginForm.controls[nomeCampo];
    return campo.invalid && (campo.dirty || campo.touched);
  }

  campoCadastroInvalido(nomeCampo: 'nome' | 'email' | 'senha'): boolean {
    const campo = this.cadastroForm.controls[nomeCampo];
    return campo.invalid && (campo.dirty || campo.touched);
  }
}
