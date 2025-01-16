#!/bin/sh
#
# Container entrypoint script
# Runs the spring-boot app
# Uses the log4j2 configuration file for Docker
# APP_HOME: Root of the app
# JAVA_OPTS: Additional Java config

exec java ${JAVA_OPTS} -jar "${APP_HOME}/mapper-backend.jar" "$@"
