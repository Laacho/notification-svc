package app.repository;

import app.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findAllByUserIdAndDeletedFalse(UUID userId);

    List<Notification> findAllByUserId(UUID userId);
}
