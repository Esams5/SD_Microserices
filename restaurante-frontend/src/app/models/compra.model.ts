export interface CompraRequest {
  pratoId: number;
  pratoNome: string;
  quantidade: number;
  precoUnitario: number;
}

export interface CompraHistorico {
  id: number;
  pratoId: number;
  pratoNome: string;
  quantidade: number;
  precoUnitario: number;
  valorTotal: number;
  usuarioId: number;
  usuarioNome: string;
  dataCompra: string;
}
