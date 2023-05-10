FROM openjdk:8-jdk-alpine
VOLUME /tmp

COPY zk-lock-demo-0.0.1-SNAPSHOT.jar app.war

ENV JAVA_OPTS=""

EXPOSE 9000

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war
