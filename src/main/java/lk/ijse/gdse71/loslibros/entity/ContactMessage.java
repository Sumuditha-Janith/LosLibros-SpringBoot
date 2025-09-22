package lk.ijse.gdse71.loslibros.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "contact_messages")
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff; // Staff who replied (nullable for initial messages)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime repliedAt;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(columnDefinition = "TEXT")
    private String replyMessage;

    // For tracking conversation thread
    private Long parentMessageId; // If this is a reply, points to original message
}