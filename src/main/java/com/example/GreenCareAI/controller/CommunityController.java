package com.example.GreenCareAI.controller;


import com.example.GreenCareAI.dto.request.PostRequest;
import com.example.GreenCareAI.dto.request.CommentRequest;
import com.example.GreenCareAI.entity.Comment;
import com.example.GreenCareAI.entity.Post;
import com.example.GreenCareAI.security.CustomUserDetails;
import com.example.GreenCareAI.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(
            @RequestBody @Valid PostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                communityService.createPost(userDetails.getId(), request)
        );
    }


    // GET: Lấy comment theo post
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(communityService.getCommentsByPost(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(communityService.addComment(userId, postId, request));
    }
    @GetMapping("/posts/count-last30days")
    public ResponseEntity<Long> countPostsLast30Days() {
        return ResponseEntity.ok(communityService.countPostsLast30Days());
    }


}
