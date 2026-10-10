package com.irhsm.waterservice.controller.api;

import com.irhsm.waterservice.dto.WaterTestRequest;
import com.irhsm.waterservice.entity.WaterQualityTest;
import com.irhsm.waterservice.service.WaterTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water/tests")
@RequiredArgsConstructor
public class WaterTestApiController {

    private final WaterTestService testService;

    @GetMapping
    public List<WaterQualityTest> getAll(@RequestParam(required = false) Long sourceId) {
        return testService.findAll(sourceId);
    }

    @PostMapping
    public ResponseEntity<WaterQualityTest> create(@Valid @RequestBody WaterTestRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.create(req));
    }
}