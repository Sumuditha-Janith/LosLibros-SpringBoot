package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.ContactMessageDTO;
import lk.ijse.gdse71.loslibros.dto.CreateMessageRequestDTO;
import lk.ijse.gdse71.loslibros.dto.MessageThreadDTO;

import java.util.List;

public interface ContactService {
    ContactMessageDTO createMessage(CreateMessageRequestDTO request, String username);
    ContactMessageDTO createReply(CreateMessageRequestDTO request, String staffUsername, Long parentMessageId);
    List<ContactMessageDTO> getUserMessages(String username);
    List<ContactMessageDTO> getPendingMessages();
    List<ContactMessageDTO> getAllMessagesForStaff();
    MessageThreadDTO getMessageThread(Long messageId, String username);
    MessageThreadDTO getMessageThreadForStaff(Long messageId);
    ContactMessageDTO getMessageById(Long id, String username);
    void deleteMessage(Long id, String username);
    Long getPendingMessagesCount();

    ContactMessageDTO closeMessageThread(Long messageId, String staffUsername);

}