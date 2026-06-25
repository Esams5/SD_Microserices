import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { Avaliacao, AvaliacaoDetalhe, AvaliacaoResumo } from '../models/avaliacao.model';

@Injectable({
  providedIn: 'root'
})
export class AvaliacoesService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/avaliacoes';

  buscarResumo(pratoId: number): Observable<AvaliacaoResumo> {
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/${pratoId}`).pipe(
      map((avaliacoes) => {
        if (avaliacoes.length === 0) {
          return {
            disponivel: true,
            texto: 'Sem avaliacoes'
          };
        }

        const media =
          avaliacoes.reduce((total, avaliacao) => total + avaliacao.nota, 0) / avaliacoes.length;

        return {
          disponivel: true,
          texto: `${media.toFixed(1)} / 5`
        };
      }),
      catchError(() =>
        of({
          disponivel: false,
          texto: 'Avaliacoes indisponiveis'
        })
      )
    );
  }

  listarPorPrato(pratoId: number): Observable<AvaliacaoDetalhe[]> {
    return this.http.get<AvaliacaoDetalhe[]>(`${this.apiUrl}/${pratoId}`).pipe(
      catchError(() => of([]))
    );
  }

  criar(avaliacao: Avaliacao, token: string): Observable<AvaliacaoDetalhe> {
    return this.http.post<AvaliacaoDetalhe>(this.apiUrl, avaliacao, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    });
  }
}
