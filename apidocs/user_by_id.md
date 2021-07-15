# Get users by id

This request retrieves the user with the specified id from the API

**URL** : `/london-users/api/v1/users/{id}`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

## Parameters

**Name**: `id`
* **Type:** `integer`
* **Description**: The id of the user
* **Required**: Yes
* **Location**: Path-variable


## Notes

The 3rd-party API contains 1000 users with IDs in the range of `[0,999)`

## Success Response

* **Code** : `200 OK` : **Content examples:**  Successfully retrieved list

  ```json
      {
        "email": "string",
        "first_name": "string",
        "id": 0,
        "ip_address": "string",
        "last_name": "string",
        "latitude": 0,
        "longitude": 0
      }
  ```
* **Code** : `404 NOT_FOUND` : The resource you were trying to reach is not found
