# Prend Tomcat officiel version 9
FROM tomcat:9.0

# Copie le fichier WAR compilé dans le dossier webapps de Tomcat
COPY target/CINEPROJ.war /usr/local/tomcat/webapps/ROOT.war

# Expose le port 8080
EXPOSE 8080

# Lance Tomcat
CMD ["catalina.sh", "run"]
