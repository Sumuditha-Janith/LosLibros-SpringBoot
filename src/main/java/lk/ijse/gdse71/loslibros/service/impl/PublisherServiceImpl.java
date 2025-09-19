package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.PublisherDTO;
import lk.ijse.gdse71.loslibros.entity.Publisher;
import lk.ijse.gdse71.loslibros.repository.PublisherRepository;
import lk.ijse.gdse71.loslibros.service.PublisherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, ModelMapper modelMapper) {
        this.publisherRepository = publisherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PublisherDTO savePublisher(PublisherDTO publisherDTO) {
        Publisher publisher = modelMapper.map(publisherDTO, Publisher.class);
        Publisher savedPublisher = publisherRepository.save(publisher);
        PublisherDTO dto = modelMapper.map(savedPublisher, PublisherDTO.class);
        // Set book count
        dto.setBookCount(savedPublisher.getBooks() != null ? savedPublisher.getBooks().size() : 0);
        return dto;
    }

    @Override
    public List<PublisherDTO> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisher -> {
                    PublisherDTO dto = modelMapper.map(publisher, PublisherDTO.class);
                    // Set book count
                    dto.setBookCount(publisher.getBooks() != null ? publisher.getBooks().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PublisherDTO getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with id: " + id));
        PublisherDTO dto = modelMapper.map(publisher, PublisherDTO.class);
        // Set book count
        dto.setBookCount(publisher.getBooks() != null ? publisher.getBooks().size() : 0);
        return dto;
    }

    @Override
    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new EntityNotFoundException("Publisher not found with id: " + id);
        }
        publisherRepository.deleteById(id);
    }

    @Override
    public PublisherDTO updatePublisher(Long id, PublisherDTO publisherDTO) {
        Publisher existingPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with id: " + id));

        existingPublisher.setPublisherName(publisherDTO.getPublisherName());

        Publisher updatedPublisher = publisherRepository.save(existingPublisher);
        PublisherDTO dto = modelMapper.map(updatedPublisher, PublisherDTO.class);
        // Set book count
        dto.setBookCount(updatedPublisher.getBooks() != null ? updatedPublisher.getBooks().size() : 0);
        return dto;
    }
}