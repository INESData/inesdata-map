# node base image
FROM node:20.11-alpine AS builder

# Java needed for openapi-generator
RUN apk --no-cache add openjdk17

# working directory
WORKDIR /app

# copy everything to current working directory
COPY . ./

# run npm install
RUN npm install \
  	&& npm run build

# nginx base image
FROM nginx:alpine

ENV NGINX_HOME=/usr/share/nginx
ENV DOCUMENT_ROOT=${NGINX_HOME}/html

# copy static contents of project to nginx html
COPY --from=builder /app/dist/mapper-frontend ${DOCUMENT_ROOT}

COPY ./docker/assets/scripts/docker-entrypoint.sh ${NGINX_HOME}

ENV ENVIRONMENT_CFG=${DOCUMENT_ROOT}/assets/config/runtime-environment.json \
  ENVIRONMENT_CFG_TPL=${DOCUMENT_ROOT}/assets/config/runtime-environment-template.json \
  ENVIRONMENT_CFG_TMP=/tmp/runtime-environment-temp.json

# Copia fichero plantilla de configuracion de entorno y otorga permiso de escritura al mismo
COPY ./src/environments/runtime-environment-template.json ${ENVIRONMENT_CFG_TPL}
COPY ./src/environments/runtime-environment-template.json ${ENVIRONMENT_CFG}

ENV INDEX_FILE=${DOCUMENT_ROOT}/index.html \
    INDEX_FILE_TMP=/tmp/runtime-index-template-temp.html

# Se setean permisos
RUN chmod ug+w ${ENVIRONMENT_CFG} \
  && chmod ug+x ${NGINX_HOME}/docker-entrypoint.sh

## Nginx default configuration
COPY ./docker/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

# Script entrypoint del contenedor
ENTRYPOINT [ "sh", "-c", "${NGINX_HOME}/docker-entrypoint.sh"]
