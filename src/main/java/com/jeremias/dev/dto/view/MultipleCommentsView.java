package com.jeremias.dev.dto.view;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode
public class MultipleCommentsView {
    private List<CommentView> comments = Collections.emptyList();
    
    public static MultipleCommentsView makeInstance(final List<CommentView> comments) {
        return new MultipleCommentsView()
                .setComments(comments);
    }
    
    public List<CommentView> getComments(){
    	return ImmutableList.copyOf(this.comments);
    }
    
    public MultipleCommentsView setComments(List<CommentView> comments) {
    	this.comments =  ImmutableList.copyOf(comments);
    	return this;
    }
}
