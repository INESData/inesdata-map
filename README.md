# Get started with INESData Map

This is a guide for launching the INESData Map application using Docker in a local environment.

## Prerequisites

- Install [Docker](https://docs.docker.com/engine/install/) and [Docker Compose](https://docs.docker.com/compose/install/)
- Clone the [Map editor backend](https://github.com/INESData/inesdata-map-editor-backend) and the [Map editor frontend](https://github.com/INESData/inesdata-map-editor-frontend) repositories
- The following ports must not be occupied: 80, 8080, 5432

Clone this repository in a directory called `environment` in the same directory as the backend and frontend repositories. The directory structure should look like this:

```
inesdata-map/
├── inesdata-map-editor-frontend/
├── inesdata-map-editor-backend/
└── environment/
    ├── db_setup/
    │   └── 00_init_inesdata-map_db.sh
    └── docker-compose.yaml
```

## Services

This application consists of the following services:

- Map editor frontend (port 80)
- Map editor backend (port 8080)
- PostgreSQL database (port 5432)

## Configuration

The configuration of the application is done through different environment variables. The environment variables are defined in the `docker-compose.yaml` file.

## Starting the application

To start the services, the following command must be executed from a command line window:

```bash
$ docker compose -p inesdata-map up --build -d
# …
# The process of building the images will begin.
# …
✔ Container inesdata-map-editor-frontend-1 Started
✔ Container inesdata-db-postgres-1 Started
✔ Container inesdata-map-editor-backend-1 Started
```

## Verifying the installation

To check the status of the containers, the following command must be executed from a command line window:

```bash
$ docker ps

CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
d8b44bac3798 inesdata-map/mapper-backend "./docker-entrypoint…" 25 minutes ago Up About a minute 0.0.0.0:8080->8080/tcp, :::8080->8080/tcp inesdata-map-map-editor-backend-1
19cadc44820e inesdata-map/mapper-frontend "sh -c ${NGINX_HOME}…" 25 minutes ago Up About a minute 0.0.0.0:80->80/tcp, :::80->80/tcp inesdata-map-map-editor-frontend-1
d2b956be18b8 postgres:16.4 "docker-entrypoint.s…" 25 minutes ago Up About a minute (healthy) 0.0.0.0:5432->5432/tcp, :::5432->5432/tcp inesdata-map-db-postgres-1
```

This will indicate that the three containers have started successfully. To access the application, open a web browser and enter the URL http://localhost:80.

## Stopping the application

To stop the services, the following command must be executed from a command line window:

```bash
$ docker compose -p inesdata-map stop
```
