package ar.edu.undef.fie.soporte2023.interfaces.request;

import java.time.LocalDate;
import java.util.List;

public record TicketRecord(String description, String priority, LocalDate creationDate, LocalDate dueDate, Long assignedTechnicianId, Long createdById, List<Long> notificationIds) {
}