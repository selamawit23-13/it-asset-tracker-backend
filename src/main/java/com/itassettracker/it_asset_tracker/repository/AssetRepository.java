package com.itassettracker.it_asset_tracker.repository;
import com.itassettracker.it_asset_tracker.entity.Asset;
import com.itassettracker.it_asset_tracker.entity.AssetStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AssetRepository extends JpaRepository<Asset, Long> {
    boolean existsBySerialNumber(String serialNumber);
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByAssignedTo_Id(Long userId);
}
