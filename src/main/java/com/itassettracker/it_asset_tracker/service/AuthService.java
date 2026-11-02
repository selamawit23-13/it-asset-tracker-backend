
package com.itassettracker.it_asset_tracker.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itassettracker.it_asset_tracker.dto.LoginRequest;
import com.itassettracker.it_asset_tracker.dto.LoginResponse;
import com.itassettracker.it_asset_tracker.dto.RegisterRequest;
import com.itassettracker.it_asset_tracker.dto.UserResponse;
import com.itassettracker.it_asset_tracker.entity.Role;
import com.itassettracker.it_asset_tracker.entity.User;
import com.itassettracker.it_asset_tracker.exception.ResourceNotFoundException;
import com.itassettracker.it_asset_tracker.repository.UserRepository;
import com.itassettracker.it_asset_tracker.security.JwtService;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository users,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.users = users;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();

        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already registered.");
        }

        User user = new User(
                username,
                encoder.encode(request.getPassword()),
                Role.ROLE_STAFF);

        User saved = users.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole());
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername().trim();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = users.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + userDetails.getUsername()));

        String token = jwtService.generateToken(userDetails.getUsername());

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                user.getUsername(),
                user.getRole().name());
    }

    public User getUser(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + id));
    }
}
