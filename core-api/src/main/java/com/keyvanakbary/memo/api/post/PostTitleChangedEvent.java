package com.keyvanakbary.memo.api.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTitleChangedEvent {
    private String id;
    private String title;
}
