# INESData Map

## Features :sparkles:

Add features

## Documentation :bookmark_tabs:

Add link to docs

## Getting Started :rocket:

## Installation

Clone this repository:

```bash
git clone https://github.com/INESData/inesdata-map
```

Start the GUI:

```bash
docker compose -p inesdata-map up --build -d
```

### Services

This application consists of the following services:

- Map editor frontend (port 80)
- Map editor backend (port 8080)
- PostgreSQL database (port 5432)

### Configuration

The configuration of the application is done through different environment variables. The environment variables are defined in the `docker-compose.yaml` file.

### Verifying the installation

To check the status of the containers, the following command must be executed from a command line window:

```bash
docker ps
```

This will indicate that the three containers have started successfully. To access the application, open a web browser and enter the URL http://localhost:80.

### Stopping the application

To stop the services, the following command must be executed from a command line window:

```bash
docker compose -p inesdata-map stop
```

## License :unlock:

INESData Map is available under the **[Apache License 2.0](https://github.com/INESData/inesdata-map/blob/main/LICENSE)**.
