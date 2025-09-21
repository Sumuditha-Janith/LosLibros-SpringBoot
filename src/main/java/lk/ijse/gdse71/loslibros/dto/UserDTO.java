package lk.ijse.gdse71.loslibros.dto;

import lk.ijse.gdse71.loslibros.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String address;
    private String email;
    private Role role;
}