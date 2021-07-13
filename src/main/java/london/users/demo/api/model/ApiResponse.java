package london.users.demo.api.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse {
    String message;
    String endpoint;
}
