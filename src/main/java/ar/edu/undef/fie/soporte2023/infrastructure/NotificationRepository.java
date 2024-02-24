package ar.edu.undef.fie.soporte2023.infrastructure;

import ar.edu.undef.fie.soporte2023.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}