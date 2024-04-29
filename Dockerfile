FROM tomcat:9.0.88-jre17-temurin-jammy

COPY target/turbomeme-1.0.war /usr/local/tomcat/webapps/turbomeme.war
