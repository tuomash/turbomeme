FROM tomcat:10.1.23-jre17-temurin-jammy

COPY target/turbomeme-1.0.war /usr/local/tomcat/webapps/turbomeme.war
