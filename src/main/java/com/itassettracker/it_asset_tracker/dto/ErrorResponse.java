package com.itassettracker.it_asset_tracker.dto;
import java.time.LocalDateTime;
public record ErrorResponse(LocalDateTime timestamp, int status, String message, String path) {}
