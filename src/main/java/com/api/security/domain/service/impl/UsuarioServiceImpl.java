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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(UsuarioServiceImpl.class);

    // Inyeccion de dependencia de usuario repository
    private UsuarioRepository usuarioRepository;

    // mapeador de objetos se usa para convertir un objeto a otro tipo de objeto debido a que usamos el patron DTO
    private ObjectMapper mapper;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, ObjectMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    // Inyeccion de dependencia de usuario rol repository
    @Autowired
    private RolRepository rolRepository;


    // Inyeccion de dependencia BCryptPasswordEncoder
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------

    @Override
    public UsuarioDTO saveUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws ResourceNotFoundException {
        LOGGER.info("GUARDANDO USUARIO METODO - saveUsuario");
        UsuarioDTO saveUsuarioDTO = new UsuarioDTO();
        UsuarioDTO usuarioLocal = mapper.convertValue(usuarioRepository.findByUsername(usuario.getUsername()), UsuarioDTO.class);
        if (usuarioLocal != null) {
            LOGGER.info("USUARIO YA EXISTE EN LA BASE DE DATOS");
            throw new ResourceNotFoundException("Usuario ya existe");
        } else {
            // asignar roles al usuario
            for (UsuarioRol ur : usuarioRoles) {
                rolRepository.save(ur.getRol());
            }
            usuario.setUsuarioRoles(usuarioRoles);
            // guardar usuario
            usuario.getUsuarioRoles().addAll(usuarioRoles);
            usuarioLocal = mapper.convertValue(usuarioRepository.save(usuario), UsuarioDTO.class);
        }
        return saveUsuarioDTO;

    }

    @Override
    public UsuarioDTO getUsuario(String username) throws ResourceNotFoundException {
        LOGGER.info("OBTENIENDO USUARIO METODO - getUsuario");
        Optional<Usuario> usuario;
        UsuarioDTO getUsuarioDTO = new UsuarioDTO();

        if (username != null) {
            usuario = Optional.ofNullable(usuarioRepository.findByUsername(username));
            if (usuario.isPresent()) {
                getUsuarioDTO = mapper.convertValue(usuario.get(), UsuarioDTO.class);
            }
            else {
                LOGGER.error("USUARIO NO ENCONTRADO");
                throw new ResourceNotFoundException("Usuario no encontrado");
            }
        }
        LOGGER.info("USUARIO ENCONTRADO");
        return getUsuarioDTO;


    }

    @Override
    public Optional<UsuarioDTO> deleteUsuario(Long usuarioId) {
        LOGGER.info("ELIMINANDO USUARIO METODO - deleteUsuario en <<UsuarioServiceImpl>>");
        UsuarioDTO deleteUsuarioDTO = new UsuarioDTO();
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
            deleteUsuarioDTO = mapper.convertValue(usuario.get(), UsuarioDTO.class);
        }
        LOGGER.info("USUARIO ELIMINADO CORRECTAMENTE");
        return Optional.ofNullable(deleteUsuarioDTO);
    }

    @Override
    public List<UsuarioDTO> getAllUsuarios() {
        LOGGER.info("OBTENIENDO TODOS LOS USUARIOS METODO - getAllUsuarios");
        List<UsuarioDTO> getAllUsuariosDTO = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario usuario : usuarios) {
            getAllUsuariosDTO.add(mapper.convertValue(usuario, UsuarioDTO.class));
        }
        LOGGER.info("USUARIOS OBTENIDOS CORRECTAMENTE");
        return getAllUsuariosDTO;
    }

    //----------------------END. IMPLEMENTED METHODS---------------------------



    // metodo para insertar un usuario en la base de datos al desplegar la aplicacion con rol de administrador
    @Override
    public void insertarUsuarios() {

        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword(bCryptPasswordEncoder.encode("12345"));
        usuario.setEmail("jony@hotmail.com");
        usuario.setEnabled(true);


        Rol rol = new Rol();
        rol.setRolId(1L);
        rol.setNombre("ADMIN");

        Set<UsuarioRol> usuarioRoles = new HashSet<>();
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setRol(rol);
        usuarioRol.setUsuario(usuario);
        usuarioRoles.add(usuarioRol);

        try {
            this.saveUsuario(usuario, usuarioRoles);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

    }

}

