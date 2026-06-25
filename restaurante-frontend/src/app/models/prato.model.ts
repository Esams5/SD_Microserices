export interface Prato {
  id?: number;
  nome: string;
  descricao: string;
  preco: number;
}

export interface PratoView extends Prato {
  avaliacaoTexto: string;
  avaliacaoDisponivel: boolean;
  avaliacoes?: {
    autor: string;
    nota: number;
    comentario: string;
  }[];
}
