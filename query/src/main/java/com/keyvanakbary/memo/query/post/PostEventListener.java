package com.keyvanakbary.memo.query.post;

import com.keyvanakbary.memo.api.post.PostContentChangedEvent;
import com.keyvanakbary.memo.api.post.PostCreatedEvent;
import com.keyvanakbary.memo.api.post.PostSlugChangedEvent;
import com.keyvanakbary.memo.api.post.PostTitleChangedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Transactional("queryPlatformTransactionManager")
public class PostEventListener {
    private final PostQueryRepository postQueryRepository;

    @EventHandler
    public void on(PostCreatedEvent event) {
        PostEntry p = new PostEntry();
        p.setPostId(event.getId());
        p.setTitle(event.getTitle());
        p.setSlug(event.getSlug());
        p.setContent(event.getContent());
        postQueryRepository.save(p);
    }

    @EventHandler
    public void on(PostSlugChangedEvent event) {
        PostEntry p = postQueryRepository.findOne(event.getId());
        p.setSlug(event.getSlug());
        postQueryRepository.save(p);
    }

    @EventHandler
    public void on(PostTitleChangedEvent event) {
        PostEntry p = postQueryRepository.findOne(event.getId());
        p.setTitle(event.getTitle());
        postQueryRepository.save(p);
    }

    @EventHandler
    public void on(PostContentChangedEvent event) {
        PostEntry p = postQueryRepository.findOne(event.getId());
        p.setContent(event.getContent());
        postQueryRepository.save(p);
    }
}
