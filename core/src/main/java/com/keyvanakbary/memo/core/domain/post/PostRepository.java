package com.keyvanakbary.memo.core.domain.post;

import java.util.concurrent.Callable;

public interface PostRepository {
    void add(Callable<Post> aPost);
    Post byId(PostId anId);
}
