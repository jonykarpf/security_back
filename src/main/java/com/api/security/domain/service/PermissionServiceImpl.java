package com.api.security.domain.service.impl;

import com.api.security.domain.service.PermissionService;
import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Permission;
import com.api.security.percistance.repository.PermissionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(PermissionServiceImpl.class);

    // Inyeccion de dependencia permission repository
    @Autowired
    private PermissionRepository permissionRepository;


    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------

    @Override
    public Permission savePermission(Permission permission) throws ResourceNotFoundException {
        LOGGER.info("GUARDANDO PERMISO METODO - savePermission");
        Permission savePermission = permissionRepository.save(permission);
        if (savePermission.getUrl() != null && savePermission.getMetodo() != null) {
            LOGGER.info("PERMISO GUARDADO CON EXITO");
            return savePermission;
        } else {
            LOGGER.info("PERMISO NO GUARDADO");
            throw new ResourceNotFoundException("Permiso no guardado");
        }
    }


    @Override
    public List<Permission> getAllPermissions() throws ResourceNotFoundException {
        LOGGER.info("OBTENIENDO TODOS LOS PERMISOS - getAllPermissions");
        List<Permission> permissionList = new ArrayList<>();
        List<Permission> permissionListLocal = permissionRepository.findAll();

        if (permissionListLocal != null) {
            permissionList = permissionListLocal;
        } else {
            LOGGER.info("NO HAY PERMISOS EN LA BASE DE DATOS");
            throw new ResourceNotFoundException("No hay permisos en la base de datos");
        }
        return permissionList;
    }

    @Override
    public Optional<Permission> getPermissionById(Long idPermission) throws ResourceNotFoundException {
        LOGGER.info("OBTENIENDO PERMISO POR ID - getPermissionById");
        Optional<Permission> permission = permissionRepository.findById(idPermission);
        if (!permission.isPresent()) {
            LOGGER.error("PERMISO NO ENCONTRADO");
            throw new ResourceNotFoundException("Permiso no encontrado");
        } else {
            LOGGER.info("PERMISO ENCONTRADO");
            return permission;
        }
    }

    @Override
    public Permission updatePermission(Permission permission) throws ResourceNotFoundException {
        LOGGER.info("ACTUALIZANDO PERMISO - updatePermission");
        Optional<Permission> permissionOptional = permissionRepository.findById(permission.getIdPermission());
        if (permissionOptional.isPresent()) {
            LOGGER.info("PERMISO ENCONTRADO");
            Permission updatePermission = permissionRepository.save(permission);
        } else {
            LOGGER.error("PERMISO NO ENCONTRADO");
            throw new ResourceNotFoundException("Permiso no encontrado");
        }
        return permission;
    }

    @Override
    public Optional<Permission> deletePermissionById(Long idPermission) throws ResourceNotFoundException {
       LOGGER.info("ELIMINANDO PERMISO - deletePermissionById");
       Optional<Permission> permission = permissionRepository.findById(idPermission);
       permission.map(permission1 -> {
           permissionRepository.delete(permission1);
           return permission1;
       }).orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado"));

         return permission;
    }


    //----------------------END. IMPLEMENTED METHODS---------------------------


    // insertar permisos al iniciar la aplicacion
    @Override
    public void insertarPermisos() {


    }
}

