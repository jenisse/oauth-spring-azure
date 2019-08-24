# Start with a base image containing Java runtime
FROM openjdk:8-jre-alpine

# Add Maintainer Info
LABEL maintainer="evereau@globokas.com"

# Add a volume pointing to /tmp
#VOLUME /log

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Make port 9090 available to the world outside this container
EXPOSE 9090

EXPOSE 5701

RUN apk add tzdata
RUN cp /usr/share/zoneinfo/America/Lima /etc/localtime
RUN echo "America/Lima" >  /etc/timezone

RUN apk add curl
RUN curl ifconfig.co


# The application's jar file
ARG JAR_FILE=spring-auth0-demo-0.0.1-SNAPSHOT.jar

# The application's property file
ARG APP_PROPERTIES=/config/prod/application.properties

# Add the application's jar to the container
ADD ${JAR_FILE} /app/spring-auth0-demo-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${APP_PROPERTIES} /app/config/application.properties

# Run the jar file 
ENTRYPOINT ["java","-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=/shared/heapdump/heapdump.bin","-Dcom.sun.management.jmxremote.rmi.port=9090","-Dcom.sun.management.jmxremote=true","-Dcom.sun.management.jmxremote.port=9090","-Dcom.sun.management.jmxremote.ssl=false","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.local.only=false","-Dcom.sun.management.jmxremote.ssl=false","-Djava.rmi.server.hostname=tampatapi.westus2.azurecontainer.io","-Djasypt.encryptor.password=tere1234","-Djasypt.encryptor.algorithm=PBEWithMD5AndTripleDES","-jar","spring-auth0-demo-0.0.1-SNAPSHOT.jar"]




