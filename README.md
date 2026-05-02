# Inventory Management

[![Java CI with Maven](https://github.com/arvind088/inventory-management/actions/workflows/maven.yml/badge.svg)](https://github.com/arvind088/inventory-management/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=alert_status)](https://sonarcloud.io/project/overview?id=arvind088_inventory-management)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=vulnerabilities)](https://sonarcloud.io/component_measures?metric=vulnerabilities&id=arvind088_inventory-management)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=bugs)](https://sonarcloud.io/component_measures?metric=bugs&id=arvind088_inventory-management)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=code_smells)](https://sonarcloud.io/component_measures?metric=code_smells&id=arvind088_inventory-management)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=coverage)](https://sonarcloud.io/component_measures?metric=coverage&id=arvind088_inventory-management)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=duplicated_lines_density)](https://sonarcloud.io/component_measures?metric=duplicated_lines_density&id=arvind088_inventory-management)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=arvind088_inventory-management&metric=sqale_index)](https://sonarcloud.io/component_measures?metric=sqale_index&id=arvind088_inventory-management)

A simple Java Swing inventory management application built with Maven.

The project follows a TDD workflow with unit tests, MongoDB integration tests, Swing UI tests, end-to-end tests, JaCoCo coverage, PIT mutation testing, GitHub Actions, and SonarCloud analysis.

SonarCloud project: [arvind088_inventory-management](https://sonarcloud.io/project/overview?id=arvind088_inventory-management)

## Requirements

- Java 17
- Docker running
- Maven Wrapper included in the project

Docker is used by Testcontainers during integration and end-to-end tests. No manual MongoDB setup or Docker Compose file is required for the tests.

## Build And Test

From the project module:

```bash
cd com.examples.inventory
./mvnw -B verify
```

Or, if Maven is installed locally:

```bash
mvn -B verify
```

This runs unit tests, integration tests, and end-to-end tests.

## Coverage

```bash
./mvnw -B verify -Pjacoco
```

Or:

```bash
mvn -B verify -Pjacoco
```

The JaCoCo report is generated in:

```text
com.examples.inventory/target/site/jacoco/index.html
```

## Mutation Testing

```bash
./mvnw -B test -Ppit
```

Or:

```bash
mvn -B test -Ppit
```

The PIT report is generated in:

```text
com.examples.inventory/target/pit-reports/index.html
```

## Application

The Swing application stores products in MongoDB. The application accepts MongoDB connection values from command-line options:

```text
--mongo-host
--mongo-port
--db-name
--db-collection
```

## Project Structure

```text
com.examples.inventory/
  src/main/java       Application code
  src/test/java       Unit and Swing view tests
  src/it/java         MongoDB integration tests
  src/e2e/java        End-to-end Swing and MongoDB tests
```

## References

- [TDD, Build Automation and CI](https://leanpub.com/tdd-buildautomation-ci)
- [Book examples repository](https://github.com/LorenzoBettini/tdd-buildautomation-ci-book-examples)
