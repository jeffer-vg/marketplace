package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.UsuarioRepository;
import com.marketplace.marketplace.service.UsuarioDTO;
import com.marketplace.marketplace.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // IMPORT CORRECTO
import org.springframework.http.HttpStatus; // IMPORT CORRECTO
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Usuario datos) {
    if (datos == null || datos.getCorreo() == null || datos.getContrasena() == null) {
        return ResponseEntity.badRequest().body("Datos incompletos");
    }

    Optional<Usuario> usuario = usuarioRepo.findByCorreo(datos.getCorreo());

    if (!usuario.isPresent()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo no registrado");
    }

    if (!usuario.get().getContrasena().equals(datos.getContrasena())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
    }

    Usuario user = usuario.get();
    Map<String, Object> response = new HashMap<>();
    response.put("mensaje", "Login exitoso");
    response.put("id", user.getId());
    response.put("nombre", user.getNombre());
    response.put("correo", user.getCorreo());
    response.put("rol", user.getRol());

    return ResponseEntity.ok(response);
}
@Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody UsuarioDTO dto) {
        boolean registrado = usuarioService.registrar(dto);
        if (registrado) {
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } else {
            return ResponseEntity.status(400).body("El correo ya está registrado");
        }
    }
    
}
