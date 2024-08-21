# Project: AI Personal Financial Analyst

### Project Description

This project implements a personal financial analyst system that leverages a large language model (LLM), specifically based on GPT-4, to make informed decisions in the financial market. The system collects real-time financial data, such as stock and cryptocurrency information, stores this data in a MongoDB database, and subsequently converts the data into embeddings that are stored in a database with PGVector support. The LLM is then fed with these embeddings to provide accurate and personalized financial insights.

### Project Structure

The project is divided into several main components, each responsible for a specific functionality:

- **Data Collection**: Utilizes an external client (`AlphaClient`) to fetch financial data for stocks and cryptocurrencies.
- **Data Storage**: The collected data is stored in MongoDB for persistence.
- **Embedding Conversion**: The data is converted into embeddings using the LLM and stored in a PGVector-supported database.
- **LLM Integration**: The LLM (based on GPT-4) uses these embeddings to generate financial insights and responses.

### Key Components

- **`DataLoadingService`**: A service responsible for loading stock and cryptocurrency data, processing it, and storing it both in MongoDB and PGVector. This service uses the `StockEnum` and `CryptoEnum` enums to iterate over the entities that need to be processed.

- **`AlphaClient`**: A client responsible for communicating with an external API to fetch financial information for stocks and cryptocurrencies.

- **`Stock` and `Crypto`**: Model classes representing the stock and cryptocurrency entities, respectively. They store the necessary data that will be used by the LLM.

- **`LLMContent`**: A component that prepares the content to be used by the LLM to generate embeddings.

### Dependencies

The project uses several libraries and frameworks to ensure its functionality. Below are the main dependencies and their versions as specified in the `pom.xml`:

- **Spring Boot**: The main framework used to create the service. It provides easy integration with MongoDB, JPA, and other RESTful services.
    - Version: `3.3.2`

- **MongoDB**: Used to store the collected financial data.
    - Driver: `spring-boot-starter-data-mongodb`
    - Version: `3.3.2`

- **PGVector**: Used to store the embeddings generated from the financial data.
    - Dependency: `spring-ai-pgvector-store` (requires specific configuration in PostgreSQL to support vectors)
  - Version: `1.0.0-SNAPSHOT`

- **SLF4J + Logback**: Used for logging within the project.
    - Version: `2.23.1`

- **JUnit & Mockito**: Testing tools used to create unit and integration tests.
    - JUnit Version: `5.10.3`
    - Mockito Version: `5.11.0`

- **Spring AI**: Spring AI OpenAI provides a streamlined approach to developing AI with gpt model to powered applications using the Spring framework.
    - Dependency: `spring-ai-openai-spring-boot-starter`
    - Version: `1.0.0-SNAPSHOT`

### Configuration and Execution

#### Prerequisites

1. **Java 22**: The project uses features of Java 22, so ensure you have this version or higher installed.
2. **MongoDB**: Required to store financial data. Must be configured and running.
3. **PostgreSQL with PGVector**: Must have the PGVector plugin installed and configured in PostgreSQL.
4. **OpenAI API Key**: Required to access the GPT-4o model. Configure your API key in the application properties.
5. **Alpha API Key**: Required to access the Financial Stock Market API, providing developers with access to a diverse range of financial data.

#### Execution Steps

1. Clone the repository.
2. Configure the properties in the `application.properties` file to point to your MongoDB and PostgreSQL instances.
3. Run docker compose to start local PGVector and MongoDB:
   ```bash
   docker-compose up -d
   ```
4. After start docker-compose you can access Mongo Express in this url: http://localhost:8081  
5. Build the project using Maven:
   ```bash
   mvn clean install
   ```
6. Run the project:
   ```bash
   mvn spring-boot:run
   ```

**Config Keycloack**
https://medium.com/javarevisited/keycloak-integration-with-spring-security-6-37999f43ec85

**Execute application local**

```
mvn spring-boot:run
```
### Request Curls

**Generate token**

```
curl --location 'http://localhost:8080/realms/ai-financial-analyst/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=super-client' \
--data-urlencode 'username=user1' \
--data-urlencode 'password=123456'
```

**Load Data**

Load Data in Vector DB and MongoDB. Just need run when you want update database.
If you use Postman, you will need get access token on generate token response and inject this token like Bearer Token 

```
curl --location 'http://localhost:9090/load' \
--header 'Authorization: ••••••'
```

**Chat**

Request to talk with chatbot, you will need do the same step of security. Inject token on request.

```
curl --location 'http://localhost:9090/chat/question' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ3OGV1eTNJRUxJZTZ5c2ZvYnJKaGF6NF9lRndVWXNCSVpDcDVPVUs5MkFnIn0.eyJleHAiOjE3MjQyNTEzMDAsImlhdCI6MTcyNDI1MTAwMCwianRpIjoiMzAzNzBiM2MtY2RiZi00NTA2LWE0ZjQtM2RmNGZkOGM5YjQ5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9haS1maW5hbmNpYWwtYW5hbHlzdCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJlMDRlN2Y0OS05NjZhLTQzZmUtYjc5ZC0xNTFkYjBhMzhiYmQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzdXBlci1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiMThjODViYjYtZDc2OC00M2I0LTljYjUtOWYyNzFiN2FjYWYxIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiYXBwX3VzZXIiLCJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtYWktZmluYW5jaWFsLWFuYWx5c3QiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InN1cGVyLWNsaWVudCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIxOGM4NWJiNi1kNzY4LTQzYjQtOWNiNS05ZjI3MWI3YWNhZjEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlVzZXIgMiIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIyIiwiZ2l2ZW5fbmFtZSI6IlVzZXIiLCJmYW1pbHlfbmFtZSI6IjIiLCJlbWFpbCI6Imx1Y2FzLm5hc2NpbWVudG8uc2NyQGhvdG1haWwuY29tIn0.X0hVXCXYgWSUkPlQmZo5Jam1QyJ-AD96BO9g7z7bexHOOflB2i6HLKZOyglM-9kIkdMJeABKEo-a3tH14TM8Air4W8WBYiaAT1ar2JmaODtBN1Q5KGRfdWV-Z7-WcHAQ0R4SB62BviocmahS6bwl3eOa9U5EYQtIWnBLVEIxMQRzCTwPgBhPMeUD1GLcMvRy53OXEZqgL5TC8s6Pc5c80lho__KOsh9KtalbLHLvZA-HzN3ZPJt9SUpogUPl5_W8c4qd-5eJBllHfTLbD_PMHADoutI-pJ1P4ZH0Weo1yYDiPQW8pShbUoLvCUgDeXCslRxIji9TZgKcCxWisP22MQ' \
--data '{
    "question": " What Are All 10 Stocks Warren Buffett Is Selling? Traduza para portugues a resposta"
}'
```


### Future Enhancements

1. **Improvement of LLM Integration**: Explore enhancements in preparing the data for the LLM, including adding more contextual data to improve the generated responses.
2. **Storage Optimization**: Implement caching strategies and optimize the use of MongoDB and PGVector.
3. **User Interface**: Develop a user interface to facilitate interaction with the financial analyst.
4. **Serverless Architecute**: Implementing SpringCloudFunctions to deploys applications using Functions in any cloud provider.
