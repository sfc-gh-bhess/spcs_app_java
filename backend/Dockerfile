FROM openjdk:19
EXPOSE 8081
WORKDIR /app
COPY src/demoapi/target/*.jar .
COPY src/entrypoint.sh .
RUN chmod +x ./entrypoint.sh
ENTRYPOINT [ "./entrypoint.sh" ]
