FROM docker-registry.fitdev.ru:15650/fabit/eclipse-temurin-jammy-jre-office:17.0.8.1_1

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

RUN apt-get update && apt-get install -y fontconfig libfreetype6 libreoffice-writer

COPY entrypoint.sh .
COPY docx-template*.jar docx-template.jar
RUN chmod 755 entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8099