package ar.edu.undef.fie.soporte2023.infrastructure;

import ar.edu.undef.fie.soporte2023.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}