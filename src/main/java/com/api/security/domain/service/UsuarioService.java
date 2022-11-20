package com.api.security.domain.service;

import com.api.security.domain.dto.UsuarioDTO;
import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Usuario;
import com.api.security.percistance.entity.UsuarioRol;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UsuarioService {

    // CRUD methods
    public UsuarioDTO saveUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws ResourceNotFoundException;

    // obtener usuario por username
    public UsuarioDTO getUsuario(String username) throws ResourceNotFoundException;

    // eliminar usuario por id
    public Optional<UsuarioDTO> deleteUsuario(Long usuarioId) throws ResourceNotFoundException;

    // obtener todos los usuarios
    List<UsuarioDTO> getAllUsuarios();

    // actualizar usuario
    public UsuarioDTO updateUsuario(Usuario usuario) throws ResourceNotFoundException;

    // insertar usuarios al iniciar la aplicacion
    public void insertarUsuarios();
}
