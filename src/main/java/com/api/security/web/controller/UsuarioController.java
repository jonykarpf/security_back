package com.api.security.web.controller;

import com.api.security.domain.dto.UsuarioDTO;
import com.api.security.domain.service.impl.UsuarioServiceImpl;
import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Rol;
import com.api.security.percistance.entity.Usuario;
import com.api.security.percistance.entity.UsuarioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    // Inyeccion de dependecia de UsuarioService impl
    @Autowired
    private UsuarioServiceImpl usuarioService;

    // Inyeccion de dependecia de BCryptPasswordEncoder
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;




    // -------------------BEGIN. VIEWS HTTP REQUESTS-------------------


    @PostMapping("/save")
    public ResponseEntity<?> saveUsuario(@RequestBody Usuario usuario) throws ResourceNotFoundException {
        ResponseEntity response = new ResponseEntity<>("AUTOINCREMENT ID", HttpStatus.BAD_REQUEST);
        // encriptar la contraseña
        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
        Set<UsuarioRol> usuarioRoles = new HashSet<>();
        UsuarioDTO usuarioDTOResponse = new UsuarioDTO();
        Rol rol = new Rol();
        rol.setRolId(1L);
        rol.setNombre("USER");
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setRol(rol);
        usuarioRol.setUsuario(usuario);
        usuarioRoles.add(usuarioRol);
        usuarioDTOResponse = usuarioService.saveUsuario(usuario, usuarioRoles);

        if (usuarioDTOResponse != null) {
            response = new ResponseEntity<>("USUARIO CREADO CON EXITO", HttpStatus.CREATED);
        }
        return response;


    }


    @GetMapping("/read/{username}")
    public ResponseEntity<?> readUsuario(@PathVariable("username") String username) throws ResourceNotFoundException {
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UsuarioDTO usuarioDTOResponse = usuarioService.getUsuario(username);

        if (usuarioDTOResponse.getId() != null) {
            response = new ResponseEntity<>(usuarioDTOResponse, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        return response;
    }


    @DeleteMapping("/delete/{usuarioId}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("usuarioId") Long usuarioId) throws ResourceNotFoundException {
        ResponseEntity response = new ResponseEntity<>("USUARIO NO EXISTE",HttpStatus.NOT_FOUND);
        Optional<UsuarioDTO> usuarioDTOResponse = usuarioService.deleteUsuario(usuarioId);

        if (usuarioDTOResponse.isPresent()) {
            response = new ResponseEntity<>(usuarioDTOResponse, HttpStatus.OK);
        }
        return response;
    }


    @GetMapping("/readAll")
    public ResponseEntity<?> readAllUsuarios() {
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Stream<UsuarioDTO> usuarioDTOResponse = usuarioService.getAllUsuarios().stream();

        if (usuarioDTOResponse != null) {
            response = new ResponseEntity<>(usuarioDTOResponse, HttpStatus.OK);
        }
        return response;

    }


    // -------------------END. VIEWS HTTP REQUESTS-------------------




}
