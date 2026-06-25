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
|-- restaurante-frontend
`-- docker-compose.yml
```

## Subir o backend

```bash
docker-compose up -d --build
```

## Rodar o frontend

```bash
cd restaurante-frontend
npm install
ng serve
```

O frontend ficara disponivel em `http://localhost:4200`.

## Endpoints principais

- `GET http://localhost:8081/api/pratos`
- `POST http://localhost:8081/api/pratos`
- `PUT http://localhost:8081/api/pratos/{id}`
- `DELETE http://localhost:8081/api/pratos/{id}`
- `GET http://localhost:8082/api/avaliacoes/{pratoId}`
- `POST http://localhost:8082/api/avaliacoes`
- `POST http://localhost:8083/api/auth/register`
- `POST http://localhost:8083/api/auth/login`

## Roteiro da demonstracao

1. Suba o backend com `docker-compose up -d --build`.
2. Rode o frontend com `ng serve`.
3. Faça login com o usuario base:

```text
email: admin@restaurante.com
senha: 1234
```

4. Cadastre um prato novo pela interface Angular.
5. Envie uma avaliacao para o prato logado como usuario autenticado.
6. Edite e exclua pratos para demonstrar o CRUD completo.
7. Pare o microsservico de avaliacoes:

```bash
docker stop avaliacoes-service
```

8. Atualize a tela do frontend.
9. Mostre que a lista de pratos continua visivel.
10. Mostre que o CRUD principal continua funcionando normalmente.
11. Mostre que o espaco de notas exibe `Avaliacoes indisponiveis`.
12. Derrube tambem o servico de autenticacao:

```bash
docker stop auth-service
```

13. Mostre que o cardapio continua publico e acessivel mesmo sem login.
14. Mostre que apenas o bloco de login/cadastro fica indisponivel.

## Observacoes tecnicas

- O `cardapio-service` usa PostgreSQL via Docker.
- O `avaliacoes-service` usa H2 em memoria para simplificar a infraestrutura.
- O `auth-service` usa H2 em memoria e ja sobe com um usuario base.
- O frontend usa `catchError` com RxJS para degradacao graciosa quando o servico de avaliacoes ou autenticacao falha.
