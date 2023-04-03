package com.jeremias.dev.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@AllArgsConstructor
public class Tag {
	@Id
    private String id;

    @Indexed(unique = true)
    private final String tagName;

    public static Tag makeInstance(final String tag) {
        return new Tag(null, tag);
    }
}
