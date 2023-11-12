#!/bin/bash

if [ -z "$1" ]; then
  echo "ERROR: Parameter is not set"
  exit 1
fi

if [ -z "$JAVA_HOME" ]; then
  echo "ERROR: JAVA_HOME is not set"
  exit 1
fi

chmod u+x ./mvnw && \
REGISTRY_URL="$1" && \
APP_VERSION=v$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)   && \
APP_NAME=$(./mvnw help:evaluate -Dexpression=project.name -q -DforceStdout) && \
docker manifest inspect "$REGISTRY_URL/$APP_NAME:$APP_VERSION" || \
./mvnw clean install -Dmaven.test.skip && \
docker build --no-cache -t "$APP_NAME:$APP_VERSION" -f ./Dockerfile . && \
docker image tag "$APP_NAME:$APP_VERSION"  "$REGISTRY_URL/$APP_NAME:$APP_VERSION" && \
docker image push "$REGISTRY_URL/$APP_NAME:$APP_VERSION"
