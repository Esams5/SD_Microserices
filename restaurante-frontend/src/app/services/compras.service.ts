import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompraHistorico, CompraRequest } from '../models/compra.model';

@Injectable({
  providedIn: 'root'
})
export class ComprasService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8084/api/compras';

  criar(compra: CompraRequest, token: string): Observable<void> {
    return this.http.post<void>(this.apiUrl, compra, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    });
  }

  listarMinhas(token: string): Observable<CompraHistorico[]> {
    return this.http.get<CompraHistorico[]>(`${this.apiUrl}/minhas`, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    });
  }
}
