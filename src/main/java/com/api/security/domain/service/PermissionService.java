package com.api.security.domain.service;

import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    // CRUD methods

    // Create
    public Permission savePermission(Permission permission) throws ResourceNotFoundException;

    // READ ALL
    public List<Permission> getAllPermissions() throws ResourceNotFoundException;

    // READ BY ID
    Optional<Permission> getPermissionById(Long idPermission) throws ResourceNotFoundException;

    // UPDATE
    public Permission updatePermission(Permission permission) throws ResourceNotFoundException;

    // DELETE BY ID
    Optional<Permission> deletePermissionById(Long idPermission) throws ResourceNotFoundException;

    // insertar permisos al iniciar la aplicacion
    public void insertarPermisos();



}
