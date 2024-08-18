# ai-financial-analyst
Java, Springboot, Spring IA and gpt-4o


## Project: Personal Financial Analyst

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

1. **Java 11+**: The project uses features of Java 11, so ensure you have this version or higher installed.
2. **MongoDB**: Required to store financial data. Must be configured and running.
3. **PostgreSQL with PGVector**: Must have the PGVector plugin installed and configured in PostgreSQL.
4. **OpenAI API Key**: Required to access the GPT-4 model. Configure your API key in the application properties.

#### Execution Steps

1. Clone the repository.
2. Configure the properties in the `application.properties` file to point to your MongoDB and PostgreSQL instances.
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the project:
   ```bash
   mvn spring-boot:run
   ```

#### Testing

The project includes unit tests for the main components, using JUnit and Mockito. To run the tests:

```bash
mvn test
```

### Future Enhancements

1. **Improvement of LLM Integration**: Explore enhancements in preparing the data for the LLM, including adding more contextual data to improve the generated responses.
2. **Storage Optimization**: Implement caching strategies and optimize the use of MongoDB and PGVector.
3. **User Interface**: Develop a user interface to facilitate interaction with the financial analyst.
4. **Serverless Architecute**: Implementing SpringCloudFunctions to deploys applications using Functions in any cloud provider.