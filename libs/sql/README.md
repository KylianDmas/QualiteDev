# SQL Change Logs

These are XML Liquibase change logs for managing database schema changes.

## Components

- [Platform](platform/main-changelog.xml): Change log for the basic features :
  - Schema creation
  - Event log
  - Outbox
- [Product Registry](product-registry/domain-changelog.xml): Change log for the product catalog features :
  - Product management
  - Product Registry View

## Environment

Liquibase is available as a sidecar devcontainer.

## Run

To run the changes, use the following command:

```bash
liquibase --changeLogFile=master.xml --search-path=/liquibase/changelog update
```