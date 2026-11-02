package com.itassettracker.it_asset_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itassettracker.it_asset_tracker.dto.AssetRequest;
import com.itassettracker.it_asset_tracker.dto.AssetResponse;
import com.itassettracker.it_asset_tracker.entity.Asset;
import com.itassettracker.it_asset_tracker.entity.AssetStatus;
import com.itassettracker.it_asset_tracker.entity.User;
import com.itassettracker.it_asset_tracker.exception.ResourceNotFoundException;
import com.itassettracker.it_asset_tracker.repository.AssetRepository;
import com.itassettracker.it_asset_tracker.repository.UserRepository;

@Service
public class AssetService {
    private final AssetRepository assets;
    private final CategoryService categories;
    private final AuthService auth;
    private final UserRepository users;
    public AssetService(AssetRepository assets, CategoryService categories, AuthService auth, UserRepository users) {
        this.assets = assets; this.categories = categories; this.auth = auth; this.users = users;
    }
    private Asset get(Long id) { return assets.findById(id).orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + id)); }
    private AssetResponse dto(Asset a) {
        return new AssetResponse(a.getId(), a.getName(), a.getSerialNumber(), a.getStatus(),
            a.getCategory().getId(), a.getCategory().getName(),
            a.getAssignedTo() == null ? null : a.getAssignedTo().getId(),
            a.getAssignedTo() == null ? null : a.getAssignedTo().getUsername(), a.getCreatedAt());
    }
    public List<AssetResponse> all(AssetStatus status) {
        List<Asset> list = status == null ? assets.findAll() : assets.findByStatus(status);
        return list.stream().map(this::dto).toList();
    }
    public List<AssetResponse> mine(String username) {
        User user = users.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return assets.findByAssignedTo_Id(user.getId()).stream().map(this::dto).toList();
    }
    public AssetResponse create(AssetRequest request) {
        if (assets.existsBySerialNumber(request.getSerialNumber().trim())) throw new IllegalArgumentException("Serial number already exists.");
        Asset a = new Asset();
        a.setName(request.getName().trim()); a.setSerialNumber(request.getSerialNumber().trim());
        a.setCategory(categories.get(request.getCategoryId())); a.setStatus(AssetStatus.AVAILABLE);
        return dto(assets.save(a));
    }
    @Transactional
    public AssetResponse assign(Long assetId, Long userId) {
        Asset a = get(assetId);
        if (a.getStatus() != AssetStatus.AVAILABLE) throw new IllegalArgumentException("Only AVAILABLE assets can be assigned.");
        a.setAssignedTo(auth.getUser(userId)); a.setStatus(AssetStatus.ASSIGNED);
        return dto(assets.save(a));
    }
    @Transactional
    public AssetResponse returnAsset(Long assetId) {
        Asset a = get(assetId);
        if (a.getStatus() != AssetStatus.ASSIGNED) throw new IllegalArgumentException("Only ASSIGNED assets can be returned.");
        a.setAssignedTo(null); a.setStatus(AssetStatus.AVAILABLE);
        return dto(assets.save(a));
    }
      @Transactional
public void delete(Long assetId) {
    Asset asset = get(assetId);

    if (asset.getStatus() == AssetStatus.ASSIGNED) {
        throw new IllegalArgumentException(
            "Cannot delete an assigned asset. Return it first."
        );
    }

    assets.delete(asset);
}
}
