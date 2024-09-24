FROM openjdk:21-slim-bookworm
WORKDIR /root/spring
COPY ./build/libs/*.jar /root/spring
ENV FILE_NAME "*.jar"
EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "/root/spring/${FILE_NAME}.jar"]
CMD echo DB_IP
CMD echo DB_PORT
ENTRYPOINT java -jar -Dspring.datasource.url=jdbc:mysql://${DB_IP}:${DB_PORT} -Dspring.datasource.password=${DB_PW} /root/spring/${FILE_NAME}
# ENTRYPOINT java -jar -Ddb.url=jdbc:mysql://${DB_IP}:${DB_PORT} -Ddb.password=${DB_PW} /root/spring/${FILE_NAME}