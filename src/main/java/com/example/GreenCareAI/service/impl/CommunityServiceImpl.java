package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.dto.request.CommentRequest;
import com.example.GreenCareAI.dto.request.PostRequest;
import com.example.GreenCareAI.entity.Comment;
import com.example.GreenCareAI.entity.Post;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.repository.CommentRepository;
import com.example.GreenCareAI.repository.PostRepository;
import com.example.GreenCareAI.repository.UserRepository;
import com.example.GreenCareAI.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public Post createPost(Long userId, PostRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);

        return postRepository.save(post);
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found");
        }
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment addComment(Long userId, Long postId, CommentRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setPost(post);

        return commentRepository.save(comment);
    }
}
