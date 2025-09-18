package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.AuthorDTO;
import lk.ijse.gdse71.loslibros.entity.Author;
import lk.ijse.gdse71.loslibros.repository.AuthorRepository;
import lk.ijse.gdse71.loslibros.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        Author author = modelMapper.map(authorDTO, Author.class);
        Author savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, AuthorDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> {
                    AuthorDTO dto = modelMapper.map(author, AuthorDTO.class);
                    // Set book count
                    dto.setBookCount(author.getBooks() != null ? author.getBooks().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        AuthorDTO dto = modelMapper.map(author, AuthorDTO.class);
        // Set book count
        dto.setBookCount(author.getBooks() != null ? author.getBooks().size() : 0);
        return dto;
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));

        existingAuthor.setAuthorName(authorDTO.getAuthorName());
        existingAuthor.setAuthorDescription(authorDTO.getAuthorDescription());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        AuthorDTO dto = modelMapper.map(updatedAuthor, AuthorDTO.class);
        // Set book count
        dto.setBookCount(updatedAuthor.getBooks() != null ? updatedAuthor.getBooks().size() : 0);
        return dto;
    }
}