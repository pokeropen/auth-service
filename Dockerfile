FROM eclipse-temurin:17-jre-alpine as build
RUN addgroup -S app && adduser -S app -G app
USER app

ENV POSTGRES_HOST 10.41.42.136
ENV POSTGRES_USER open_poker
ENV POSTGRES_PWD open_poker
ENV POSTGRES_PORT 5432
ENV POSTGRES_DB postgres

#COPY run.sh .
#mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /*.* /
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=*:9000,server=y,suspend=n
# ENV _JAVA_OPTIONS="-Xms4g -Xmx4g"
EXPOSE 9000
EXPOSE 9090
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]