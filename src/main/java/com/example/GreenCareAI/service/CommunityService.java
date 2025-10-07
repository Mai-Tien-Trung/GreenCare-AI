package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.request.CommentRequest;
import com.example.GreenCareAI.dto.request.PostRequest;
import com.example.GreenCareAI.entity.Comment;
import com.example.GreenCareAI.entity.Post;

import java.util.List;

public interface CommunityService {
    List<Post> getAllPosts();
    List<Post> getPostsByUser(Long userId);
    Post createPost(Long userId, PostRequest dto);
    List<Comment> getCommentsByPost(Long postId);
    Comment addComment(Long userId, Long postId, CommentRequest dto);
    long countPostsLast30Days();
}

