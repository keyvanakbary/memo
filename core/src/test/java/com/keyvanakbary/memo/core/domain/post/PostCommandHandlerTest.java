package com.keyvanakbary.memo.core.domain.post;

import com.keyvanakbary.memo.api.post.*;
import com.keyvanakbary.memo.core.infrastructure.persistence.AxonPostRepository;
import org.axonframework.messaging.GenericMessage;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostCommandHandlerTest {
    private AggregateTestFixture<Post> fixture;
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(Post.class);
        postRepository = new AxonPostRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(new PostCommandHandler(postRepository));
        initUnitOfWork();
    }

    private static void initUnitOfWork() {
        if (!CurrentUnitOfWork.isStarted()) {
            DefaultUnitOfWork.startAndGet(new GenericMessage<>(""));
        }
    }

    @Test
    void itCreatesAPost() {
        String id = UUID.randomUUID().toString();

        CreatePostCommand command = new CreatePostCommand(id, "A title", "a-slug", "Some content");

        fixture
            .given()
            .when(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(new PostCreatedEvent(id, "A title", "a-slug", "Some content"));

        Post p = postRepository.byId(PostId.fromString(id));
        assertEquals("A title", p.getTitle().asString());
        assertEquals("a-slug", p.getSlug().asString());
        assertEquals("Some content", p.getContent().asString());
    }

    @Test
    void itUpdatesAPost() {
        String id = UUID.randomUUID().toString();

        UpdatePostCommand command = new UpdatePostCommand(id, "A New Title", "a-new-slug", "Some new content");

        fixture
            .given(new PostCreatedEvent(id, "A title", "a-slug", "Some content"))
            .when(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                new PostTitleChangedEvent(id, "A New Title"),
                new PostContentChangedEvent(id, "Some new content"),
                new PostSlugChangedEvent(id, "a-new-slug"));

        Post p = postRepository.byId(PostId.fromString(id));
        assertEquals("A New Title", p.getTitle().asString());
        assertEquals("a-new-slug", p.getSlug().asString());
        assertEquals("Some new content", p.getContent().asString());
    }

    @Test
    void itDoesNotUpdatePostWithoutChanges() {
        String id = UUID.randomUUID().toString();

        UpdatePostCommand command = new UpdatePostCommand(id, "A title", "a-slug", "Some content");

        fixture
            .given(new PostCreatedEvent(id, "A title", "a-slug", "Some content"))
            .when(command)
            .expectSuccessfulHandlerExecution()
            .expectNoEvents();
    }
}
