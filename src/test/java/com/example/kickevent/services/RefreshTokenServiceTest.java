package com.example.kickevent.services;

import com.example.kickevent.model.RefreshToken;
import com.example.kickevent.model.User;
import com.example.kickevent.repositories.RefreshTokenRepository;
import com.example.kickevent.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 3_600_000L);
    }

    @Test
    void createRefreshTokenRotatesExistingTokenForUser() {
        User user = User.builder().id(1L).userName("user").build();
        RefreshToken existingToken = new RefreshToken();
        existingToken.setUser(user);
        existingToken.setToken("old-token");
        existingToken.setExpiryDate(new Date(0));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken rotatedToken = refreshTokenService.createRefreshToken(1L);

        assertSame(existingToken, rotatedToken);
        assertNotEquals("old-token", rotatedToken.getToken());
        verify(refreshTokenRepository).save(existingToken);
    }
}
