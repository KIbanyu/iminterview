#!/bin/bash
set -e

# Run commands as the primary PostgreSQL user
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE db_customer_service;
    CREATE DATABASE db_transaction_service;
    CREATE DATABASE db_account_service;
EOSQL
