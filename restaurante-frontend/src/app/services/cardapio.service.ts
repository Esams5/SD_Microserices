import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prato } from '../models/prato.model';

@Injectable({
  providedIn: 'root'
})
export class CardapioService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/pratos';

  listar(): Observable<Prato[]> {
    return this.http.get<Prato[]>(this.apiUrl);
  }

  criar(prato: Prato): Observable<Prato> {
    return this.http.post<Prato>(this.apiUrl, prato);
  }

  atualizar(id: number, prato: Prato): Observable<Prato> {
    return this.http.put<Prato>(`${this.apiUrl}/${id}`, prato);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

