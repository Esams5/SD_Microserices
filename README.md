# Cardapio Digital Distribuido

Projeto pratico para a disciplina de Sistemas Distribuidos com foco em:

- Microsservicos e orquestracao de containers
- Banco de dados distribuido
- Tolerancia a falhas com degradacao graciosa

## Estrutura

```text
.
|-- auth-service
|-- avaliacoes-service
|-- cardapio-service
|-- compras-service
|-- restaurante-frontend
`-- docker-compose.yml
```

## Subir tudo com Docker

```bash
docker compose up -d --build
```

Servicos expostos:

- Frontend: `http://localhost:4200`
- Cardapio: `http://localhost:8081`
- Avaliacoes: `http://localhost:8082`
- Auth: `http://localhost:8083`
- Compras: `http://localhost:8084`

Para parar tudo:

```bash
docker compose down
```

Para parar e remover tambem os volumes dos bancos:

```bash
docker compose down -v
```

## Rodar o frontend fora do Docker

Se quiser desenvolver o Angular localmente, o projeto ja vem com proxy para os microsservicos:

```bash
cd restaurante-frontend
npm install
npm start
```

O frontend ficara disponivel em `http://localhost:4200` e as chamadas `/api/*` serao encaminhadas para os servicos backend corretos.

## Como o frontend foi dockerizado

- O `restaurante-frontend` usa um `Dockerfile` multi-stage.
- A etapa de build gera o Angular com `npm run build`.
- A imagem final usa `nginx:alpine` para servir os arquivos estaticos.
- O `nginx` faz proxy de:
  - `/api/pratos` -> `cardapio-service:8081`
  - `/api/avaliacoes` -> `avaliacoes-service:8082`
  - `/api/auth` -> `auth-service:8083`
  - `/api/compras` -> `compras-service:8084`

Assim o navegador fala sempre com `http://localhost:4200`, e o container do frontend resolve os servicos internos via Docker Compose.

## Endpoints principais

- `GET /api/pratos`
- `POST /api/pratos`
- `PUT /api/pratos/{id}`
- `DELETE /api/pratos/{id}`
- `GET /api/avaliacoes/{pratoId}`
- `POST /api/avaliacoes`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/validate`
- `POST /api/compras`

Se quiser chamar direto sem passar pelo frontend:

- `GET http://localhost:8081/api/pratos`
- `GET http://localhost:8082/api/avaliacoes/{pratoId}`
- `POST http://localhost:8083/api/auth/login`
- `POST http://localhost:8084/api/compras`

## Bancos de dados

Cada microsservico que persiste dados usa seu proprio PostgreSQL:

- `cardapio-service` -> `postgres-cardapio`
- `avaliacoes-service` -> `postgres-avaliacoes`
- `auth-service` -> `postgres-auth`
- `compras-service` -> `postgres-compras`

Isso evita acoplamento entre os dados dos servicos. Se `auth-service` cair, o cardapio continua publico. Se `avaliacoes-service` cair, a listagem do cardapio continua funcionando.

## Roteiro da demonstracao

1. Suba tudo com `docker compose up -d --build`.
2. Abra `http://localhost:4200`.
3. Faça login com o usuario base:

```text
email: admin@restaurante.com
senha: 1234
```

4. Cadastre um prato novo pela interface.
5. Envie uma avaliacao para o prato logado como usuario autenticado.
6. Registre uma compra simples em um prato logado como usuario autenticado.
7. Edite e exclua pratos para demonstrar o CRUD completo.
8. Pare o microsservico de compras:

```bash
docker stop compras-service
```

9. Mostre que somente a compra falha, mas cardapio, login e avaliacoes continuam.
10. Pare o microsservico de avaliacoes:

```bash
docker stop avaliacoes-service
```

11. Atualize a tela do frontend.
12. Mostre que a lista de pratos continua visivel.
13. Mostre que o CRUD principal continua funcionando normalmente.
14. Mostre que o espaco de notas exibe `Avaliacoes indisponiveis`.
15. Derrube tambem o servico de autenticacao:

```bash
docker stop auth-service
```

16. Mostre que o cardapio continua publico e acessivel mesmo sem login.
17. Mostre que apenas login/cadastro, compras e acoes autenticadas ficam indisponiveis.

## Observacoes tecnicas

- O `cardapio-service` usa PostgreSQL via Docker.
- O `avaliacoes-service` usa PostgreSQL proprio e exige token valido no `POST` de avaliacao.
- O `auth-service` usa PostgreSQL proprio, sobe com um usuario base e emite token no login.
- O `compras-service` usa PostgreSQL proprio e exige token valido no `POST` de compra.
- O frontend usa caminhos relativos `/api/*`, proxy no `ng serve` e proxy no `nginx` para funcionar igual em desenvolvimento e em Docker.
