package ar.edu.undef.fie.soporte2023.interfaces;

import ar.edu.undef.fie.soporte2023.domain.entities.Priority;
import ar.edu.undef.fie.soporte2023.domain.entities.Role;
import ar.edu.undef.fie.soporte2023.domain.entities.Ticket;
import ar.edu.undef.fie.soporte2023.domain.entities.User;
import ar.edu.undef.fie.soporte2023.infrastructure.TicketRepository;
import ar.edu.undef.fie.soporte2023.infrastructure.UserRepository;
import ar.edu.undef.fie.soporte2023.interfaces.request.TicketRecord;
import ar.edu.undef.fie.soporte2023.interfaces.request.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public UserController(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping("/createUser")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRecord userRecord) {

        User newUser = new User();
        newUser.setGrade(userRecord.grade());
        newUser.setFirstName(userRecord.firstName());
        newUser.setLastName(userRecord.lastName());
        newUser.setUsername(userRecord.username());
        newUser.setPassword(userRecord.password());
        newUser.setRole(Role.valueOf(userRecord.role()));

        userRepository.save(newUser);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Usuario creado exitosamente!.");
        response.put("grade", newUser.getGrade());
        response.put("firstName", newUser.getFirstName());
        response.put("lastName", newUser.getLastName());
        response.put("username", newUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credenciales) {
        String nombreUsuario = credenciales.get("nombreUsuario");
        String contrasena = credenciales.get("contrasena");

        User usuario = userRepository.findByUsername(nombreUsuario);

        Map<String, String> respuesta = new HashMap<>();
        if (usuario != null && usuario.getPassword().equals(contrasena)) {
            respuesta.put("estado", "Inicio de sesión exitoso");
            return ResponseEntity.ok(respuesta);
        } else {

            respuesta.put("estado", "Nombre de usuario o contraseña inválidos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @PostMapping("/crearTicket")
    public ResponseEntity<String> crearTicket(@RequestBody TicketRecord ticketRecord, @RequestParam String nombreUsuario, @RequestParam Role rol) {
        User user = userRepository.findByUsername(nombreUsuario);

        if (user != null && user.getRole() == rol && rol == Role.USER) {
            Ticket nuevoTicket = new Ticket();
            nuevoTicket.setTitle(ticketRecord.title());
            nuevoTicket.setDescription(ticketRecord.description());
            nuevoTicket.setPriority(Priority.valueOf(ticketRecord.priority()));
            nuevoTicket.setCreationDate(ticketRecord.creationDate());
            nuevoTicket.setDueDate(ticketRecord.dueDate());
            nuevoTicket.setCreatedBy(user);

            ticketRepository.save(nuevoTicket);

            String response = "Ticket creado con éxito por el usuario " + nombreUsuario;

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

