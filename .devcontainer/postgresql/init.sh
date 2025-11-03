#!/bin/bash

psql -U postgres <<-EOSQL
-- Create main application database
CREATE DATABASE order_flow;

-- Create user for application
CREATE USER order_flow WITH PASSWORD '${POSTGRES_ORDERFLOW_PASSWORD}';
GRANT ALL PRIVILEGES ON DATABASE order_flow TO order_flow;
EOSQL
