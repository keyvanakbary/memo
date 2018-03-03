package com.keyvanakbary.memo.web.config;

import com.keyvanakbary.memo.core.domain.post.Post;
import com.keyvanakbary.memo.core.domain.post.PostCommandHandler;
import com.keyvanakbary.memo.core.domain.post.PostRepository;
import com.keyvanakbary.memo.core.infrastructure.persistence.AxonPostRepository;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostConfig {
    @Bean
    public PostRepository postRepository(EventStore eventStore) {
        return new AxonPostRepository(new EventSourcingRepository<>(Post.class, eventStore));
    }

    @Bean
    public PostCommandHandler postCommandHandler(PostRepository postRepository) {
        return new PostCommandHandler(postRepository);
    }
}
