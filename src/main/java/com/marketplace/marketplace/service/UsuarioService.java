package com.marketplace.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.model.Rol;
import com.marketplace.marketplace.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean registrar(UsuarioDTO dto) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(dto.correo);
        if (existente.isPresent()) {
            return false;
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(dto.usuario);
        nuevo.setCorreo(dto.correo);
        nuevo.setContrasena(dto.contrasena);

        // Asignar rol según dominio del correo
        if (dto.correo.toLowerCase().endsWith(".edu.pe")) {
            nuevo.setRol(Rol.ESTUDIANTE);
        } else {
            nuevo.setRol(Rol.COMPRADOR);
        }

        usuarioRepository.save(nuevo);
        return true;
    }
}
