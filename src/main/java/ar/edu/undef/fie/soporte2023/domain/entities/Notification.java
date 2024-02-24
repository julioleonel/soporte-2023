package ar.edu.undef.fie.soporte2023.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDate fecha;
    @ManyToOne
    private Ticket ticket;
    @ManyToOne
    private User recipient;

    // getters and setters
}