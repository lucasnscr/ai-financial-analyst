FROM amazoncorretto:21-alpine

WORKDIR /app

# Copiar o arquivo de propriedades para o contêiner
COPY src/main/resources/application.properties /app/config/application.properties

# Copiar o jar do projeto para o contêiner
COPY target/ai-financial-analyst-0.0.1-SNAPSHOT.jar /app/ai-financial-analyst-0.0.1-SNAPSHOT.jar

# Comando para rodar a aplicação com o arquivo de propriedades especificado
CMD ["java", "-jar", "ai-financial-analyst-0.0.1-SNAPSHOT.jar", "--spring.config.location=/app/config/application.properties"]