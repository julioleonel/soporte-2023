package ar.edu.undef.fie.soporte2023.domain.entities;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private LocalDate creationDate;
    private LocalDate dueDate;
    @ManyToOne
    private User assignedTechnician;
    @ManyToOne
    private User createdBy;
    @OneToMany(mappedBy = "ticket")
    private List<Notification> notifications;

    // getters and setters
}