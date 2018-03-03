package com.keyvanakbary.memo.core.domain.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostId implements Serializable {
    @NonNull private UUID id;

    @Override
    public String toString() {
        return id.toString();
    }

    public static PostId fromString(String anId) {
        return new PostId(UUID.fromString(anId));
    }

    public String asString() {
        return id.toString();
    }
}
