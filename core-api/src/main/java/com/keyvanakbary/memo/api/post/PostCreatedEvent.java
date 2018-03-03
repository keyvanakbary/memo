package com.keyvanakbary.memo.api.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreatedEvent {
    private String id;
    private String title;
    private String slug;
    private String content;
}
