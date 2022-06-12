package com.bosscut.service.post;

import com.bosscut.domain.Post;
import com.bosscut.model.PostSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PostService {

    public Post add(Post post) throws IOException;

    public Page<Post> getPosts(PostSearchRequest request, Pageable pageable);

    public Post getPostById(Long id);

    public Post getPostByCode(String code);
}
