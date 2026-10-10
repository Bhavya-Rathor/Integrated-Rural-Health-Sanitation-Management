package com.irhsn.medicine.controller.api;

import com.irhsn.medicine.entity.Stock;
import com.irhsn.medicine.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for stock batches. Same /api/medicines prefix,
// so the gateway's /api/medicines/** route already covers these.
@RestController
@RequestMapping("/api/medicines")
public class StockApiController {

    private final StockService stockService;

    public StockApiController(StockService stockService) {
        this.stockService = stockService;
    }

    // POST /api/medicines/1/stock
    // Body: { "centreId": 1, "batchNo": "B-101", "quantity": 300, "expiryDate": "2027-03-31" }
    // "Medicine 1 just arrived at centre 1: here's the batch."
    // @Valid checks centreId, batchNo, quantity, expiryDate are filled in properly.
    @PostMapping("/{medicineId}/stock")
    @ResponseStatus(HttpStatus.CREATED)
    public Stock receive(@PathVariable Long medicineId, @Valid @RequestBody Stock batch) {
        return stockService.receiveStock(medicineId, batch);
    }

    // GET /api/medicines/stock            -> every batch
    // GET /api/medicines/stock?centreId=1 -> only centre 1's batches
    @GetMapping("/stock")
    public List<Stock> list(@RequestParam(required = false) Long centreId) {
        return stockService.findByCentre(centreId);
    }

    // GET /api/medicines/stock/expiring?days=30
    // Batches that expire within 30 days and still have units left.
    // defaultValue = "30" -> if ?days is missing, use 30.
    @GetMapping("/stock/expiring")
    public List<Stock> expiring(@RequestParam(defaultValue = "30") int days) {
        return stockService.expiringWithin(days);
    }
}