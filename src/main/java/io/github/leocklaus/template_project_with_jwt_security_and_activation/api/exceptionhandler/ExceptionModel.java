package io.github.leocklaus.template_project_with_jwt_security_and_activation.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionModel {
    @Schema(example = "404", description = "HTTP Status")
    private Integer status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    @Schema(example = "/user-not-found", description = "Exception Type")
    private String type;
    @Schema(example = "User not found", description = "Exception Title")
    private String title;
    @Schema(example = "User not found with id 1", description = "Exception Details")
    private String detail;
    @Schema(example = "User not found with id 1", description = "A friendly exception message for users")
    private String userMessage;
    @Schema(example = "", description = "A list of validation exceptions")
    private List<Object> objects;

    @Getter
    @Builder
    public static class Object {

        private String name;
        private String userMessage;

    }
}
