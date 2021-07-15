# Get users by city name

This request retrieves all the users from the API that are registered in the specified city

**URL** : `/london-users/api/v1/users/city/{cityName}`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

## Parameters

**Name**: `cityName`
* **Type:** String
* **Description**: The name of the city
* **Required**: Yes
* **Location**: Path-variable


## Notes

The 3rd-party API contains information only for the city of London.

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
