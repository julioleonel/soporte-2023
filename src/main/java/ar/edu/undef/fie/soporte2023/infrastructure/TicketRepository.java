package ar.edu.undef.fie.soporte2023.infrastructure;

import ar.edu.undef.fie.soporte2023.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //Consulta para contar los tickets asignados a un técnico
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTechnician.username = :username AND t.dueDate IS NOT NULL")
    Long contarResueltosPorTecnico(@Param("username") String username);
}
