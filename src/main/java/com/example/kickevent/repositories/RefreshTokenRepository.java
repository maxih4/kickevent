package com.example.kickevent.repositories;

import com.example.kickevent.model.RefreshToken;
import com.example.kickevent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);


    @Modifying
    int deleteByUser(User user);
}
