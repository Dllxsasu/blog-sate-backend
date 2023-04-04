package com.jeremias.dev.persistence.repository;

import java.util.function.BiConsumer;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.jeremias.dev.persistence.entity.Tag;

import reactor.core.publisher.Flux;
//If you read this comments from me, It's just for practice purposes, and reminder things, and maybe someone will find it util.
public interface TagRepository extends ReactiveMongoRepository<Tag, String> {
	default Flux<Tag> saveAllTags(final Iterable<String> tags) {
			//
		return Flux.fromIterable(tags).flatMap(it -> save(Tag.makeInstance(it)))
				.onErrorContinue(DuplicateKeyException.class, nothing());
	}
	//take two paraneters and retrn and empty object, just like the name say do nothing
	private BiConsumer<Throwable, Object> nothing() {
		return (throwable, o) -> {
		};
	}
}
