package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO updateUserRole(Long id, String role);
}