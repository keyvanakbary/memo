package com.keyvanakbary.memo.query.post;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class PostEntry {
    @Id
    private String postId;
    private String title;
    private String slug;
    private String content;
}
