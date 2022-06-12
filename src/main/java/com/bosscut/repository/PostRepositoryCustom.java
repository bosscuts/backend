package com.bosscut.repository;

import com.bosscut.domain.Post;
import com.bosscut.model.PostSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    void lock(long id);

    Page<Post> search(PostSearchRequest request, Pageable pageable);
}
