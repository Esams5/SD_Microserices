import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { CompraHistorico, CompraRequest } from '../models/compra.model';

@Injectable({
  providedIn: 'root'
})
export class ComprasService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/compras';

  criar(compra: CompraRequest, token: string): Observable<void> {
    return this.http.post<void>(this.apiUrl, compra, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    }).pipe(
      catchError((error) => throwError(() => error))
    );
  }

  listarMinhas(token: string): Observable<CompraHistorico[]> {
    return this.http.get<CompraHistorico[]>(`${this.apiUrl}/minhas`, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    });
  }
}
