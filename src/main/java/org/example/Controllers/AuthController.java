package org.example.Controllers;

import lombok.AllArgsConstructor;
import org.example.Util.User;
import org.example.Util.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Usuario requerido y contraseña requrida"));
        }

        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.status(409)
                    .body(Map.of("message", "El usuario ya existe"));
        }

        userRepo.save(new User(username, encoder.encode(password), "ROLE_USER"));

        return ResponseEntity.status(201)
                .body(Map.of("message", "Usuario registrado exitosamente"));
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        return ResponseEntity.ok(Map.of(
                "username", auth.getName(),
                "mensaje", "Autenticado correctamente via HTTPS + Basic Auth"
        ));
    }
}