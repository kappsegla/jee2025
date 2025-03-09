# By default, build on JDK 21 on UBI 9.
ARG jdk=23
# Red Hat UBI 9 (ubi9-minimal) should be used on JDK 11 and later.
ARG dist=ubi9-minimal
FROM eclipse-temurin:${jdk}-${dist}

# Starting on jdk 17 eclipse-temurin is based on ubi9-minimal version 9.3 
#   that doesn't includes shadow-utils package that provides groupadd & useradd commands
# Conditional RUN: IF no groupadd AND microdnf THEN: update, install shadow-utils, clean
RUN if ! [ -x "$(command -v groupadd)" ] && [ -x "$(command -v microdnf)" ]; \
    then microdnf update -y && microdnf install --best --nodocs -y shadow-utils && microdnf clean all; \
    fi

WORKDIR /opt/jboss

RUN groupadd -r jboss -g 1000 && useradd -u 1000 -r -g jboss -m -d /opt/jboss -s /sbin/nologin -c "JBoss user" jboss \
    && chmod 755 /opt/jboss

ARG WILDFLY_VERSION=35.0.1.Final
ARG WILDFLY_SHA1=35e61cfe2b14bab1f0644d4967090fe7de8590dd
ARG POSTGRESQL_DRIVER_VERSION=42.7.5
ARG POSTGRESQL_JDBC_DRIVER=postgresql-${POSTGRESQL_DRIVER_VERSION}.jar
# Set environment variables
ENV JBOSS_HOME=/opt/jboss/wildfly
# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND=true

USER root

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && curl -L -O https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/wildfly-preview-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-preview-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
    && tar xf wildfly-preview-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-preview-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-preview-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Download PostgreSQL JDBC Driver
RUN curl -L -o ${JBOSS_HOME}/standalone/deployments/postgresql.jar \
    https://jdbc.postgresql.org/download/${POSTGRESQL_JDBC_DRIVER} \
    && chmod 664 ${JBOSS_HOME}/standalone/deployments/postgresql.jar

# Create the .dodeploy marker file
RUN touch ${JBOSS_HOME}/standalone/deployments/postgresql.jar.dodeploy

# Copy your application WAR file
COPY --chown=jboss:0 target/*.war ${JBOSS_HOME}/standalone/deployments/

# Copy CLI scripts for datasource and driver configuration
COPY --chown=jboss:0 docker/configure-datasource.cli /tmp/
COPY --chown=jboss:0 docker/entrypoint.sh /opt/jboss/

# Make the entrypoint script executable
RUN chmod +x /opt/jboss/entrypoint.sh

# Set default environment variables - these can be overridden at container runtime
ENV DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=example \
    DB_USER=postgres \
    DB_PASSWORD=postgres \
    DS_NAME=postgresql.jar \
    DS_JNDI=java:/PostgresDS

USER jboss

# Expose the ports in which we're interested
EXPOSE 8080

# Use custom entrypoint script to configure the datasource at runtime
ENTRYPOINT ["/opt/jboss/entrypoint.sh"]
