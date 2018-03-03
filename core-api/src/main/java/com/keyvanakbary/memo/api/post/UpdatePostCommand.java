package com.keyvanakbary.memo.api.post;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Value
public class UpdatePostCommand {
    @TargetAggregateIdentifier
    private String postId;
    private String title;
    private String slug;
    private String content;
}
