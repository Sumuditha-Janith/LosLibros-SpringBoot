package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.PublisherSaleDTO;
import lk.ijse.gdse71.loslibros.service.PublisherSaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin
public class PublisherSaleController {

    private final PublisherSaleService publisherSaleService;

    public PublisherSaleController(PublisherSaleService publisherSaleService) {
        this.publisherSaleService = publisherSaleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherSaleDTO> createSale(@RequestBody PublisherSaleDTO saleDTO) {
        return ResponseEntity.ok(publisherSaleService.createSale(saleDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PublisherSaleDTO>> getAllSales() {
        return ResponseEntity.ok(publisherSaleService.getAllSales());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherSaleDTO> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherSaleService.getSaleById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherSaleDTO> updateSale(@PathVariable Long id, @RequestBody PublisherSaleDTO saleDTO) {
        return ResponseEntity.ok(publisherSaleService.updateSale(id, saleDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        publisherSaleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<PublisherSaleDTO>> getActiveSales() {
        return ResponseEntity.ok(publisherSaleService.getActiveSales());
    }
}