package london.users.demo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * This class models the users that are both consumed by the API and returned by this app. The naming of
 * fields follow the Java naming conventions and the @JsonProperty annotation is used to map the fields
 * to the json responses.
 */
@Value
@AllArgsConstructor
public class User {
    @JsonProperty("id")
    @ApiModelProperty(notes = "The user's id")
    int id;

    @JsonProperty("first_name")
    @ApiModelProperty(notes = "The user's first name")
    String firstName;

    @JsonProperty("last_name")
    @ApiModelProperty(notes = "The user's family name")
    String lastName;

    @JsonProperty("email")
    @ApiModelProperty(notes = "The user's registered email")
    String email;

    @JsonProperty("ip_address")
    @ApiModelProperty(notes = "The user's IP address")
    String ipAddress;

    @JsonProperty("latitude")
    @ApiModelProperty(notes = "The user's known latitude")
    double latitude;

    @JsonProperty("longitude")
    @ApiModelProperty(notes = "The user's known longitude")
    double longitude;

}
