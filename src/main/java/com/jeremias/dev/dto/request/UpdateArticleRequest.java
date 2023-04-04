package com.jeremias.dev.dto.request;

import com.jeremias.dev.validation.NotBlankOrNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateArticleRequest {
	@NotBlankOrNull
    private String title;

    @NotBlankOrNull
    private String description;

    @NotBlankOrNull
    private String body;
}
