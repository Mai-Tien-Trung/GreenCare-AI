package com.example.GreenCareAI.controller;


import com.example.GreenCareAI.dto.request.PostRequest;
import com.example.GreenCareAI.dto.request.CommentRequest;
import com.example.GreenCareAI.entity.Comment;
import com.example.GreenCareAI.entity.Post;
import com.example.GreenCareAI.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // cho phép FE gọi
public class CommunityController {

    private final CommunityService communityService;

    // GET: Lấy tất cả post
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(communityService.getAllPosts());
    }

    // GET: Lấy tất cả post theo user
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(communityService.getPostsByUser(userId));
    }

    // POST: Tạo post mới
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(
            @RequestParam Long userId,
            @RequestBody @Valid PostRequest request
    ) {
        return ResponseEntity.ok(communityService.createPost(userId, request));
    }

    // GET: Lấy comment theo post
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(communityService.getCommentsByPost(postId));
    }

    // POST: Thêm comment vào post
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody @Valid CommentRequest request
    ) {
        return ResponseEntity.ok(communityService.addComment(userId, postId, request));
    }
}
