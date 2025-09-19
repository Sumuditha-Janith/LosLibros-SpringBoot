package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.PublisherDTO;

import java.util.List;

public interface PublisherService {
    PublisherDTO savePublisher(PublisherDTO publisherDTO);
    List<PublisherDTO> getAllPublishers();
    PublisherDTO getPublisherById(Long id);
    void deletePublisher(Long id);
    PublisherDTO updatePublisher(Long id, PublisherDTO publisherDTO);
}