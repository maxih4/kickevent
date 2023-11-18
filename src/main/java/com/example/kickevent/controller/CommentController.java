package com.example.kickevent.controller;

import com.example.kickevent.model.Comment;
import com.example.kickevent.repositories.CommentRepository;
import com.example.kickevent.repositories.EventRepository;
import com.example.kickevent.services.CommentService;
import com.example.kickevent.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);


    @Autowired
    private CommentService commentService;


    //Todo new Exception
    @GetMapping("/api/event/{eventId}/comment")
    public List<Comment> getComments(@PathVariable Long eventId){

        return this.commentService.getComments(eventId).orElseThrow(()->new RuntimeException("Event not found"));
    }

    //Todo: Exception
    @PostMapping("/api/event/{eventId}/comment")
    public Comment createComment(@PathVariable Long eventId, @RequestBody Comment comment, Authentication auth){

        return this.commentService.createComment(comment, eventId, auth);
    }

    //Todo Exception
    @DeleteMapping("/api/event/{eventId}/comment")
    public ResponseEntity<?> deleteAllComments(@PathVariable Long eventId){
        List<Comment> comments = this.commentService.getComments(eventId).orElseThrow(()->new RuntimeException("Event not found"));
        for (Comment c:comments) {
            this.commentService.deleteComment(c);
        }
        return new ResponseEntity<>("All comments from event with ID: " + eventId + " deleted", HttpStatus.ACCEPTED);
    }


    //Todo Exception
    @GetMapping("/api/comment/{commentId}")
    public Comment getComment(@PathVariable Long commentId){
        return this.commentService.getComment(commentId).orElseThrow(()->new RuntimeException("Comment not found"));
    }

    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        this.commentService.deleteCommentById(commentId);
        return new ResponseEntity<>("Comment with ID: " + commentId + " deleted", HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/comment/{commentId}")
    public Comment updateComment(@PathVariable Long commentId,@RequestBody Comment comment){
    return this.commentService.updateComment(commentId, comment);
    }
}
