package com.jeremias.dev.dto.view;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jeremias.dev.persistence.entity.Tag;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode
public class TagListView {
    private List<String> tags;
  
    public static TagListView makeInstance(final List<Tag> tags) {
    	final var rowTags = tags.stream().map(Tag::getTagName).toList();
    	return new TagListView().setTags(rowTags);
    }
    public List<String> getTags(){
    	return ImmutableList.copyOf(tags);	
    }
    
    public TagListView setTags(List<String> tags) {
    	this.tags = ImmutableList.copyOf(tags);
    	return this;
    }
}
