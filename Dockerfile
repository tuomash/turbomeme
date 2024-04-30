FROM tomcat:9.0.88-jre17-temurin-jammy

RUN echo 'export JAVA_OPTS="-Dturbomeme.app.name=turbomeme -Dturbomeme.db.host=host.docker.internal -Dturbomeme.db.port=5432 -Dturbomeme.db.name=turbomeme -Dturbomeme.db.username=turbomeme -Dturbomeme.db.name=turbomeme -Dturbomeme.db.password=turbomeme"' >> /usr/local/tomcat/bin/setenv.sh \
    && chmod u+x /usr/local/tomcat/bin/setenv.sh

COPY target/turbomeme-1.0.war /usr/local/tomcat/webapps/turbomeme.war
