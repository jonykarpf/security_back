package com.api.security.domain.service.impl;

import com.api.security.domain.dto.UsuarioDTO;
import com.api.security.domain.service.UsuarioService;
import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Rol;
import com.api.security.percistance.entity.Usuario;
import com.api.security.percistance.entity.UsuarioRol;
import com.api.security.percistance.repository.RolRepository;
import com.api.security.percistance.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(UsuarioServiceImpl.class);

    // Inyeccion de dependencia de usuario repository
    @Autowired
    private UsuarioRepository usuarioRepository;


    // Inyeccion de dependencia de usuario rol repository
    @Autowired
    private RolRepository rolRepository;





    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------


    @Override
    public List<Usuario> findAllUsuarios() {
        LOGGER.info("OBTENIENDO TODOS LOS USUARIOS - METODO: UsuarioServiceImpl.findAllUsuarios()");
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findUsuarioById(Long id) {
        LOGGER.info("OBTENIENDO USUARIO POR ID - METODO: UsuarioServiceImpl.findUsuarioById()");
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return usuario;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }
    }

    @Override
    public Optional<Usuario> findUsuarioByNickname(String username) {
        LOGGER.info("OBTENIENDO USUARIO POR NICKNAME - METODO: UsuarioServiceImpl.findUsuarioByNickname()");
        Optional<Usuario> usuario = usuarioRepository.findByNickname(username);
        if (usuario.isPresent()) {
            return Optional.of(usuario.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario con el nickname: " + username + " no existe");
        }
    }

    @Override
    public Optional<Usuario> findUsuarioByEmail(String email)  {
        LOGGER.info("OBTENIENDO USUARIO POR EMAIL - METODO: UsuarioServiceImpl.findUsuarioByEmail()");
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent()) {
            return Optional.of(usuario.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario con el email: " + email + " no existe");
        }
    }

    @Override
    public ResponseEntity<Usuario> saveUsuario(Usuario usuario) {
           LOGGER.info("GUARDANDO USUARIO - METODO: UsuarioServiceImpl.saveUsuario()");
        // Check id for new user, it is recommended to be autogenerated
        if(usuario.getId() != null){
            Optional<Usuario> result = this.usuarioRepository.findById(usuario.getId());
            if(result.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El usuario con el id: " + usuario.getId() + " ya existe");
        }
        // validaciones de campos
        if((usuario.getEmail() != null) && (usuario.getUsername() != null) &&
                (usuario.getPassword() != null) && (usuario.getUsuarioRoles() != null)
        ){
            // validacion de email
            Optional<Usuario> result = this.usuarioRepository.findByEmail(usuario.getEmail());
            if(result.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El usuario con el email: " + usuario.getEmail() + " ya existe");
            // validacion de nickname
            result = this.usuarioRepository.findByNickname(usuario.getUsername());
            if(result.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El usuario con el nickname: " + usuario.getUsername() + " ya existe");
            // encriptacion de password
            usuario.setPassword(this.convertToSHA256(usuario.getPassword()));
            // guardado de usuario en base de datos
            return new ResponseEntity<>(this.usuarioRepository.save(usuario), HttpStatus.CREATED);

        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "validacion de campos incorrecta");
    }


    @Override
    public ResponseEntity<?> updateUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteUsuario(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void insertarUsuarios() {

    }

    public String convertToSHA256(String password){
        MessageDigest md;
        try{
            md = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        StringBuffer sb = new StringBuffer();
        byte[] hash = md.digest(password.getBytes());
        for(byte b: hash)
            sb.append( String.format("%02x", b) );
        return sb.toString();
    }




    //----------------------END. IMPLEMENTED METHODS---------------------------



    // metodo para insertar un usuario en la base de datos al desplegar la aplicacion con rol de administrador
//    @Override
//    public void insertarUsuarios() {
//
//        Usuario usuario = new Usuario();
//        usuario.setUsername("admin");
//        usuario.setPassword(bCryptPasswordEncoder.encode("12345"));
//        usuario.setEmail("jony@hotmail.com");
//        usuario.setEnabled(true);
//
//
//        Rol rol = new Rol();
//        rol.setIdRol(1L);
//        rol.setNombre("ADMIN");
//        rol.setDescripcion("Administrador de la aplicacion");
//
//        Set<UsuarioRol> usuarioRoles = new HashSet<>();
//        UsuarioRol usuarioRol = new UsuarioRol();
//        usuarioRol.setRol(rol);
//        usuarioRol.setUsuario(usuario);
//        usuarioRoles.add(usuarioRol);
//
//        try {
//            this.saveUsuario(usuario, usuarioRoles);
//        } catch (ResourceNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

}

