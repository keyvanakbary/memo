package com.keyvanakbary.memo.query.post;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostQueryRepository extends PagingAndSortingRepository<PostEntry, String> {
}
