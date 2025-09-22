package lk.ijse.gdse71.loslibros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageRequestDTO {
    private String subject;
    private String message;
    private Long parentMessageId; // For replies
}