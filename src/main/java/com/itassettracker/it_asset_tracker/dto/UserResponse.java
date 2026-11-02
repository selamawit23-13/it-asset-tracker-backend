package com.itassettracker.it_asset_tracker.dto;
import com.itassettracker.it_asset_tracker.entity.Role;
public record UserResponse(Long id, String username, Role role) {}
