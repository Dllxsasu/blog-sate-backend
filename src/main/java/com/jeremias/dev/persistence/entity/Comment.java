package com.jeremias.dev.persistence.entity;

import static java.util.Optional.ofNullable;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {
	@Id
    @EqualsAndHashCode.Include
    @Getter
    private final String id;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private String authorId;

    @Getter
    private final Instant createdAt;

    @Getter
    @LastModifiedDate
    private final Instant updatedAt;
    
    @Builder
    public Comment(final String id,
    		final String body,
    		final String authorId,
    		final Instant createdAt,
    		final Instant updatedAt
    		) {
    	 this.id = id;
         this.body = body;
         this.authorId = authorId;
         this.createdAt = ofNullable(createdAt).orElse(Instant.now());
         this.updatedAt = ofNullable(updatedAt).orElse(this.createdAt);
    	
    }
    public boolean isAuthor(final User user) {
        return authorId.equals(user.getId());
    }
}
