package com.jeremias.dev.dto.request;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.google.common.collect.ImmutableList;
import com.jeremias.dev.persistence.entity.Article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
public class CreateArticleRequest {
	@NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String body;

    
    private ImmutableList<String> tagList = ImmutableList.of();

    public Article toArticle(final String id, final String authorId) {
        return Article.builder()
                .id(id)
                .authorId(authorId)
                .description(description)
                .title(title)
                .body(body)
                .tags(Collections.unmodifiableList(tagList))
                .build();
    }

    public CreateArticleRequest setTagList(List<String> tagList) {
        this.tagList = ImmutableList.copyOf(tagList);
        return this;
    }
}
