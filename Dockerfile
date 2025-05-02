# Étape 1 : Builder le projet avec Maven
FROM maven:3.8.5-openjdk-8 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Copier le .war dans Tomcat
FROM tomcat:9.0
COPY --from=builder /app/target/CINEPROJ.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]