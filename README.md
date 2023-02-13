# Entrevista

Prueba técnica realizada 13/02/2023.

## Apis Disponibles

Api principal publicada en openapi [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Esta requiere de autenticación para la llamada de los WS (entrevista/entrevista), ejemplo:
```bash
curl -X 'GET' \
  'http://localhost:8080/v1/rates/get/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Basic ZW50cmV2aXN0YTplbnRyZXZpc3Rh'
```

Api mockeada tanto en ejecución como en Test de las Currency en [http://localhost:8081](http://localhost:8081)
Con los siguientes servicios:
```bash
http://localhost:8081/v1/currencies
http://localhost:8081/v1/currencies/{code}
```
## Requisitos
- Tener libres los puertos 8080 y 8081
- Java 17
- Maven 
- Compilar proyecto, para la generación de las clases de openapi
```bash
mvn clean install

```

- BD postgresql configurada en el application.properties 
```properties
spring.datasource.url= jdbc:postgresql://localhost:32768/exercicedb
spring.datasource.username= postgres
spring.datasource.password= postgrespw

```

## Ejecución

```bash
java -jar target/exercise-0.0.1-SNAPSHOT.jar
```
