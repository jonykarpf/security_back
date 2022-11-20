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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
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
        // encriptar la contraseña
        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
        // asignarle un rol por defecto
        Set<UsuarioRol> usuarioRoles = new HashSet<>();
        UsuarioDTO usuarioDTOResponse = new UsuarioDTO();
        Rol rol = new Rol();
        rol.setNombre("USER");
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setRol(rol);
        usuarioRol.setUsuario(usuario);
        usuarioRoles.add(usuarioRol);
        usuarioDTOResponse = usuarioService.saveUsuario(usuario, usuarioRoles);

        // si el usuario ya existe en la base de datos se lanza una excepcion
        if (usuarioDTOResponse == null) {
            return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
        }
        // si el usuario no existe en la base de datos se guarda en la base de datos
        return new ResponseEntity<>(usuarioRoles, HttpStatus.OK);
    }




    @GetMapping("/read/{username}")
    public ResponseEntity<?> readUsuario(@PathVariable("username") String username) throws ResourceNotFoundException {
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UsuarioDTO usuarioDTOResponse = usuarioService.getUsuario(username);

        // si el usuario existe en la base de datos se retorna
        if (usuarioDTOResponse.getId() != null) {
            response = new ResponseEntity<>(usuarioDTOResponse, HttpStatus.OK);
        }
        // si el usuario no existe en la base de datos se lanza una excepcion
        else {
            response = new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        return response;
    }


    @DeleteMapping("/delete/{usuarioId}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("usuarioId") Long usuarioId) throws ResourceNotFoundException {
        ResponseEntity response = new ResponseEntity<>("USUARIO NO EXISTE",HttpStatus.NOT_FOUND);
        Optional<UsuarioDTO> usuarioDTOResponse = usuarioService.deleteUsuario(usuarioId);

        // si el usuario existe en la base de datos se elimina
        if (usuarioDTOResponse.isPresent()) {
            response = new ResponseEntity<>(usuarioDTOResponse.get(), HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("USUARIO NO EXISTE",HttpStatus.NOT_FOUND);
        }
        return response;
    }


    @GetMapping("/readAll")
    public ResponseEntity<?> readAllUsuarios() throws ResourceNotFoundException {
        AtomicReference<ResponseEntity> response = new AtomicReference<>(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();

        // si hay usuarios en la base de datos se retornan
        usuarios.forEach(usuario -> {
            if (usuario.getId() != null) {
                response.set(new ResponseEntity<>(usuarios, HttpStatus.OK));
            }

        });

        return response.get();

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUsuario(@RequestBody Usuario usuario) throws ResourceNotFoundException {
       // encriptar la contraseña
        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
        UsuarioDTO usuarioDTOResponse = usuarioService.updateUsuario(usuario);
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // si el usuario existe en la base de datos se actualiza
        if (usuarioDTOResponse.getId() != null){
            response = new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        // si el usuario no existe en la base de datos se lanza una excepcion
        else {
            response = new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        return response;

    }


    // -------------------END. VIEWS HTTP REQUESTS-------------------




}
