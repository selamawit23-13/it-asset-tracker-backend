package com.itassettracker.it_asset_tracker.dto;
import com.itassettracker.it_asset_tracker.entity.AssetStatus;
import java.time.LocalDateTime;
public record AssetResponse(Long id, String name, String serialNumber, AssetStatus status,
    Long categoryId, String categoryName, Long assignedToId, String assignedToUsername,
    LocalDateTime createdAt) {}
