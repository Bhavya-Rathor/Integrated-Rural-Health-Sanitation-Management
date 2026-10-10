package com.irhsm.waterservice.controller.api;

import com.irhsm.waterservice.entity.WaterSource;
import com.irhsm.waterservice.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water/sources")
@RequiredArgsConstructor
public class SourceApiController {

    private final SourceService sourceService;

    @GetMapping
    public List<WaterSource> getAll(@RequestParam(required = false) String village) {
        return sourceService.findAll(village);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterSource> getOne(@PathVariable Long id) {
        return sourceService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WaterSource> create(@Valid @RequestBody WaterSource source) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sourceService.create(source));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterSource> update(@PathVariable Long id, @Valid @RequestBody WaterSource source) {
        return sourceService.update(id, source).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}