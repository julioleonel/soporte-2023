package ar.edu.undef.fie.soporte2023.interfaces;

import ar.edu.undef.fie.soporte2023.infrastructure.TicketRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
public class EstadisticasController {

    private final TicketRepository ticketRepository;

    public EstadisticasController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/totalTickets")
    public String getTotalTickets() {
        long totalTickets = ticketRepository.count();
        return "Cantidad de tiket creados: " + totalTickets;
    }

    @GetMapping("/ticketsResueltosPorTecnico")
    public String ticketsResueltosPorTecnico(@RequestParam String username) {
        Long cantidad = ticketRepository.contarResueltosPorTecnico(username);
        return "Tickets cerrados por el técnico " + username + ": " + cantidad;
    }
}

