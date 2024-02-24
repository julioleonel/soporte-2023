package ar.edu.undef.fie.soporte2023.interfaces;

import ar.edu.undef.fie.soporte2023.domain.entities.*;
import ar.edu.undef.fie.soporte2023.infrastructure.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

    public UserController(UserRepository userRepository, TicketRepository ticketRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.notificationRepository = notificationRepository;


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

    @PostMapping("/asignarTicket")
    public ResponseEntity<String> asignarTicket(@RequestParam Long ticketId, @RequestParam String nombreTecnico, @RequestParam Role rol) {
        if (rol != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        User tecnico = userRepository.findByUsername(nombreTecnico);

        if (ticket == null || tecnico == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ticket.setAssignedTechnician(tecnico);
        ticketRepository.save(ticket);

        // Crear una notificación
        Notification notification = new Notification();
        notification.setMessage("Se te ha asignado un nuevo ticket: " + ticket.getId());
        notification.setFecha(LocalDate.now());
        notification.setTicket(ticket);
        notification.setRecipient(tecnico);
        notificationRepository.save(notification);

        return ResponseEntity.ok("Ticket asignado con éxito al técnico " + nombreTecnico);
    }

    @PostMapping("/cerrarTicket")
    public ResponseEntity<String> cerrarTicket(@RequestParam Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ticket.setDueDate(LocalDate.now());
        ticketRepository.save(ticket);

        // Crear una notificación
        Notification notification = new Notification();
        notification.setMessage("Tu ticket " + ticket.getId() + " ha sido cerrado.");
        notification.setFecha(LocalDate.now());
        notification.setTicket(ticket);
        notification.setRecipient(ticket.getCreatedBy());
        notificationRepository.save(notification);

        return ResponseEntity.ok("Ticket " + ticketId + " cerrado con éxito");
    }
}

