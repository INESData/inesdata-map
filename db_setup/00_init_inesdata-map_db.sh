#!/bin/bash
set -e
mkdir -pv /var/lib/postgresql/data/inesdata_map_db/{data,indx,lobs}
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE inesdata_map_db ENCODING = 'UTF8';
EOSQL
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "inesdata_map_db" <<-EOSQL
	CREATE USER inesdata_map PASSWORD 'inesdata_map';
	GRANT ALL PRIVILEGES ON DATABASE inesdata_map_db to inesdata_map;
	GRANT ALL ON SCHEMA public TO inesdata_map;
	CREATE TABLESPACE TBS_INESDATA_MAP_DATA OWNER inesdata_map LOCATION '/var/lib/postgresql/data/inesdata_map_db/data';
	CREATE TABLESPACE TBS_INESDATA_MAP_INDX OWNER inesdata_map LOCATION '/var/lib/postgresql/data/inesdata_map_db/indx';
	CREATE TABLESPACE TBS_INESDATA_MAP_LOBS OWNER inesdata_map LOCATION '/var/lib/postgresql/data/inesdata_map_db/lobs';
EOSQL
