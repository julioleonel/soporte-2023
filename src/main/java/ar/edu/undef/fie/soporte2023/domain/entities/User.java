package ar.edu.undef.fie.soporte2023.domain.entities;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;

    private String firstName;
    private String lastName;
    private String username;    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    // getters and setters
}