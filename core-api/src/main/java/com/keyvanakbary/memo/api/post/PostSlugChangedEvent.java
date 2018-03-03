package com.keyvanakbary.memo.api.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSlugChangedEvent {
    private String id;
    private String slug;
}
