export interface LoginRequest {
  email: string;
  senha: string;
}

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
}

export interface UsuarioAutenticado {
  id: number;
  nome: string;
  email: string;
  mensagem: string;
}
