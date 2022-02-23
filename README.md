[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.tardin%3Abookstoremanager&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.tardin%3Abookstoremanager) [![Build Status](https://app.travis-ci.com/8rux40/bookstoremanager_course.svg?branch=master)](https://app.travis-ci.com/github/8rux40/bookstoremanager_course)

<h2>Bookstore API Manager</h2>

O objetivo do projeto Bookstore API Manager é disponibilizar uma API para cadastro dos livros de uma livraria através de uma API REST.

## Módulos
- [x] Gerenciamento de Autores
- [x] Gerenciamento de Editoras
- [x] Gerenciamento de Usuários
- [ ] Gerenciamento de Livros
- [x] Autenticação e Autorização

## Executar
Para executar o projeto no terminal, digite o seguinte comando:

```shell script
mvn spring-boot:run 
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

```
http://localhost:8080/api/v1/books
```
## Documentação
Para abrir a documentação (disponibilizada através do Swagger 2) de todas as operações implementadas com o padrão arquitetural REST, acesse o seguinte link abaixo:

```
http://localhost:8080/swagger-ui.html
```

## Produção
Abaixo, segue o link do projeto implantado no Heroku:
 
```
https://bookstoremanager-course-8rux40.herokuapp.com/api/books
```

O link da documentação no Heroku, implementada também atraves do Swagger, está no link abaixo:
 
```
https://bookstoremanager-course-8rux40.herokuapp.com/swagger-ui.html
```

