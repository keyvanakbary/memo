package com.keyvanakbary.memo.query.post;

import com.keyvanakbary.memo.api.post.FetchPostQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostQueryHandler {
    private final PostQueryRepository postQueryRepository;

    @QueryHandler
    public PostEntry handleFetchPost(FetchPostQuery query) {
        return postQueryRepository.findOne(query.getPostId());
    }
}
