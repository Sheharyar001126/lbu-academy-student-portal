package com.lbu.academy;

import com.lbu.academy.dto.request.RegisterRequest;
import com.lbu.academy.dto.response.AuthResponse;
import com.lbu.academy.entity.PortalUser;
import com.lbu.academy.repository.PortalUserRepository;
import com.lbu.academy.security.JwtUtil;
import com.lbu.academy.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortalUserServiceTest {

    @Mock
    private PortalUserRepository portalUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("test.student");
        registerRequest.setPassword("Test@1234");
        registerRequest.setEmail("test@lbu.ac.uk");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("Student");
    }

    @Test
    void registerNewAccount_Success() {
        when(portalUserRepository.existsByUsername(anyString())).thenReturn(false);
        when(portalUserRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(portalUserRepository.save(any(PortalUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken(anyString())).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("test.student", response.getUsername());
        assertEquals("STUDENT", response.getRole());
        assertNotNull(response.getToken());
    }

    @Test
    void registerAccount_DuplicateUsername_ThrowsException() {
        when(portalUserRepository.existsByUsername("test.student")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));

        assertEquals("Username already taken", ex.getMessage());
    }

    @Test
    void registerAccount_DuplicateEmail_ThrowsException() {
        when(portalUserRepository.existsByUsername(anyString())).thenReturn(false);
        when(portalUserRepository.existsByEmail("test@lbu.ac.uk")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));

        assertEquals("Email already registered", ex.getMessage());
    }

    @Test
    void registerAccount_PasswordIsEncoded() {
        when(portalUserRepository.existsByUsername(anyString())).thenReturn(false);
        when(portalUserRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("Test@1234")).thenReturn("$2a$hashed");
        when(portalUserRepository.save(any(PortalUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken(anyString())).thenReturn("mock.jwt.token");

        authService.register(registerRequest);

        verify(passwordEncoder, times(1)).encode("Test@1234");
    }

    @Test
    void registerAccount_TokenGenerated() {
        when(portalUserRepository.existsByUsername(anyString())).thenReturn(false);
        when(portalUserRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(portalUserRepository.save(any(PortalUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken("test.student")).thenReturn("generated.token");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("generated.token", response.getToken());
    }

    @Test
    void registerAccount_SaveCalledOnce() {
        when(portalUserRepository.existsByUsername(anyString())).thenReturn(false);
        when(portalUserRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(portalUserRepository.save(any(PortalUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken(anyString())).thenReturn("mock.jwt.token");

        authService.register(registerRequest);

        verify(portalUserRepository, times(1)).save(any(PortalUser.class));
    }
}