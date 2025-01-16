# Mapper Frontend

- [Description](#description)
- [Docker](#docker)

# Description

Web application that creates a mapping file in RML / R2RML between different data sources and an ontology.

## Client for consuming REST services

To make client-side development easier and faster, the use of [OpenAPI Generator](https://openapi-generator.tech/docs/installation/) is recommended. This way, you can create a library with all the services and models of the API.

The library `mapper-api-client` has been created using the OpenAPI specification of the mapper-backend component. Just copy and paste the OpenAPI JSON file into the `src/openapi` folder and run `npm run build`.

## Generating forms based on OpenApi definition

The OpenApi definition should be the source of truth for the validation rules of the application forms.

The library `mapper-forms` has been created using the OpenAPI specification of the mapper-backend component. Just copy and paste the OpenAPI JSON file into the `src/openapi` folder and run `npm run build`.

# Docker

The application must be run as a Docker container. The Dockerfile is located in the `docker` directory. It is a node.js image and the application runs on port 80.

## Build

Build the image using the following command:

```
docker build -f docker/Dockerfile --tag com.inesdata-map/mapper-frontend .
```

## Run

The image requires the following environment variables:

- `API_BASE_URL`: URL of the mapper-backend API

For starting a container, run the following command:

```
docker run --name mapper-frontend -p 80:80 -e API_BASE_URL=http://mapper-backend -d com.inesdata-map/mapper-backend:latest
```
