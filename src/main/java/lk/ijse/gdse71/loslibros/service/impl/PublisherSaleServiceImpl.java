package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.PublisherSaleDTO;
import lk.ijse.gdse71.loslibros.entity.Publisher;
import lk.ijse.gdse71.loslibros.entity.PublisherSale;
import lk.ijse.gdse71.loslibros.repository.PublisherRepository;
import lk.ijse.gdse71.loslibros.repository.PublisherSaleRepository;
import lk.ijse.gdse71.loslibros.service.PublisherSaleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherSaleServiceImpl implements PublisherSaleService {

    private final PublisherSaleRepository publisherSaleRepository;
    private final PublisherRepository publisherRepository;
    private final ModelMapper modelMapper;

    public PublisherSaleServiceImpl(PublisherSaleRepository publisherSaleRepository,
                                    PublisherRepository publisherRepository,
                                    ModelMapper modelMapper) {
        this.publisherSaleRepository = publisherSaleRepository;
        this.publisherRepository = publisherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PublisherSaleDTO createSale(PublisherSaleDTO saleDTO) {
        Publisher publisher = publisherRepository.findById(saleDTO.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + saleDTO.getPublisherId()));

        PublisherSale sale = modelMapper.map(saleDTO, PublisherSale.class);
        sale.setPublisher(publisher);
        sale.setIsActive(true); //this line sets default active status

        PublisherSale savedSale = publisherSaleRepository.save(sale);
        return convertToDTO(savedSale);
    }

    @Override
    public List<PublisherSaleDTO> getAllSales() {
        return publisherSaleRepository.findAllOrderedByDate().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherSaleDTO getSaleById(Long id) {
        PublisherSale sale = publisherSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
        return convertToDTO(sale);
    }

    @Override
    public PublisherSaleDTO updateSale(Long id, PublisherSaleDTO saleDTO) {
        PublisherSale existingSale = publisherSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));

        Publisher publisher = publisherRepository.findById(saleDTO.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + saleDTO.getPublisherId()));

        existingSale.setPublisher(publisher);
        existingSale.setSaleDescription(saleDTO.getSaleDescription());
        existingSale.setDiscountPercentage(saleDTO.getDiscountPercentage());
        existingSale.setStartDate(saleDTO.getStartDate());
        existingSale.setEndDate(saleDTO.getEndDate());
        existingSale.setIsActive(saleDTO.getIsActive());

        PublisherSale updatedSale = publisherSaleRepository.save(existingSale);
        return convertToDTO(updatedSale);
    }

    @Override
    public void deleteSale(Long id) {
        if (!publisherSaleRepository.existsById(id)) {
            throw new RuntimeException("Sale not found with id: " + id);
        }
        publisherSaleRepository.deleteById(id);
    }

    @Override
    public List<PublisherSaleDTO> getActiveSales() {
        return publisherSaleRepository.findAllActiveSales(new Date()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PublisherSaleDTO convertToDTO(PublisherSale sale) {
        PublisherSaleDTO dto = modelMapper.map(sale, PublisherSaleDTO.class);
        dto.setPublisherId(sale.getPublisher().getPublisherId());
        dto.setPublisherName(sale.getPublisher().getPublisherName());
        return dto;
    }
}