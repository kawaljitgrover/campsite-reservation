# Java version: 1.8

# Build maven project, which will generate executable jar in target folder
mvn clean install

# run the application, it currently points to H2 in-memory db
java -jar campsite-reservation-0.0.1-SNAPSHOT.jar

# please refer postman collection for sample apis/payload.