# 📘 APIs REST com Spring Boot

**Projeto desenvolvido no módulo 5 do curso "Spring Boot Expert".**

## 🔓 Funcionalidades Abordadas

- **Criação de APIs RESTful:** 
  - Desenvolvimento de endpoints CRUD (Create, Read, Update, Delete) utilizando as melhores práticas REST.
- **Validação de Dados:**
  - Implementação de Bean Validation com anotações como `@NotBlank`, `@Email` e `@Valid` para garantir a integridade dos dados recebidos.
- **DTOs (Data Transfer Objects):**
  - Uso de DTOs para desacoplar as entidades de banco de dados da camada de apresentação.
  - Conversão entre entidades e DTOs utilizando ferramentas como MapStruct.
- **Documentação de APIs:**
  - Configuração e utilização do Swagger (OpenAPI) para gerar documentação interativa das APIs.
- **Tratamento Global de Exceções:**
  - Centralização do tratamento de erros com `@ControllerAdvice` e `@ExceptionHandler`, fornecendo respostas padronizadas para erros comuns.
- **Configuração de CORS e Segurança Básica:**
  - Configuração de Cross-Origin Resource Sharing (CORS) para permitir acesso à API a partir de diferentes origens.
  - Implementação de segurança mínima em endpoints sensíveis.

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Hibernate Validator (Bean Validation)**
- **Swagger/OpenAPI**
- **MapStruct**

---

## 🔧 Estrutura do Conteúdo Abordado

1. **Criação de Controllers:**
   - Uso de anotações como `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.
   - Implementação de endpoints RESTful para operações CRUD.

2. **Uso de DTOs:**
   - Implementação de classes DTO para transferência de dados.
   - Conversão entre entidades e DTOs utilizando MapStruct.

3. **Validação de Dados:**
   - Uso de Bean Validation para garantir que os dados enviados atendam aos requisitos definidos.

4. **Documentação com Swagger:**
   - Configuração do Swagger para gerar uma interface interativa da API.

5. **Tratamento de Exceções:**
   - Centralização de mensagens de erro e retorno consistente para o cliente.

6. **Configuração de CORS e Segurança:**
   - Implementação de configurações para acesso seguro e controlado à API.
