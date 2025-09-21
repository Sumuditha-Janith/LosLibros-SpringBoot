package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.PublisherSaleDTO;

import java.util.List;

public interface PublisherSaleService {
    PublisherSaleDTO createSale(PublisherSaleDTO saleDTO);
    List<PublisherSaleDTO> getAllSales();
    PublisherSaleDTO getSaleById(Long id);
    PublisherSaleDTO updateSale(Long id, PublisherSaleDTO saleDTO);
    void deleteSale(Long id);
    List<PublisherSaleDTO> getActiveSales();
}