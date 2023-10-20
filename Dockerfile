FROM openjdk:20-ea-4-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080 
ENTRYPOINT ["java","-jar","/app.jar",">>","spring.log"]
