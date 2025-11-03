
# Development Quality - Order Flow

A comprehensive DDD, CQRS, event-driven application for customer's order and stock management.

This application stack is designed for integrating into an ecosystem needing generic customer and order management.

## Installation

### Dev environment

This software project is designed to be run in a Docker environment. It uses devcontainer specification to provide a consistent development environment.

To run this project in a dev environment, you need to have Docker and Docker Compose installed on your machine.

1. Clone the repository
2. Open the project in Visual Studio Code / IntelliJ IDEA or any other IDE that supports devcontainer.
3. Open the project in the devcontainer.

Supported IDEs :
- Visual Studio Code
- IntelliJ IDEA

#### Pre-requisites

- Docker
- Docker Compose
- Visual Studio Code / IntelliJ IDEA
- Java 17+ (included)
- Gradle 8.14+ (included)
- Node.js 24+ (included)
- pnpm 10.15+ (included)

#### Mono-repository

This project is a mono-repository. It contains multiple packages that are designed to work together.

Applications :
- `apps/store-back` : Store Back-For-Front exposing backend features
- `apps/store-front` : Store Front-End exposing GUI features
- `apps/product-registry` : the product registry microservices, managing products
  - `product.registry` : the command microservice, handling product registry business logic
  - `product.registry.read` : the read microservice, handling product registry read queries
- `apps/product-catalog` : the product catalog microservices, managing product catalog
  - `product.catalog` : the command microservice, handling product catalog business logic
  - `product.catalog.read` : the read microservice, handling product catalog read queries

Libraries :
- `libs/cqrs-support` : a library exposing utilities for event, projection, typings and persistence
- `libs/kernel` : a library exposing the core business logic and domain models
- `libs/sql`: a package containing Liquibase changelog
- `libs/bom-platform` : a library factorizing the Bill of Materials for the platform
- `libs/contracts/*` : modules exposing the contracts for the different services, holding transitional data structures

## Features

This application allows to manage products, catalogs as an admin and consult catalogs as a customer.
This application does not cover customer management, order processing nor delivery processing.
This application does not cover stock management.

### API

The main API is exposed through various Back-For-Front services. Internal services communicate with each other using events and commands passed to RESTful endpoints.

### Product registry

The product registry is a list of products that can be integrated into a catalog. Each product has a name, a description

### Product catalog

The product catalog is a list of products that can be ordered by customers. Each entry includes a price.

## Documentation

[Go to index](./doc/index.md)

## Installation

### Development

Run to install the Node dependencies:

```bash
pnpm install
```

Run to build the java project:

```bash
gradle build
```

Run quarkus application modules:

```bash
gradle <module_name>:quarkusDev
# Module names typically follows the pattern: apps:<app_name>
```

Run angular application:

```bash
pnpm start --filter <apps-store-front>
```

### Production deployment

TODO
    
## Authors

- Thibaud FAURIE :
  - [@thibaud.faurie (Private GitLab)](https://gitlab.cloud0.openrichmedia.org/thibaud.faurie)
  - [@thibaud-faurie (LinkedIn)](https://www.linkedin.com/in/thibaud-faurie/)

