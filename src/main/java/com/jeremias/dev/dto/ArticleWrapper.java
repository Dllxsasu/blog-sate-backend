package com.jeremias.dev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeremias.dev.dto.request.CreateArticleRequest;
import com.jeremias.dev.dto.request.UpdateArticleRequest;
import com.jeremias.dev.dto.view.ArticleView;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ArticleWrapper<T>  {
	@JsonProperty("article")
    private T content;

    @NoArgsConstructor
    public static class ArticleViewWrapper extends ArticleWrapper<ArticleView> {
        public ArticleViewWrapper(final ArticleView article) {
            super(article);
        }
    }

    @NoArgsConstructor
    public static class CreateArticleRequestWrapper extends ArticleWrapper<CreateArticleRequest> {
        public CreateArticleRequestWrapper(final CreateArticleRequest article) {
            super(article);
        }
    }

    @NoArgsConstructor
    public static class UpdateArticleRequestWrapper extends ArticleWrapper<UpdateArticleRequest> {
        public UpdateArticleRequestWrapper(final UpdateArticleRequest article) {
            super(article);
        }
    }
}
