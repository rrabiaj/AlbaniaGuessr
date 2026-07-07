package com.albaniaguessr.controller;

import com.albaniaguessr.entity.Location;
import com.albaniaguessr.service.store.InMemoryLocationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InMemoryLocationStore locationStore;

    /**
     * List all locations (admin only).
     * GET /api/admin/locations
     */
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationStore.findAll());
    }

    /**
     * Get a specific location.
     * GET /api/admin/locations/{id}
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        return locationStore.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new location (admin only).
     * POST /api/admin/locations
     */
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location saved = locationStore.save(location);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update an existing location (admin only).
     * PUT /api/admin/locations/{id}
     */
    @PutMapping("/locations/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        Optional<Location> existing = locationStore.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        location.setId(id);
        Location saved = locationStore.save(location);
        return ResponseEntity.ok(saved);
    }

    /**
     * Health check.
     * GET /api/admin/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "locations", locationStore.count(),
                "version", "0.0.1-SNAPSHOT"
        ));
    }
}