package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.ContactMessage;
import lk.ijse.gdse71.loslibros.entity.MessageStatus;
import lk.ijse.gdse71.loslibros.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByStatusOrderByCreatedAtDesc(MessageStatus status);

    @Query("SELECT cm FROM ContactMessage cm WHERE cm.parentMessageId IS NULL ORDER BY cm.createdAt DESC")
    List<ContactMessage> findAllParentMessages();

    List<ContactMessage> findByParentMessageIdOrderByCreatedAtAsc(Long parentMessageId);

    @Query("SELECT cm FROM ContactMessage cm WHERE cm.user = :user AND cm.parentMessageId IS NULL ORDER BY cm.createdAt DESC")
    List<ContactMessage> findParentMessagesByUser(@Param("user") User user);

    @Query("SELECT cm FROM ContactMessage cm WHERE cm.user = :user ORDER BY cm.createdAt DESC")
    List<ContactMessage> findByUserOrderByCreatedAtDesc(@Param("user") User user);

    Optional<ContactMessage> findByIdAndUser(Long id, User user);

    Long countByStatus(MessageStatus status);


}