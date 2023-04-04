package com.jeremias.dev.services.article;

import static java.util.Optional.ofNullable;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jeremias.dev.dto.request.CreateArticleRequest;
import com.jeremias.dev.dto.request.CreateCommentRequest;
import com.jeremias.dev.dto.request.UpdateArticleRequest;
import com.jeremias.dev.dto.view.ArticleView;
import com.jeremias.dev.dto.view.CommentView;
import com.jeremias.dev.dto.view.MultipleArticlesView;
import com.jeremias.dev.dto.view.MultipleCommentsView;
import com.jeremias.dev.dto.view.ProfileView;
import com.jeremias.dev.dto.view.TagListView;
import com.jeremias.dev.exception.InvalidRequestException;
import com.jeremias.dev.persistence.entity.Article;
import com.jeremias.dev.persistence.entity.User;
import com.jeremias.dev.persistence.repository.ArticleRepository;
import com.jeremias.dev.persistence.repository.TagRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;;

@Service
@RequiredArgsConstructor
public class ArticleService {
	private final ArticleRepository articleRepository;
	private final TagRepository tagRepository;
	private final UserArticleService userArticleService;

	public Mono<TagListView> getTags() {
		return tagRepository.findAll().collectList().map(TagListView::makeInstance);
	}

	public Mono<ArticleView> createArticle(final Mono<CreateArticleRequest> request, final User author) {
		final String uuid = UUID.randomUUID().toString();
		final ProfileView profileView = ProfileView.toUnfollowedProfileView(author);
		final Mono<Article> newArticle = request.map(e -> e.toArticle(uuid, author.getId()));

		return Mono.defer(() -> this.articleRepository.saveAll(newArticle).single().flatMap(
				article -> Mono.defer(() -> this.tagRepository.saveAllTags(article.getTags()).then(Mono.just(article))))
				.map(article -> ArticleView.toUnfavoredArticleView(article, profileView)));
	}

	public Mono<MultipleArticlesView> feed(final int offset, final int limit, final User currentUser) {
		final var followingAuthorIdS = currentUser.getFollowingIds();
		return Mono.defer(() -> this.articleRepository.findNewestArticlesByAuthorIds(followingAuthorIdS, offset, limit)
				.flatMap(article -> Mono.defer(() -> userArticleService.mapToArticleView(article, currentUser)))
				.collectList().map(MultipleArticlesView::makeInstance));
	}

	public Mono<MultipleArticlesView> findArticles(final String tag, final String authorName,
			final String favoritingUserName, final int offset, final int limit, final Optional<User> currentUser) {
		return this.userArticleService.findArticles(tag, authorName, favoritingUserName, offset, limit, currentUser);
	}
	public Mono<ArticleView> getArticle(final String slug, final Optional<User> currentUser) {
        return Mono.defer(() -> this.articleRepository.findBySlug(slug)
                .flatMap(article -> this.userArticleService.mapToArticleView(article, currentUser)));
    }
	public Mono<ArticleView> getArticle(final String slug, final User currentUser) {
		return Mono.defer(() -> this.articleRepository.findBySlugOrFail(slug)
				.flatMap(article -> this.userArticleService.mapToArticleView(article, currentUser)));
	}

	public Mono<ArticleView> updateArticle(final String slug, final Mono<UpdateArticleRequest> request,
			final User actionUser) {
		return this.articleRepository.findBySlugOrFail(slug).flatMap(article -> {
			if (!article.isAuthor(actionUser)) {
				return Mono.error(new InvalidRequestException("Article", "only the autor can update the article"));
			}
			return request.map(m -> {
				ofNullable(m.getBody()).ifPresent(article::setBody);
				ofNullable(m.getDescription()).ifPresent(article::setDescription);
				ofNullable(m.getTitle()).ifPresent(article::setTitle);
				return article;
			});
		}).flatMap(r -> this.articleRepository.save(r))
				.flatMap(article -> this.userArticleService.mapToArticleView(article, actionUser));
	}

	public Mono<Void> deleteArticle(final String slug, final User currentUser) {
		return this.articleRepository.findBySlugOrFail(slug).flatMap(article -> {
			if (!article.isAuthor(currentUser)) {
				return Mono.error(new InvalidRequestException("Article", "only the autor can delete the article"));
			}
			return articleRepository.delete(article).then();
		});
	}

	public Mono<CommentView> addComment(final String slug, final Mono<CreateCommentRequest> request,
			final User currentUser) {
		return this.userArticleService.addComment(slug, request, currentUser);
	}

	public Mono<Void> deleteComment(final String commentId, final String slug, final User user) {
		return this.userArticleService.deleteComment(commentId, slug, user);
	}

	public Mono<MultipleCommentsView> getComments(final String slug, final Optional<User> user) {
		return this.userArticleService.getComments(slug, user);
	}
	public Mono<ArticleView> favoriteArticle(final String slug, final User actionUser) {
        return updateArticleOnAction(slug, actionUser, ArticleAction.FAVORITE);
    }

    public Mono<ArticleView> unfavoriteArticle(final String slug, final User actionUser) {
        return updateArticleOnAction(slug, actionUser, ArticleAction.UNFAVORITE);
    }

	private Mono<ArticleView> updateArticleOnAction(final String slug,
			final User actionUser, final ArticleAction action
			) {
		return this.articleRepository.findBySlug(slug)
				.flatMap(article -> {
					if(action.act(article, actionUser)) {
						return this.articleRepository.save(article);
					}
					
					return Mono.just(article);
				})
				.flatMap(r -> this.userArticleService.mapToArticleView(r));
	}
	enum ArticleAction{
		FAVORITE,
		UNFAVORITE;
		
		public boolean act(final Article article, final User actionUser) {
			switch (this) {
			case FAVORITE:  return article.favoriteByUser(actionUser);
			case UNFAVORITE: return article.unfavoriteByUser(actionUser);
				
			
			default:
				return false;
				//throw new IllegalArgumentException("Unexpected value: " + this);
			}
		}
	}

}
