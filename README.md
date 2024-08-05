# SPRING SECURITY JWT and EMAIL VALIDATION with SPRING [![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

This is a JWT Security template, ready to be used as a base to new projects.

## About this project

This project aims to create a jwt secured template, with email validation, making it easy to be used as template to new projects that should have JWT security.

### The main goals for this project are:

* User can create an account
* User can log in and receive a JWT Token
* User count is initially disabled
* User receives an activation email right after account is created
* Email has a code to activate the account
* Code should be persisted along with an expiration time
* The activation endpoint should check if code is still valid
* If valid, account is activated, if not, another email is sent with a new valid code
* Password can be changed
* Exception should be handled properly

## How to run this project locally

1. Clone this project
2. Open the folder you just created on your favorite IDE
3. Install dependencies with Maven
4. Access local folder with `cd local`
5. Run `docker compose up`, which will start postgres and email dev server.
6. Local db and mail variables are already configured on application.properties. You may change it if necessary
7. Start the project
8. Project will run on `http://localhost:8080`

## Documentation

This project has been fully documented with SpringDoc/Swagger. You can find all the endpoints, together with the expected inputs/outputs following the steps bellow.

### How to access documentation

1. Navigate to `http://localhost:8080/swagger-ui/index.html#/`
2. If you're trying to test some protected endpoints (marked with a padlock), make sure you authenticate before. A JWT will be necessary.

## Technologies

* Java 21 and Spring Boot 3.3.2
* Spring Security and Spring Security Oauth2
* JWT authentication
* Spring WEB and Spring Data JPA
* Bean validation with Hibernate Validator
* Lombok
* SpringDoc/Swagger
* Postgres
* Spring Email


## Found a bug or want to contribute to this project?

If you've found a bug, make sure you open an Issue on this project repository. Also, all users are welcome to submit pull requests, but remember to mention on the PR which Issue are you fixing on it.



