package lk.ijse.gdse71.loslibros.dto;

import lk.ijse.gdse71.loslibros.entity.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageDTO {
    private Long id;
    private String subject;
    private String message;
    private Long userId;
    private String userEmail;
    private String userName;
    private Long staffId;
    private String staffName;
    private LocalDateTime createdAt;
    private LocalDateTime repliedAt;
    private MessageStatus status;
    private String replyMessage;
    private Long parentMessageId;
    private boolean canEdit;
}