# Mapper Backend

- [Description](#description)
- [Docker](#docker)

# Description
Service responsible for saving a data source mapping and generating the corresponding R2RML / RML mapping file.

# Docker

The application must be run as a Docker container. The Dockerfile is located in the `docker` directory. It is a python image with the necessary dependencies to run the application.

The application runs on port 8080 and is executed by the user `mapper`.

## Build
Build the image using the following command:

```
docker build -f docker/Dockerfile --tag com.inesdata-map/mapper-backend .
```
## Run
The image requires the following environment variables:

- `SPRING_DATASOURCE_URL`: URL of the database to be used by the application.
- `SPRING_DATASOURCE_USERNAME`: Username to access the database.
- `SPRING_DATASOURCE_PASSWORD`: Password to access the database.
- `APP_DATAPROCESSINGPATH`: Path to the directory where the data source files and mapping outputs will be stored. It must be a directory with write permissions (for example, the home folder).

A volume is needed to store the datasource's files. It is important that it is mounted into the same directory as defined on the environment variable `APP_DATAPROCESSINGPATH`.
 
For starting a container, run the following command:
```
docker run --name mapper-backend -p 8080:8080 -d --env-file ./docker/env.list -v $(pwd)/inesdata-map-data:/home/mapper/data -d com.inesdata-map/mapper-backend:latest
```
