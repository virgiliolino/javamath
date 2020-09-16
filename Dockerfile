FROM gradle:6.6.1-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM openjdk:8-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spatial.jar
COPY --from=build /home/gradle/src/*.json /app/

ENTRYPOINT ["java", "-jar", "/app/spatial.jar", "/app/shapes.json"]
