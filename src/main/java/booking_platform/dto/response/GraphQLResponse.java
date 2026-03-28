package booking_platform.dto.response;

import lombok.Getter;
import lombok.Setter;

/*
    GraphQL Mutation
 */

@Getter
@Setter
public class GraphQLResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public GraphQLResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> GraphQLResponse<T> success(String message, T data) {
        return new GraphQLResponse<>(true, message, data);
    }
}