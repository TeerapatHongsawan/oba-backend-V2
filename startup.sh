#!/bin/sh
set -e

# Set basic java options
echo "Starting"
echo "Options Override from Jenkins: ${JAVA_OPTS_PARAM}"
# Start the application
exec java ${JAVA_OPTS_PARAM} -jar /apps/app.jar