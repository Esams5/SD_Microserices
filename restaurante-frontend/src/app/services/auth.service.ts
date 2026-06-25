import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, UsuarioAutenticado } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8083/api/auth';
  private readonly storageKey = 'restaurante.usuario';

  login(payload: LoginRequest): Observable<UsuarioAutenticado> {
    return this.http.post<UsuarioAutenticado>(`${this.apiUrl}/login`, payload).pipe(
      tap((usuario) => localStorage.setItem(this.storageKey, JSON.stringify(usuario)))
    );
  }

  cadastrar(payload: RegisterRequest): Observable<UsuarioAutenticado> {
    return this.http.post<UsuarioAutenticado>(`${this.apiUrl}/register`, payload);
  }

  verificarDisponibilidade(): Observable<boolean> {
    return this.http.get<{ status: string }>(`${this.apiUrl}/health`).pipe(
      map((response) => response.status === 'UP'),
      catchError(() => of(false))
    );
  }

  usuarioAtual(): UsuarioAutenticado | null {
    const raw = localStorage.getItem(this.storageKey);
    return raw ? (JSON.parse(raw) as UsuarioAutenticado) : null;
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
  }
}
