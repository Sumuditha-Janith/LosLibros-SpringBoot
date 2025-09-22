package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.ApiResponse;
import lk.ijse.gdse71.loslibros.dto.ContactMessageDTO;
import lk.ijse.gdse71.loslibros.dto.CreateMessageRequestDTO;
import lk.ijse.gdse71.loslibros.dto.MessageThreadDTO;
import lk.ijse.gdse71.loslibros.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
@CrossOrigin
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/messages")
    public ResponseEntity<ApiResponse> createMessage(
            @RequestBody CreateMessageRequestDTO request,
            Authentication authentication) {
        try {
            ContactMessageDTO message = contactService.createMessage(request, authentication.getName());
            return ResponseEntity.ok(new ApiResponse(200, "Message sent successfully", message));
        } catch (RuntimeException e) {
            System.err.println("Error creating message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error: " + e.getMessage(), null));
        } catch (Exception e) {
            System.err.println("Unexpected error creating message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(500, "Internal server error", null));
        }
    }

    @PostMapping("/messages/{parentMessageId}/reply")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> createReply(
            @PathVariable Long parentMessageId,
            @RequestBody CreateMessageRequestDTO request,
            Authentication authentication) {
        try {
            ContactMessageDTO reply = contactService.createReply(request, authentication.getName(), parentMessageId);
            return ResponseEntity.ok(new ApiResponse(200, "Reply sent successfully", reply));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @GetMapping("/messages/my-messages")
    public ResponseEntity<ApiResponse> getUserMessages(Authentication authentication) {
        try {
            System.out.println("Fetching messages for user: " + authentication.getName());

            List<ContactMessageDTO> messages = contactService.getUserMessages(authentication.getName());

            System.out.println("Found " + messages.size() + " messages for user");
            return ResponseEntity.ok(new ApiResponse(200, "Messages retrieved successfully", messages));
        } catch (RuntimeException e) {
            System.err.println("Error fetching user messages: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @GetMapping("/messages/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getPendingMessages() {
        try {
            List<ContactMessageDTO> messages = contactService.getPendingMessages();
            return ResponseEntity.ok(new ApiResponse(200, "Pending messages retrieved", messages));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @GetMapping("/messages")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getAllMessages() {
        try {
            List<ContactMessageDTO> messages = contactService.getAllMessagesForStaff();
            return ResponseEntity.ok(new ApiResponse(200, "All messages retrieved", messages));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @GetMapping("/messages/{messageId}/thread")
    public ResponseEntity<ApiResponse> getMessageThread(
            @PathVariable Long messageId,
            Authentication authentication) {
        try {
            MessageThreadDTO thread = contactService.getMessageThread(messageId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse(200, "Thread retrieved successfully", thread));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }
    @GetMapping("/messages/{messageId}/thread/staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getMessageThreadForStaff(@PathVariable Long messageId) {
        try {
            MessageThreadDTO thread = contactService.getMessageThreadForStaff(messageId);
            return ResponseEntity.ok(new ApiResponse(200, "Thread retrieved successfully", thread));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @GetMapping("/messages/pending/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getPendingMessagesCount() {
        try {
            Long count = contactService.getPendingMessagesCount();
            return ResponseEntity.ok(new ApiResponse(200, "Pending count retrieved", count));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessage(
            @PathVariable Long messageId,
            Authentication authentication) {
        try {
            contactService.deleteMessage(messageId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse(200, "Message deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @PatchMapping("/messages/{messageId}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> closeMessageThread(@PathVariable Long messageId, Authentication authentication) {
        try {
            ContactMessageDTO closedMessage = contactService.closeMessageThread(messageId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse(200, "Thread closed successfully", closedMessage));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
        }
    }
}