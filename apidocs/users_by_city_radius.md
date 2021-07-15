# Get users by city radius

**This is the main requirement of the assignment.** It finds all the users that live/are registered
around the specified city. Optionally, the user can specify the radius (default value: 50)

**URL** : `/london-users/api/v1/users/city-radius/{cityName}`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

## Parameters

**Name**: `cityName`
* **Type:** String
* **Description**: The name of the city
* **Required**: Yes
* **Location**: Path-variable 

**Name**: `radius`
* **Type:** int
* **Description**: The radius (in miles) from the city center to search for users 
* **Required**: No
* **Default value**: 50
* **Location**: Query-parameter


## Notes

This API supports only 3 cities ("London", "Paris" and "Athens"). For other cities an exception 
will be thrown. Also, the 3rd-party API contains information only for the city of London.

## Success Response

* **Code** : `200 OK` : **Content examples:**  Successfully retrieved list

  ```json
    [
      {
        "email": "string",
        "first_name": "string",
        "id": 0,
        "ip_address": "string",
        "last_name": "string",
        "latitude": 0,
        "longitude": 0
      }
    ]
  ```
* **Code**: `400 BAD_REQUEST` : Negative radius value provided.
