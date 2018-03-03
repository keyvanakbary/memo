package com.keyvanakbary.memo.core.infrastructure.persistence;

import com.keyvanakbary.memo.core.domain.post.Post;
import com.keyvanakbary.memo.core.domain.post.PostId;
import com.keyvanakbary.memo.core.domain.post.PostRepository;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.model.Repository;

import java.util.concurrent.Callable;
import java.util.function.Function;

@AllArgsConstructor
public class AxonPostRepository implements PostRepository {
    private final Repository<Post> postRepository;

    @Override
    public void add(Callable<Post> createPost) {
        try {
            postRepository.newInstance(createPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Post byId(PostId anId) {
        return postRepository.load(anId.asString()).invoke(Function.identity());
    }
}
