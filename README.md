# Congestion Tax Calculator

Welcome the Volvo Cars Congestion Tax Calculator assignment.

This repository contains a developer [assignment](ASSIGNMENT.md) used as a basis for candidate intervew and evaluation.

Clone this repository to get started. Due to a number of reasons, not least privacy, you will be asked to zip your solution and mail it in, instead of submitting a pull-request. In order to maintain an unbiased reviewing process, please ensure to **keep your name or other Personal Identifiable Information (PII) from the code**.

## How to run application locally

### Run with configuration file
- check that you have java 11, run in the command line
  `
  java --version
  `
  if you don't have an installed java then navigate to the next page:
  https://jdk.java.net/archive/
- check that you have Maven, run in the command line
  `
  mvn --version
  `
- if you don't have an installed maven then navigate to the next page:
  https://maven.apache.org/

- Extract zip archive
- Go to the project's directory and run the following command to build the application:
  `
  mvn clean package
  `
- Run the application with the default configuration:
  `
  java -jar target/congestion-tax-calculator-0.0.1-SNAPSHOT.jar
  `

- You can run application from the IDE or using maven:
  `
  mvn spring-boot:run
  `

### Run with external tax configuration file

- set a path to the property `configuration.file.path` in
  the [application.properties](src/main/resources/application.properties),
  e.g.

  ```properties
  configuration.file.path=file:/opt/taxes/tax_configuration.json
  ```

or

- pass it as parameter:

  ```bash
  mvn spring-boot:run -Dspring-boot.run.arguments=--configuration.file.path="file:/opt/tax/tax_configuration.json"
  ```

### How to call application API via HTTP using curl

  ```bash
  curl -v 'http://localhost:8080/api/calculator' \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json;' \
    --data-raw '{
        "city": "GOTHENBURG",
        "vehicleType" : "CAR",
        "dates": ["2013-01-14T21:00:00",
                  "2013-01-15T21:00:00",
                  "2013-02-07T06:23:27",
                  "2013-02-07T15:27:00",
                  "2013-02-08T06:20:27",
                  "2013-02-08T06:27:00",
                  "2013-02-08T14:35:00"]
  }'
  ```
