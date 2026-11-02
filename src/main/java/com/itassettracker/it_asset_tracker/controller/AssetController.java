package com.itassettracker.it_asset_tracker.controller;
import com.itassettracker.it_asset_tracker.dto.*;
import com.itassettracker.it_asset_tracker.entity.AssetStatus;
import com.itassettracker.it_asset_tracker.service.AssetService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetService service;
    public AssetController(AssetService service) { this.service = service; }
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<AssetResponse> all(@RequestParam(required = false) AssetStatus status) { return service.all(status); }
    @GetMapping("/my-assets") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<AssetResponse> mine(Authentication auth) { return service.mine(auth.getName()); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @PreAuthorize("hasRole('ADMIN')")
    public AssetResponse create(@Valid @RequestBody AssetRequest request) { return service.create(request); }
    @PatchMapping("/{id}/assign/{userId}") @PreAuthorize("hasRole('ADMIN')")
    public AssetResponse assign(@PathVariable Long id, @PathVariable Long userId) { return service.assign(id, userId); }
    @PatchMapping("/{id}/return") @PreAuthorize("hasRole('ADMIN')")
    public AssetResponse returnAsset(@PathVariable Long id) { return service.returnAsset(id); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
