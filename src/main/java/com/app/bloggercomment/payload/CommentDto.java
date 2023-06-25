package com.app.bloggercomment.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    @NotEmpty
    @Size(min = 10, message = "Message body should have at least 10 characters")
    private String body;
}
