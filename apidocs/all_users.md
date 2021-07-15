# Get all users

This request retrieves all the users available from the API

**URL** : `/london-users/api/v1/users`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

## Parameters

None


## Notes

The 3rd-party API contains 1000 users with IDs in the range of `[0,999)`

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
