# Bookstore API Tests

This project contains automated API tests for the FakeRestAPI Bookstore (Books and Authors endpoints) using Java, Maven, RestAssured, TestNG, and Lombok. The tests cover happy path and edge case scenarios, with Allure reporting and GitHub Actions CI/CD integration.

## Prerequisites

- **Java**: JDK 21
- **Maven**: 3.8.0 or higher
- **IntelliJ IDEA** (or another IDE)
- **Lombok Plugin**: Required for IntelliJ IDEA
- **Git**: For cloning the repository

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd bookstore-api-tests

2. **Install Lombok in IntelliJ IDEA**:

    Go to File > Settings > Plugins.
    Search for "Lombok" and install the plugin.
    Enable annotation processing: File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors > Enable annotation processing.
    Restart IntelliJ IDEA if prompted.


3. **Install dependencies (without Docker)**:
    ```bash
   mvn clean install

  (Optional) Build Docker image (if using Docker):   
  ```bash
  docker build -t bookstore-api-tests -f image.dockerfile .
  ```

4. **Run tests (without Docker)**:
    ```bash
   mvn clean verify

  To run tests with specific profiles (e.g., ApiTests), use:
   ```bash 
        mvn clean verify -P ApiTests
  ```

  Run tests (with Docker, if built):
```bash
      docker run --rm -v $(pwd):/app bookstore-api-tests mvn clean verify
```

5. **Generate and serve Allure report**:
    ```bash
   mvn allure:report
   mvn allure:serve

This will open the Allure report in your default browser.

**Project Structure**

src/test/java/com/bookstore/api/:
   base: Base setup for RestAssured.

   logger: Custom logging utility.

   model: DTOs for Book and Author.

   service: BookStoreService layer for API calls. Added `IBookStoreService` interface for better testability and adherence to SOLID principles.
Implemented Dependency Injection with constructor injection for `IBookStoreService` with TestNG @Factory.

   tests: TestNG test classes for happy path and edge cases.

   utils: Utility classes for test configuration and execution.

src/test/resources/config/: Configuration files (e.g., application.properties).

src/test/resources/suites/: TestNG configuration files (e.g., testngDefault.xml).

.github/workflows/ci.yml: GitHub Actions workflow for CI/CD.

image.dockerfile: Configuration for Docker-based execution (optional).


**Test Groups**

**smoke**: Critical tests for basic functionality.

**regression**: Full test suite for Books and Authors APIs.

**books**: Tests specific to Books API.

**authors**: Tests specific to Authors API.

**edge**: Edge case tests.


Run specific groups via:
```bash
mvn verify -Dgroups=smoke
```

Run specific configurations via:
```bash
mvn verify -P ApiTests
```
where ApiTests is profile in pom.xml connected with corresponding xml

**CI/CD**
The project uses GitHub Actions for continuous integration. 
Tests are executed on push or pull request to the main branch, and you can choose to run them with or without Docker by setting the use-docker input. 
Additionally, a manual dispatch workflow allows you to trigger tests with the desired configuration.

   **Automatic Triggers**: On push or pull request to main.
   **Manual Dispatch**: Trigger via GitHub Actions tab with use-docker option.
   **Artifacts**: Includes api-console.log and a zipped Allure report.

**API documentation**: [https://fakerestapi.azurewebsites.net/index.html]

**Troubleshooting**

   Lombok errors: Ensure the Lombok plugin is installed and annotation processing is enabled.
   
   Test failures: Check the API base URL (https://fakerestapi.azurewebsites.net) and ensure the API is accessible.
   
   Allure report issues: Verify that mvn allure:serve is run after mvn test.
   
   Docker issues: Ensure Docker is installed and the image is built before running tests. Check image.dockerfile for missing dependencies.

**Contact**

For questions, contact Velibor Mandzo at velja.jagodina@gmail.com

