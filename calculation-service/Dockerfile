FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} calculation-service.jar
ENTRYPOINT ["java","-jar","/calculation-service.jar"]
EXPOSE 8082