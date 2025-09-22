package lk.ijse.gdse71.loslibros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageThreadDTO {
    private ContactMessageDTO originalMessage;
    private List<ContactMessageDTO> replies;
}