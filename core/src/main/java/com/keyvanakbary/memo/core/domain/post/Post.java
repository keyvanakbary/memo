package com.keyvanakbary.memo.core.domain.post;

import com.keyvanakbary.memo.api.post.PostContentChangedEvent;
import com.keyvanakbary.memo.api.post.PostCreatedEvent;
import com.keyvanakbary.memo.api.post.PostSlugChangedEvent;
import com.keyvanakbary.memo.api.post.PostTitleChangedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@AggregateRoot
@NoArgsConstructor
public class Post {
    @AggregateIdentifier
    @Getter @NonNull private PostId postId;
    @Getter @NonNull private Title title;
    @Getter @NonNull private Slug slug;
    @Getter @NonNull private Content content;

    public Post(PostId postId, Title title, Slug slug, Content content) {
        apply(new PostCreatedEvent(postId.asString(), title.asString(), slug.asString(), content.asString()));
    }

    public void updateTitle(Title aTitle) {
        if (!aTitle.equals(title)) {
            apply(new PostTitleChangedEvent(postId.asString(), aTitle.asString()));
        }
    }

    public void updateSlug(Slug aSlug) {
        if (!aSlug.equals(slug)) {
            apply(new PostSlugChangedEvent(postId.asString(), aSlug.asString()));
        }
    }

    public void updateContent(Content someContent) {
        if (!someContent.equals(content)) {
            apply(new PostContentChangedEvent(postId.asString(), someContent.asString()));
        }
    }

    @EventSourcingHandler
    public void on(PostCreatedEvent event) {
        postId = PostId.fromString(event.getId());
        title = Title.fromString(event.getTitle());
        slug = Slug.fromString(event.getSlug());
        content = Content.fromString(event.getContent());
    }

    @EventSourcingHandler
    public void on(PostTitleChangedEvent event) {
        title = Title.fromString(event.getTitle());
    }

    @EventSourcingHandler
    public void on(PostSlugChangedEvent event) {
        slug = Slug.fromString(event.getSlug());
    }

    @EventSourcingHandler
    public void on(PostContentChangedEvent event) {
        content = Content.fromString(event.getContent());
    }
}
