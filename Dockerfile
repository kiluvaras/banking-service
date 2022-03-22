FROM openjdk:17
RUN ./gradlew build
ADD build/libs/tuum-0.0.1-SNAPSHOT.jar docker.jar
VOLUME /tmp
ENTRYPOINT ["java", "-jar", "docker.jar"]
EXPOSE 8080