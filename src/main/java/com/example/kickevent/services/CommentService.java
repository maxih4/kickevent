package com.example.kickevent.services;

import com.example.kickevent.model.Comment;
import com.example.kickevent.repositories.CommentRepository;
import com.example.kickevent.repositories.EventRepository;
import com.example.kickevent.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    public Optional<List<Comment>> getComments(Long id) {
        return this.commentRepository.findByEventId(id);
    }

    public Comment createComment(Comment comment, Long id, Authentication auth) {

        comment.setEvent(this.eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not available")));
        Date date = Date.from(Instant.now());
        comment.setCreated(date);
        comment.setLastEdited(date);
        comment.setOwner(userRepository.findByUserName(auth.getName()).orElseThrow(() -> new RuntimeException("Wrong Owner")));
        return this.commentRepository.save(comment);

    }

    public void deleteComment(Comment comment){
        this.commentRepository.delete(comment);
    }

    public Optional<Comment> getComment(Long id){
    return this.commentRepository.findCommentById(id);
    }

    //Todo Exception
    public void deleteCommentById(Long id){
        this.commentRepository.delete(this.commentRepository.findCommentById(id).orElseThrow(()->new RuntimeException("Comment not found")));
    }

    //Todo Exception
    public Comment updateComment(Long id, Comment comment){
        Comment newComment = this.commentRepository.findCommentById(id).orElseThrow(()->new RuntimeException("Comment not found"));
        newComment.setContent(comment.getContent());
        newComment.setLastEdited(Date.from(Instant.now()));
        return this.commentRepository.save(newComment);
    }
}
