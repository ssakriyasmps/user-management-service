FROM java

RUN groupadd -r appuser && useradd --no-log-init -r -g appuser appuser

USER appuser

ENV SERVER_PORT 8080
EXPOSE 8080

WORKDIR /home/appuser

LABEL APP-VERSION=${artifactId}-${version}

COPY ${project.artifactId}.jar /home/appuser/${project.artifactId}.jar

CMD exec java -jar ${artifactId}.jar
