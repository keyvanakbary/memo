package com.keyvanakbary.memo.core.domain.post;

import com.keyvanakbary.memo.api.post.CreatePostCommand;
import com.keyvanakbary.memo.api.post.UpdatePostCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;

@AllArgsConstructor
public class PostCommandHandler {
    private final PostRepository postRepository;

    @CommandHandler
    public void handleCreatePost(CreatePostCommand command) {
        postRepository.add(() -> new Post(
            PostId.fromString(command.getPostId()),
            Title.fromString(command.getTitle()),
            Slug.fromString(command.getSlug()),
            Content.fromString(command.getContent())
        ));
    }

    @CommandHandler
    public void handleUpdatePost(UpdatePostCommand command) {
        Post p = postRepository.byId(PostId.fromString(command.getPostId()));

        if (command.getTitle() != null) {
            p.updateTitle(Title.fromString(command.getTitle()));
        }

        if (command.getContent() != null) {
            p.updateContent(Content.fromString(command.getContent()));
        }

        if (command.getSlug() != null) {
            p.updateSlug(Slug.fromString(command.getSlug()));
        }
    }
}
