FROM amazoncorretto:17.0.5

EXPOSE 8080
EXPOSE 9090

ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=
ENV JWT_SECRET=

COPY target/lists-backend*.jar lists-backend.jar

CMD ["/bin/sh", "-c", "java -jar /lists-backend.jar"]
