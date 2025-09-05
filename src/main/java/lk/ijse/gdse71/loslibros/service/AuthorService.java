package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO saveAuthor(AuthorDTO authorDTO);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO getAuthorById(Long id);
    void deleteAuthor(Long id);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
}