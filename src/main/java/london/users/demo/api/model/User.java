package london.users.demo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * This class models the users that are both consumed by the API and returned by this app. The naming of
 * fields follow the Java naming conventions and the @JsonProperty annotation is used to map the fields
 * to the json responses.
 */
@Value
public class User {
    @JsonProperty("id")
    int id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("ip_address")
    String ipAddress;

    @JsonProperty("latitude")
    double latitude;

    @JsonProperty("longitude")
    double longitude;
}
