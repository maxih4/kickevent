package com.example.kickevent.repositories;

import com.example.kickevent.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findByEventId(Long eventId);
    Optional<Comment> findCommentById(Long commentId);

}
