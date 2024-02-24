package ar.edu.undef.fie.soporte2023.infrastructure;

import ar.edu.undef.fie.soporte2023.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
