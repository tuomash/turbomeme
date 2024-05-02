FROM tomcat:9.0.88-jre17-temurin-jammy

RUN --mount=type=secret,id=app_name \
    --mount=type=secret,id=db_host \
    --mount=type=secret,id=db_port \
    --mount=type=secret,id=db_name \
    --mount=type=secret,id=db_username \
    --mount=type=secret,id=db_password \
    echo "export JAVA_OPTS=\"-Dturbomeme.app.name=$(cat /run/secrets/app_name) -Dturbomeme.db.host=$(cat /run/secrets/db_host) -Dturbomeme.db.port=$(cat /run/secrets/db_port) -Dturbomeme.db.name=$(cat /run/secrets/db_name) -Dturbomeme.db.username=$(cat /run/secrets/db_username) -Dturbomeme.db.password=$(cat /run/secrets/db_password)\"" >> /usr/local/tomcat/bin/setenv.sh \
    && chmod u+x /usr/local/tomcat/bin/setenv.sh

COPY target/turbomeme-1.0.war /usr/local/tomcat/webapps/turbomeme.war
