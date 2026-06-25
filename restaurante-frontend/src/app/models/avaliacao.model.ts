export interface Avaliacao {
  id?: number;
  pratoId: number;
  nota: number;
  autor: string;
  comentario: string;
}

export interface AvaliacaoResumo {
  disponivel: boolean;
  texto: string;
}

export interface AvaliacaoDetalhe extends Avaliacao {
  id: number;
}

