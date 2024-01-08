package com.example.kickevent.controller;

import com.example.kickevent.model.Comment;
import com.example.kickevent.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')" )
    @PostMapping("/api/event/{eventId}/comment")
    public Comment createComment(@PathVariable Long eventId, @RequestBody Comment comment, Authentication auth, SecurityContextHolderAwareRequestWrapper req){


        return this.commentService.createComment(comment, eventId, auth);
    }

    //Todo Exception
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN') || @commentService.getComment(#commentId).get().owner.userName==authentication.name")
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId){
        this.commentService.deleteCommentById(commentId);
        return new ResponseEntity<>("Comment with ID: " + commentId + " deleted", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ADMIN') || @commentService.getComment(#commentId).get().owner.userName==authentication.name")
    @PutMapping("/api/comment/{commentId}")
    public Comment updateComment(@PathVariable("commentId") Long commentId,@RequestBody Comment comment){
    return this.commentService.updateComment(commentId, comment);
    }
}
