package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.ContactMessageDTO;
import lk.ijse.gdse71.loslibros.dto.CreateMessageRequestDTO;
import lk.ijse.gdse71.loslibros.dto.MessageThreadDTO;
import lk.ijse.gdse71.loslibros.entity.ContactMessage;
import lk.ijse.gdse71.loslibros.entity.MessageStatus;
import lk.ijse.gdse71.loslibros.entity.User;
import lk.ijse.gdse71.loslibros.repository.ContactMessageRepository;
import lk.ijse.gdse71.loslibros.repository.UserRepository;
import lk.ijse.gdse71.loslibros.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ContactMessageDTO createMessage(CreateMessageRequestDTO request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ContactMessage message = ContactMessage.builder()
                .subject(request.getSubject())
                .message(request.getMessage())
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(MessageStatus.PENDING)
                .parentMessageId(request.getParentMessageId())
                .build();

        ContactMessage savedMessage = contactMessageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    @Override
    public ContactMessageDTO createReply(CreateMessageRequestDTO request, String staffUsername, Long parentMessageId) {
        User staff = userRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new RuntimeException("Staff member not found"));

        ContactMessage parentMessage = contactMessageRepository.findById(parentMessageId)
                .orElseThrow(() -> new RuntimeException("Parent message not found"));

        // Create reply message
        ContactMessage reply = ContactMessage.builder()
                .subject("Re: " + parentMessage.getSubject())
                .message(request.getMessage())
                .user(parentMessage.getUser()) // Original user
                .staff(staff)
                .createdAt(LocalDateTime.now())
                .repliedAt(LocalDateTime.now())
                .status(MessageStatus.RESPONDED)
                .parentMessageId(parentMessageId)
                .replyMessage(request.getMessage())
                .build();

        // Update parent message status
        parentMessage.setStatus(MessageStatus.RESPONDED);
        parentMessage.setReplyMessage(request.getMessage());
        parentMessage.setRepliedAt(LocalDateTime.now());
        parentMessage.setStaff(staff);

        contactMessageRepository.save(parentMessage);
        ContactMessage savedReply = contactMessageRepository.save(reply);

        return convertToDTO(savedReply);
    }

    @Override
    public List<ContactMessageDTO> getUserMessages(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ContactMessage> messages = contactMessageRepository.findByUserOrderByCreatedAtDesc(user);

        return messages.stream()
                .map(this::convertToDTOWithPermissions)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactMessageDTO> getPendingMessages() {
        return contactMessageRepository.findByStatusOrderByCreatedAtDesc(MessageStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactMessageDTO> getAllMessagesForStaff() {
        return contactMessageRepository.findAllParentMessages()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MessageThreadDTO getMessageThread(Long messageId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ContactMessage parentMessage = contactMessageRepository.findByIdAndUser(messageId, user)
                .orElseThrow(() -> new RuntimeException("Message not found or access denied"));

        List<ContactMessage> replies = contactMessageRepository.findByParentMessageIdOrderByCreatedAtAsc(messageId);

        MessageThreadDTO threadDTO = new MessageThreadDTO();
        threadDTO.setOriginalMessage(convertToDTOWithPermissions(parentMessage));
        threadDTO.setReplies(replies.stream()
                .map(this::convertToDTOWithPermissions)
                .collect(Collectors.toList()));

        return threadDTO;
    }

    @Override
    public MessageThreadDTO getMessageThreadForStaff(Long messageId) {
        ContactMessage parentMessage = contactMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        List<ContactMessage> replies = contactMessageRepository.findByParentMessageIdOrderByCreatedAtAsc(messageId);

        MessageThreadDTO threadDTO = new MessageThreadDTO();
        threadDTO.setOriginalMessage(convertToDTO(parentMessage));
        threadDTO.setReplies(replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));

        return threadDTO;
    }

    @Override
    public ContactMessageDTO getMessageById(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ContactMessage message = contactMessageRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Message not found or access denied"));

        return convertToDTOWithPermissions(message);
    }

    @Override
    public void deleteMessage(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ContactMessage message = contactMessageRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Message not found or access denied"));

        // Users can only delete their own pending messages
        if (message.getStatus() == MessageStatus.PENDING && message.getStaff() == null) {
            contactMessageRepository.delete(message);
        } else {
            throw new RuntimeException("Cannot delete messages that have been responded to");
        }
    }

    @Override
    public Long getPendingMessagesCount() {
        return contactMessageRepository.countByStatus(MessageStatus.PENDING);
    }

    private ContactMessageDTO convertToDTO(ContactMessage message) {
        ContactMessageDTO dto = modelMapper.map(message, ContactMessageDTO.class);

        if (message.getUser() != null) {
            dto.setUserId(message.getUser().getId());
            dto.setUserEmail(message.getUser().getEmail());
            dto.setUserName(message.getUser().getUsername());
        }

        if (message.getStaff() != null) {
            dto.setStaffId(message.getStaff().getId());
            dto.setStaffName(message.getStaff().getUsername());
        }

        return dto;
    }

    private ContactMessageDTO convertToDTOWithPermissions(ContactMessage message) {
        ContactMessageDTO dto = convertToDTO(message);
        // Users can only edit their own pending messages (no staff response yet)
        dto.setCanEdit(message.getStaff() == null && message.getStatus() == MessageStatus.PENDING);
        return dto;
    }

    @Override
    public ContactMessageDTO closeMessageThread(Long messageId, String staffUsername) {
        User staff = userRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new RuntimeException("Staff member not found"));

        ContactMessage message = contactMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Update the message status to CLOSED
        message.setStatus(MessageStatus.CLOSED);
        message.setStaff(staff);
        message.setRepliedAt(LocalDateTime.now());

        ContactMessage savedMessage = contactMessageRepository.save(message);
        return convertToDTO(savedMessage);
    }


}