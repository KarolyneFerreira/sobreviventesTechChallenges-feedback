
# Arquitetura do Projeto

## Objetivo

O sistema tem como finalidade:
- Conectar-se a um banco de dados Amazon RDS (Relational Database Service) hospedado na AWS.
- Consultar registros de Feedback inseridos na última semana.
- Processar e agrupar os dados por faixa de notas.
- Calcular a média semanal.
- Gerar um relatório CSV e armazená-lo em um bucket Amazon S3.
  
## Componentes Principais
- Quarkus Framework: fornece suporte nativo para aplicações Java com baixo tempo de inicialização e integração com JPA/Hibernate.
- Banco de Dados RDS (AWS): armazena os registros de feedback.
- Panache Repository: abstração para consultas JPA simplificadas.
- AWS SDK v2: integração com o serviço S3 para upload dos relatórios.
- OpenCSV: biblioteca utilizada para geração de arquivos CSV.

## Estrutura de Arquitetura

src/<br>
├── principal/<br>
│   └── docker/<br>
│       ├── Dockerfile.jvm<br>
│       ├── Dockerfile.legacy-jar<br>
│       ├── Dockerfile.native<br>
│       └── Dockerfile.native-micro<br>
├── java/org/fiap/com/<br>
│       ├── Models/<br>
│       │   └── Feedback.java   Define a entidade principal do sistema, representando dados de feedback.<br>
│       ├── Repositories/<br>
│       │   └── FeedbackRepository.java    Interface de acesso a dados<br>
│       └── Services/<br>
│           └── FeedbackService.java    Camada de lógica de negócios, responsável por processar e gerenciar operações relacionadas ao feedback.<br>

  
## Fluxo de Execução
- Usuário insere feedbacks no sistema (persistidos no RDS).
- Serviço FeedbackService consulta os dados da última semana via FeedbackRepository.
- Os feedbacks são agrupados por faixa de notas e a média semanal é calculada.
- Um arquivo CSV é gerado em memória.
- O arquivo é enviado para o bucket S3 configurado



## Executando a aplicação em modo de desenvolvimento

Você pode rodar sua aplicação em modo de desenvolvimento, que habilita live coding, usando:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  O Quarkus possui uma Dev UI disponível apenas em modo de desenvolvimento em <http://localhost:8080/q/dev/>.

## Empacotando e executando a aplicação

A aplicação pode ser empacotada usando:

```shell script
./mvnw package
```

Isso gera o arquivo `quarkus-run.jar`  no diretório `target/quarkus-app/`.
Note que não é um _über-jar_ pois as dependências são copiadas para `target/quarkus-app/lib/`.

A aplicação pode ser executada com: `java -jar target/quarkus-app/quarkus-run.jar`.

Para criar um _über-jar_, execute:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

E rode com: `java -jar target/*-runner.jar`.

##  Criando um executável nativo

Você pode criar um executável nativo usando:

```shell script
./mvnw package -Dnative
```

Ou, se não tiver o GraalVM instalado, pode rodar a build nativa em um container:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Depois, execute o binário gerado: `./target/lambda-quarkus-1.0.0-SNAPSHOT-runner`
