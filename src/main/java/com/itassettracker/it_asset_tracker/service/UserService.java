
package com.itassettracker.it_asset_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itassettracker.it_asset_tracker.dto.UserResponse;
import com.itassettracker.it_asset_tracker.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();
    }
}
