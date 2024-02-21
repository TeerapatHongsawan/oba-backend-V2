FROM harbordev.se.scb.co.th/library/maven:3.8.1-openjdk-17-slim as build
USER root
RUN mkdir /build
COPY . /build
COPY settings.xml /root/.m2/settings.xml

WORKDIR /build

RUN mvn clean compile package
RUN ls -la /build/target/
FROM harbordev.se.scb.co.th/library/eclipse-temurin:17-scb-1.5
USER root
COPY --from=build --chown=java:java /build/target/*.jar /apps/app.jar
COPY --from=build --chown=java:java /build/startup.sh /apps/startup.sh
RUN chmod +x /apps/startup.sh
USER java
ENTRYPOINT ["/apps/startup.sh"]