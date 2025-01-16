FROM maven:3.9.7-eclipse-temurin-17 AS builder

# Working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

# Copy everything to current working directory
COPY ./src ./src

# Build and remove unnecessary jar files
RUN mvn package -Dmaven.test.skip=true && \
	rm target/*.jar.original && \
    rm target/*-javadoc.jar

# Python base image
FROM python:3.10-slim-bookworm

ENV JAVA_HOME=/opt/java/openjdk
COPY --from=eclipse-temurin:17-jre $JAVA_HOME $JAVA_HOME
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY ./lib ./lib

# Install dependencies
RUN pip install ./lib/kg_generation-0.1.0-py3-none-any.whl

# System env vars
ENV USER_NAME=mapper
ENV USER_UID=200
ENV APP_HOME=/opt/mapper-backend

# Add the user who executes the app. The user belongs to the group with gui 0
RUN adduser --shell /bin/sh --disabled-password --disabled-login --uid ${USER_UID} ${USER_NAME}

# Create the app directory
RUN mkdir -p ${APP_HOME}

# Copy the script of the entrypoint and resources
COPY ./docker/docker-entrypoint.sh ${APP_HOME}/docker-entrypoint.sh

# Sets the minimum rights
RUN chown -R ${USER_NAME}:0 ${APP_HOME} \
    && chmod -R g=u ${APP_HOME} \
    && chmod ug+x ${APP_HOME}/docker-entrypoint.sh

# Define the port of the container
EXPOSE 8080

# User by default
USER ${USER_NAME}

# Copy the fatjar
COPY --chown="${USER_NAME}:0" --from=builder /app/target/mapper-backend-*.jar ${APP_HOME}/mapper-backend.jar

# Script entrypoint for the container
WORKDIR ${APP_HOME}
ENTRYPOINT ["./docker-entrypoint.sh"]
