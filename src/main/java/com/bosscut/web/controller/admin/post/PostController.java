package com.bosscut.web.controller.admin.post;

import com.bosscut.common.constant.Constants;
import com.bosscut.domain.Post;
import com.bosscut.domain.Tag;
import com.bosscut.model.CustomResponse;
import com.bosscut.model.PostSearchRequest;
import com.bosscut.service.post.PostService;
import com.bosscut.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("admin_")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @GetMapping("posts")
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @PageableDefault(50) Pageable pageable,
                       Model model){
        PostSearchRequest request = new PostSearchRequest("vi").withKeyword(keyword);
        Page<Post> posts = postService.getPosts(request, pageable);
        model.addAttribute("listItem", posts.getContent());
        return "admin/post/list";
    }

    @GetMapping("post/new")
    public String newPost(Model model){
        List<Tag> tags = tagService.findAll();
        model.addAttribute("tags",tags);
        return "admin/post/create";
    }

    @PutMapping("post/create")
    public ResponseEntity<CustomResponse> createPost(@RequestBody Post post) throws IOException {
        CustomResponse customResponse = new CustomResponse();
        post = postService.add(post);
        customResponse.setResultCode(Constants.ResultCode.SUCCESS);
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

}
