# Flight Search API

This project aims to provide flight & airport CRUD API that helps users to securely make any operation.

## Main Features

- CRUD operations both Airports and Flights
- Search operations with full-text and pagination
- Secure connection with JWT
- Role based operations for authorized operations (CUD)
- Cron tasks for continuously updating data

## Technologies Used

- JDK 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Maven
- Liquibase Migration
- Postgres
- OpenAPI Generator
- QueryDSL
- JWT

### [Navigate OpenAPI Documentation](https://github.com/hasangurbuzz/flightsearchapi/blob/master/src/main/resources/openapi/openapi.yaml)

## Installation

#### * Make sure you have all dependencies * 

Clone the repository

```
git clone https://github.com/hasangurbuzz/flightsearchapi.git
```

Navigate to project root directory

```
cd flightsearchapi
```

#### Required env vars must be set in ``src/main/resources/application.yml``
#### Also you can create an application.properties file to reference variables.

| Environment Variable 	 | Purpose                                              	 | Type   	   | Required 	|
|------------------------|-------------------------------------------------------|------------|----------	|
| JWT_SECRET           	 | Secret key for signing JWT keys                      	 | String 	   | True     	|
| JWT_EXPIRATION       	 | JWT expiration time in secs                          	 | Long   	   | True     	|
| POSTGRES_DB_NAME     	 | App is on default will be looking for PostgreSQL DB. 	 | String 	   | True     	|
| POSTGRES_USERNAME    	 | PostgreSQL DB username                               	 | String 	   | True     	|
| POSTGRES_PASSWORD    	 | PostgreSQL DB password                               	 | String 	   | True     	|
| POSTGRES_PORT          | PostgreSQL DB port number                             | String     | True  

#### Then run the project

```
mvn spring-boot:run
```

### ! Unauthenticated users will be denied by API here is the mock users
| Username 	| Password 	| Role     	| Permissions 	|
|----------	|----------	|----------	|-------------	|
| admin    	| admin    	| ADMIN    	| READ, WRITE 	|
| user     	| 1234     	| STANDARD 	| READ        	|

#### [If you don't know how to authenticate click to navigate docs](https://github.com/hasangurbuzz/flightsearchapi/blob/master/src/main/resources/openapi/openapi.yaml)
