package com.api.security.domain.service;


import com.api.security.percistance.entity.Permission;
import com.api.security.percistance.repository.PermissionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl  {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(PermissionServiceImpl.class);

    // Inyeccion de dependencia permission repository
    @Autowired
    private PermissionRepository permissionRepository;


    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------


    public List<Permission> index(){
        LOGGER.info("OBTENIENDO TODOS LOS PERMISOS - METODO: PermissionServiceImpl.index()");
        return (List<Permission>)this.permissionRepository.findAll();
    }

    public Optional<Permission> showId(Integer id){
        LOGGER.info("OBTENIENDO PERMISO POR ID - METODO: PermissionServiceImpl.showId()");
        Optional<Permission> result = this.permissionRepository.findById(id);
        if(result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Permiso no encontrado");
        return result;
    }

    public ResponseEntity<Permission> create(Permission newPermission){
        LOGGER.info("CREANDO PERMISO - METODO: PermissionServiceImpl.create()");
        if(newPermission.getIdPermission() != null) {
            Optional<Permission> result = this.permissionRepository.findById(newPermission.getIdPermission());
            if (result.isPresent())
                LOGGER.info("EL PERMISO YA EXISTE EN LA BASE DE DATOS");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El permiso ya existe");
        }


        if(newPermission.getUrl() != null && newPermission.getMethod() != null) {

            Optional<Permission> tempPermission = this.permissionRepository.findByUrl(newPermission.getUrl());
            if(tempPermission.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "URL ya existe");
            return new ResponseEntity<>(this.permissionRepository.save(newPermission), HttpStatus.CREATED);
        }
        else
            LOGGER.error("ERROR AL CREAR EL PERMISO - METODO: PermissionServiceImpl.create()");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "URL y método son obligatorios");
    }

    public ResponseEntity<Permission> update(Integer id, Permission updatedPermission){
        LOGGER.info("ACTUALIZANDO PERMISO - METODO: PermissionServiceImpl.update()");
        if(id > 0){
            Optional<Permission> tempPermission = this.showId(id);

            if(tempPermission.isPresent()){
                if(updatedPermission.getMethod() != null)
                    tempPermission.get().setMethod(updatedPermission.getMethod());
                if(updatedPermission.getUrl() != null)
                    tempPermission.get().setUrl(updatedPermission.getUrl());
                try {
                      LOGGER.info("PERMISO ACTUALIZADO CORRECTAMENTE - METODO: PermissionServiceImpl.update()");
                    return new ResponseEntity<>(this.permissionRepository.save(tempPermission.get()), HttpStatus.CREATED);
                }
                catch(Exception ex){
                    LOGGER.error("ERROR AL ACTUALIZAR EL PERMISO CONFLICTO - METODO: PermissionServiceImpl.update()");
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Conflicto  en la base de datos");
                }
            }
            else
                LOGGER.error("ERROR AL ACTUALIZAR EL PERMISO NO ENCONTRADO - METODO: PermissionServiceImpl.update()");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Permiso no encontrado en la base de datos");
        }
        else
            LOGGER.error("ERROR AL ACTUALIZAR EL PERMISO  ID INVALIDO - METODO: PermissionServiceImpl.update()");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Id inválido");
    }

    public ResponseEntity<Boolean> delete(Integer id){
        LOGGER.info("ELIMINANDO PERMISO - METODO: PermissionServiceImpl.delete()");
        Boolean success = this.showId(id).map(permission -> {
            this.permissionRepository.delete(permission);
            return true;
        }).orElse(false);
        if(success)
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            LOGGER.error("ERROR AL ELIMINAR EL PERMISO - METODO: PermissionServiceImpl.delete()");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error interno del servidor al eliminar el permiso");
    }





    //----------------------END. IMPLEMENTED METHODS---------------------------



}

