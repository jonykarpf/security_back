package com.api.security.domain.service;

import com.api.security.percistance.entity.Usuario;
import com.api.security.percistance.repository.UsuarioRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class UsuarioServiceImpl {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(UsuarioServiceImpl.class);

    // Inyeccion de dependencia de usuario repository
    @Autowired
    private UsuarioRepository usuarioRepository;





    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------



    public List<Usuario> findAllUsuarios() {
        LOGGER.info("OBTENIENDO TODOS LOS USUARIOS - METODO: UsuarioServiceImpl.findAllUsuarios()");
        return usuarioRepository.findAll();
    }


    public Optional<Usuario> findUsuarioById(int id) {
        LOGGER.info("OBTENIENDO USUARIO POR ID - METODO: UsuarioServiceImpl.findUsuarioById()");
        Optional<Usuario> usuario = usuarioRepository.findById(Math.toIntExact(id));
        if (usuario.isPresent()) {
            return usuario;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }
    }


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

    public ResponseEntity<Usuario> saveUsuario(Usuario usuario) {
           LOGGER.info("GUARDANDO USUARIO - METODO: UsuarioServiceImpl.saveUsuario()");

        if(usuario.getId() == null){
            Optional<Usuario> result = this.usuarioRepository.findById(usuario.getId());
            if(result.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El usuario con el id: " + usuario.getId() + " ya existe");
        }

        if((usuario.getEmail() != null) && (usuario.getNickname() != null) &&
                (usuario.getPassword() != null) && (usuario.getRol() != null)
        ){

            Optional<Usuario> tempUser = this.usuarioRepository.findByEmail(usuario.getEmail());
            if(tempUser.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El email del usuario ya existe en la base de datos");
            tempUser = this.usuarioRepository.findByNickname(usuario.getNickname());
            if(tempUser.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El nickname del usuario ya existe en la base de datos");

            usuario.setPassword(this.convertToSHA256(usuario.getPassword()));

            return new ResponseEntity<>(this.usuarioRepository.save(usuario), HttpStatus.CREATED);
        }
        else
            LOGGER.error("ERROR AL GUARDAR USUARIO - METODO: UsuarioServiceImpl.saveUsuario()");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario no tiene todos los campos obligatorios");
    }


    public ResponseEntity<Usuario> update(int id, Usuario usuario) {
        LOGGER.info("ACTUALIZANDO USUARIO - METODO: UsuarioServiceImpl.update()");

        if(id > 0){
            Optional<Usuario> tempUser = this.usuarioRepository.findById(id);

            if(tempUser.isPresent()) {

                if(usuario.getNickname() != null)
                    tempUser.get().setNickname(usuario.getNickname());
                if(usuario.getPassword() != null)
                    tempUser.get().setPassword( this.convertToSHA256(usuario.getPassword()) );
                if(usuario.getRol() != null)
                    tempUser.get().setRol(usuario.getRol());
                try {

                    return new ResponseEntity<>(this.usuarioRepository.save(tempUser.get()), HttpStatus.CREATED);
                }
                catch(Exception ex){
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El usuario no se pudo actualizar");
                }
            }
            else
                LOGGER.error("ERROR AL ACTUALIZAR USUARIO - METODO: UsuarioServiceImpl.update()");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El usuario no existe en la base de datos");
        }
        else
            LOGGER.error("ERROR AL ACTUALIZAR USUARIO POR ID - METODO: UsuarioServiceImpl.update()");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El id del usuario no es válido");
    }


    public ResponseEntity<Boolean> delete(int id){
        LOGGER.info("ELIMINANDO USUARIO - METODO: UsuarioServiceImpl.delete()");
        Boolean success = this.findUsuarioById(id).map(user -> {
                this.usuarioRepository.delete(user);
                return true;
        }).orElse(false);
        if(success)
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            LOGGER.error("ERROR AL ELIMINAR USUARIO - METODO: UsuarioServiceImpl.delete()");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "El usuario no se pudo eliminar");
    }


    public Usuario login(Usuario user){
        LOGGER.info("INICIANDO SESION - METODO: UsuarioServiceImpl.login()");
        Usuario result;

        if(user.getPassword() != null && user.getEmail() != null) {
            String email = user.getEmail();

            String password = this.convertToSHA256( user.getPassword() );
            Optional<Usuario> tempUser = this.usuarioRepository.validateLogin(email, password);

            if(tempUser.isEmpty())
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Credenciales incorrectas");
            else
                result = tempUser.get();
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario no tiene todos los campos obligatorios");
        LOGGER.info("SESION INICIADA - METODO: UsuarioServiceImpl.login()");
        return result;
    }






    public String convertToSHA256(String password){
        LOGGER.info("CONVERTIR A SHA256 - METODO: UsuarioServiceImpl.convertToSHA256()");
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

