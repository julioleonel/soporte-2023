package ar.edu.undef.fie.soporte2023.interfaces.request;

import java.time.LocalDate;

public record UserRecord(String grade, String firstName, String lastName, String username, String password, String role) {
}