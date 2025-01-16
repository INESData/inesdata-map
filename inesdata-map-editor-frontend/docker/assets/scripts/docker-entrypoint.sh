#!/bin/sh

# Remplaza los valores de configuracion en entorno con variables de entorno
if test -f "$ENVIRONMENT_CFG_TPL"; then
    echo "Creando fichero de configuracion desde plantilla con variables de entorno"
    envsubst < $ENVIRONMENT_CFG_TPL > $ENVIRONMENT_CFG_TMP && cp $ENVIRONMENT_CFG_TMP $ENVIRONMENT_CFG && cat $ENVIRONMENT_CFG
    echo "Fichero de configuracion de entorno creado"
else
    echo "No existe fichero plantilla de configuracion en $ENVIRONMENT_CFG_TPL"
fi

# Remplaza el index.html para setear el base href y desplegar en un contexto distinto al raíz
if test -n "$APP_BASE_HREF"; then
    echo "Reemplazando fichero index.html"
    sed -i 's/<base href="\/"/<base href="$APP_BASE_HREF"/' $INDEX_FILE
    envsubst < $INDEX_FILE > $INDEX_FILE_TMP && cp $INDEX_FILE_TMP $INDEX_FILE && cat $INDEX_FILE
    echo "Fichero index.html reemplazado"
fi

echo "Iniciando servidor web nginx"
# Execute nginx with same parameters defined in nginx:stable official dockerfile
nginx -g 'daemon off;'
