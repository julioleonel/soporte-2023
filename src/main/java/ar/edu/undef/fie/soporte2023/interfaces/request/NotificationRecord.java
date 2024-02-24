package ar.edu.undef.fie.soporte2023.interfaces.request;

import java.time.LocalDate;

public record NotificationRecord(String message, LocalDate fecha, Long ticketId, Long recipientId) {
}