# London users demo API

A demo API in [Spring Boot](https://spring.io/projects/spring-boot) that consumes a 
[3rd Party API](https://bpdts-test-app.herokuapp.com/) to identify people that
are registered or are close to London.

## Prerequisites

For the project to run properly, Java 11 (or later) must be installed on your system. Also, the project users Gradle 
build tool. To run the project:
1. Build the project:
    - macOS / 'nix: `./gradlew build`
    - Windows:  `gradlew.bat build`
      It will also execute the implemented tests
2. Run the project: `./gradlew bootRun`.
3. Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to see the API 
   documentation and test the API
4. You can also send queries via the browser (since only **GET** requests are implemented) or a API test tool (e.g. Postman)

#### Note: Make sure that the port 8080 on your system is not used by another process

## Endpoints

* [Get users by city radius](apidocs/users_by_city_radius.md) : `GET /london-users/api/v1/users/city-radius/{cityName}`

   **This is the main requirement of the assignment.** It finds all the users that live/are registered 
  around the specified city. Optionally, the user can specify the radius (default value: 50)
  
* [Get users by city name](apidocs/users_by_city.md) : `GET /london-users/api/v1/users/city/{cityName}`
  
  This request retrieves all the users from the API that are registered in the specified city
  
* [Get user by id](apidocs/user_by_id.md) : `GET /london-users/api/v1/users/{id}`
  
   This request retrieves the user with the specified id from the API
  
* [Get all users](apidocs/all_users.md) : `GET /london-users/api/v1/users`

  This request retrieves all the users available from the API

* [Get instructions](apidocs/instructions.md) : `GET /london-users/api/v1/instructions`
  
  This request utilizes the `/instructions` endpoint to retrieve the instructions of the assignment