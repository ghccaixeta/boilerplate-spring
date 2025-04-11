# Boilerplate Spring + Oauth2

## Descrição

Este é um servidor de autorização OAuth2 implementado com Spring Security.

## Pré-requisitos

- Java 17+
- Maven
- OpenSSL

## Configuração das Chaves

Antes de executar o projeto, é necessário gerar as chaves pública e privada. Siga os passos abaixo:

### 1. Gerando a Chave Privada

```bash
openssl genrsa -out app.key 2048
```

### 2. Gerando a Chave Pública

```bash
openssl rsa -in app.key -pubout -out app.pub
```

### 3. Localização das Chaves

Coloque os arquivos `app.key` e `app.pub` no diretório: src/main/resources

## Executando o Projeto

1. Certifique-se de que as chaves foram geradas e estão no local correto
2. Execute o projeto usando Maven:

```bash
mvn spring-boot:run
```

## Endpoints Disponíveis

- Token Endpoint: `/oauth2/token`
  - Método: POST
  - Grant Type: client_credentials

## Segurança

⚠️ **IMPORTANTE**:

- Nunca compartilhe ou commite as chaves privadas no controle de versão
- Adicione `*.key` e `*.pub` ao seu `.gitignore`
- Em ambiente de produção, considere usar variáveis de ambiente ou cofre de senhas para armazenar as chaves
