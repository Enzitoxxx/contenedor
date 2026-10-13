FROM payara/server-full:6.2023.12-jdk17

# 1. Copiar comandos de configuración JDBC
COPY post-boot-commands.asadmin /opt/payara/config/post-boot-commands.asadmin

# 2. Copiar el archivo .war al directorio de despliegue
ENV DEPLOY_DIR=/opt/payara/appserver/glassfish/domains/production/autodeploy
COPY target/BibliotecaJSF2.0-Practico3-LAB5-1.0.war ${DEPLOY_DIR}/ROOT.war

EXPOSE 8080