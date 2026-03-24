FROM eclipse-temurin:21-jdk
ENV PORT=8443

COPY keystores /keystores

COPY target/*.jar app.jar

CMD ["java","-jar","app.jar"]
