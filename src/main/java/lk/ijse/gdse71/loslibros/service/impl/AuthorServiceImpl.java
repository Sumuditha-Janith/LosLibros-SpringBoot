package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.AuthorDTO;
import lk.ijse.gdse71.loslibros.entity.Author;
import lk.ijse.gdse71.loslibros.repository.AuthorRepository;
import lk.ijse.gdse71.loslibros.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException; // Using a more specific exception
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // 2. Added for transaction management
public class AuthorServiceImpl implements AuthorService {

    // 1. Switched to final fields for constructor injection
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    // 1. Using Constructor Injection instead of @Autowired on fields
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
    @Transactional(readOnly = true) // Optimization for read operations
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> modelMapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true) // Optimization for read operations
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                // 3. Using a more specific exception for better error handling
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return modelMapper.map(author, AuthorDTO.class);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            // 3. Using a more specific exception
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                // 3. Using a more specific exception
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));

        existingAuthor.setAuthorName(authorDTO.getAuthorName());
        existingAuthor.setAuthorDescription(authorDTO.getAuthorDescription());

        // The save is technically not required if @Transactional is present,
        // as changes to a managed entity will be flushed automatically.
        // However, it's good practice to keep it for clarity.
        Author updatedAuthor = authorRepository.save(existingAuthor);
        return modelMapper.map(updatedAuthor, AuthorDTO.class);
    }
}