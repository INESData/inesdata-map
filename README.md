# Introduction

INESData Map is an intuitive web application that enables users to define relationships between data, manage and store mapping rules, and streamline the process of building knowledge graphs. It includes an efficient engine based on the [Morph](https://github.com/morph-kgc/morph-kgc) tool, capable of interpreting declarative mapping rules and generating the corresponding knowledge graphs.

To ensure the integrity and availability of resources used in graph construction, the application also includes features for managing their lifecycle, such as a history of materializations.

Finally, generative AI techniques are integrated to allow automatic creation of mapping rules. This is achieved through the development of language models capable of understanding and generating mapping rules tailored to the user’s inputs.

For further information, please check the [User Guide](docs/GMV-INESDATA-MAP-GU-001_v3.0.pdf).

# Get started with INESData Map

This is a guide for launching the INESData Map application using Docker in a local environment.

This project contains git submodules for the [Map editor backend](https://github.com/INESData/inesdata-map-editor-backend) and the [Map editor frontend](https://github.com/INESData/inesdata-map-editor-frontend) repositories.

To clone this repository and initialize and update each submodule, use the following command:

```bash
git clone --recurse-submodules https://github.com/INESData/inesdata-map
```

If you already cloned the project and forgot `--recurse-submodules`, you can by running the following command:

```bash
git submodule update --init
```

To update the submodules to the latest commit, run the following command:

```bash
git submodule update --remote
```

## Prerequisites

- Install [Docker](https://docs.docker.com/engine/install/) and [Docker Compose](https://docs.docker.com/compose/install/)
- The following ports must not be occupied: 80, 8080, 5432

## Previous steps

Check that openssl is installed:

```
openssl version
```

If not, install it:

- Linux

  ```
  sudo apt install openssl
  ```

- macOS

  ```
  brew install openssl
  ```

- Windows

  - Download and install the .exe file Win32/Win64 OpenSSL Light version from https://slproweb.com/products/Win32OpenSSL.html

  - Add the path to the OpenSSL installation, for instance C:\Program Files\OpenSSL-Win64\bin, to the PATH environment variable.

Verify that the installation was successful by running the command `openssl version` and generate a secret key to encrypt and decrypt the passwords of the data sources:

```
openssl enc -aes-128-cbc -k secret -P -md sha1
```

## Services

This application consists of the following services:

- Map editor frontend (port 80)
- Map editor backend (port 8080)
- PostgreSQL database (port 5432)

## Configuration

The configuration of the application is done through different environment variables. The environment variables are defined in the `docker-compose.yaml` file, under the `map-editor-backend` service.

Configure the following environment variables before starting the application:

- `APP_CIPHERKEY`: Previously generated secret key used to encrypt and decrypt the passwords of the data sources.

**Every user who wants to use an AI model must be registered in the INESdata platform and added as a collaborator in the Kubeflow namespace where that model is deployed. Please, contact your system administrator for more information.**

The following environment variables must be configured before starting the application in order to use the [generative AI module](https://github.com/INESData/inesdata-map-gen-ai):

- `KUBEFLOW_LLM_ENDPOINT`: The LLM endpoint (e.g., `https://kubeflow.ai.inesdata-project.eu/openai/v1/completions`)
- `KUBEFLOW_LLM_HOST`: The KServe host (e.g., `mixtral87b.XXX.kserve.ai.inesdata-project.eu`)
- `KUBEFLOW_USERNAME`: Your Kubeflow username
- `KUBEFLOW_PASSWORD`: Your Kubeflow password
- `HF_TOKEN`: [Hugging Face user access token](https://huggingface.co/settings/tokens) for tokenizer model (e.g. `hf_XXX`)

NOTE: If LLM model is outside Kubeflow's INESDATA platform, there's the possibility of using Azure OpenAI LLM model by following these steps:
1. Recommended LLM model (the one that has been used in development tests of [gen-ai package](https://github.com/INESData/inesdata-map-gen-ai)) to deploy in Azure OpenAI is `gpt-40-mini`, which is already specified in LLM parameters `model` and `model-id` from file `gen_ai_mapping/azure_llm_params.json`.
2. To connect to the Azure model instead of the Kubeflow model (the default model), it's necessary to delete the Kubeflow environment variable `KUBEFLOW_LLM_ENDPOINT`.
3. Create new environment variable `AZURE_LLM_ENDPOINT` for new Azure LLM endpoint (e.g. `https://<azure-openai-url>.openai.azure.com/openai/deployments/`).
4. Create new environment variable `AZURE_API_KEY` for Azure credentials.

## Starting the application

To start the services, the following command must be executed from a command line window:

```bash
docker compose -p inesdata-map up --build -d
```

## Verifying the installation

To check the status of the containers, the following command must be executed from a command line window:

```bash
docker ps

CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
d8b44bac3798 inesdata-map/mapper-backend "./docker-entrypoint…" 25 minutes ago Up About a minute 0.0.0.0:8080->8080/tcp, :::8080->8080/tcp inesdata-map-map-editor-backend-1
19cadc44820e inesdata-map/mapper-frontend "sh -c ${NGINX_HOME}…" 25 minutes ago Up About a minute 0.0.0.0:80->80/tcp, :::80->80/tcp inesdata-map-map-editor-frontend-1
d2b956be18b8 postgres:16.4 "docker-entrypoint.s…" 25 minutes ago Up About a minute (healthy) 0.0.0.0:5432->5432/tcp, :::5432->5432/tcp inesdata-map-db-postgres-1
```

This will indicate that the three containers have started successfully. To access the application, open a web browser and enter the URL http://localhost:80.

## Stopping the application

To stop the services, the following command must be executed from a command line window:

```bash
docker compose -p inesdata-map stop
```
