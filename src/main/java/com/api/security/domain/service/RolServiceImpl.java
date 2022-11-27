package com.api.security.domain.service;

import com.api.security.percistance.entity.Permission;
import com.api.security.percistance.entity.Rol;
import com.api.security.percistance.repository.PermissionRepository;
import com.api.security.percistance.repository.RolRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class RolServiceImpl {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(RolServiceImpl.class);


    // Inyeccion de dependencia rol repository
    @Autowired
    private RolRepository rolRepository;


    @Autowired
    private PermissionRepository permissionRepository;




    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------


    public List<Rol> index(){
        return (List<Rol>)this.rolRepository.findAll();
    }


    public Optional<Rol> showId(Integer id){
        LOGGER.info("OBTENIENDO ROL POR ID - METODO: RolServiceImpl.showId()");
        Optional<Rol> result = this.rolRepository.findById(id);
        if(result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Rol no existe en la base de datos");
        return result;
    }

    public ResponseEntity<Rol> create(Rol newRol){
        LOGGER.info("CREANDO ROL - METODO: RolServiceImpl.create()");
        if(newRol.getIdRol() != null) {
            Optional<Rol> result = this.rolRepository.findById(Math.toIntExact(newRol.getIdRol()));
            if (result.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rol ya existe en la base de datos");
        }

        if(newRol.getName() != null) {
            LOGGER.error("ROL YA EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.create()");
            Optional<Rol> tempRol = this.rolRepository.findByName(newRol.getName());
            if (tempRol.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rol ya existe en la base de datos");
            LOGGER.info("GUARDANDO ROL EN LA BASE DE DATOS - METODO: RolServiceImpl.create()");
            return new ResponseEntity<>(this.rolRepository.save(newRol), HttpStatus.ACCEPTED);
        }
        else
            LOGGER.error("ROL NO PUEDE SER NULO - METODO: RolServiceImpl.create()");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol no cumple con los campos obligatorios");
    }


    public ResponseEntity<Rol> update(Integer id, Rol updatedRol){
        LOGGER.info("ACTUALIZANDO ROL - METODO: RolServiceImpl.update()");
        if(id > 0){
            Optional<Rol> tempRol = this.showId(id);
            if(tempRol.isPresent()){
                if(updatedRol.getName() != null)
                    tempRol.get().setName(updatedRol.getName());
                if(updatedRol.getDescription() != null)
                    tempRol.get().setDescription(updatedRol.getDescription());
                try {
                    LOGGER.info("GUARDANDO ROL EN LA BASE DE DATOS - METODO: RolServiceImpl.update()");
                    return new ResponseEntity<>(this.rolRepository.save(tempRol.get()), HttpStatus.CREATED);
                }
                catch(Exception ex){
                    LOGGER.error("ERROR AL GUARDAR ROL EN LA BASE DE DATOS - METODO: RolServiceImpl.update()");
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Rol no cumple con las restricciones de la base de datos");
                }
            }
            else
                LOGGER.error("ROL NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.update()");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Rol no existe en la base de datos");
        }
        else
            LOGGER.error("ID ROL NO PUEDE SER MENOR O IGUAL A CERO - METODO: RolServiceImpl.update()");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol ID no es válido");
    }

    public ResponseEntity<Rol> addPermission(Integer idRol, Integer idPermission) {
        LOGGER.info("AGREGANDO PERMISO A ROL - METODO: RolServiceImpl.addPermission()");
        Optional<Rol> rol = this.rolRepository.findById(idRol);

        if(rol.isPresent()) {
            Optional<Permission> permission = this.permissionRepository.findById(idPermission);

            if (permission.isPresent()) {
                Set<Permission> tempPermissions = rol.get().getPermissions();
                if(tempPermissions.contains(permission))
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Rol ya tiene asignada la permiso");
                LOGGER.info("AGREGANDO PERMISO A ROL - METODO: RolServiceImpl.addPermission()");
                tempPermissions.add(permission.get());
                rol.get().setPermissions(tempPermissions);
                return new ResponseEntity<>(this.rolRepository.save(rol.get()), HttpStatus.CREATED);
            }
            else
                LOGGER.error("PERMISO NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.addPermission()");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Permiso no existe en la base de datos");
        }
        else
            LOGGER.error("ROL NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.addPermission()");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Rol no existe en la base de datos");
    }

    public ResponseEntity<Rol> removePermission(Integer idRol, Integer idPermission) {
        LOGGER.info("QUITANDO PERMISO A ROL - METODO: RolServiceImpl.removePermission()");
        Optional<Rol> rol = this.rolRepository.findById(idRol);

        if(rol.isPresent()) {
            Optional<Permission> permission = this.permissionRepository.findById(idPermission);

            if (permission.isPresent()) {
                Set<Permission> tempPermissions = rol.get().getPermissions();
                if(!tempPermissions.contains(permission))
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Rol no tiene asignada el permiso");
                LOGGER.info("QUITANDO PERMISO A ROL - METODO: RolServiceImpl.removePermission()");
                tempPermissions.remove(permission.get());
                rol.get().setPermissions(tempPermissions);
                return new ResponseEntity<>(this.rolRepository.save(rol.get()), HttpStatus.CREATED);
            }
            else
                LOGGER.error("PERMISO NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.removePermission()");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Permiso no existe en la base de datos");
        }
        else
            LOGGER.error("ROL NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.removePermission()");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Rol no existe en la base de datos");
    }


    public ResponseEntity<Boolean> delete(Integer id){
        LOGGER.info("ELIMINANDO ROL - METODO: RolServiceImpl.delete()");
        Boolean success = this.showId(id).map(rol -> {
            this.rolRepository.delete(rol);
            return true;
        }).orElse(false);
        if(success)

            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            LOGGER.error("ROL NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.delete()");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Rol no pudo ser eliminado");
    }


    public ResponseEntity<Boolean> validateGrant(Integer idRol, Permission permission){
        LOGGER.info("VALIDANDO PERMISO - METODO: RolServiceImpl.validateGrant()");
        boolean isGrant = false;
        Optional<Rol> tempRol = this.rolRepository.findById(idRol);

        if(tempRol.isPresent()){

            for(Permission rolPermission: tempRol.get().getPermissions())
                if(rolPermission.getUrl().equals(permission.getUrl()) &&
                        rolPermission.getMethod().equals(permission.getMethod())){
                    isGrant = true;
                    break;
                }
            if(isGrant)
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                LOGGER.error("PERMISO NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.validateGrant()");
                return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        else
            LOGGER.error("ROL NO EXISTE EN LA BASE DE DATOS - METODO: RolServiceImpl.validateGrant()");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El Rol proporcionado no existe en la base de datos");
    }


    //-----------------------------END. ROL SERVICE IMPLEMENTATION--------------------------------


}
