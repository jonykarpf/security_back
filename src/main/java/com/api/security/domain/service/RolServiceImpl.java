package com.api.security.domain.service.impl;

import com.api.security.domain.service.RolService;
import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Rol;
import com.api.security.percistance.repository.RolRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RolServiceImpl implements RolService {

    // Loggers
    final static Logger LOGGER = Logger.getLogger(RolServiceImpl.class);


    // Inyeccion de dependencia rol repository
    @Autowired
    private RolRepository rolRepository;


    //----------------------BEGIN. IMPLEMENTED METHODS---------------------------

    @Override
    public Rol createRol(Rol rol) throws ResourceNotFoundException {
        LOGGER.info("GUARDANDO ROL METODO - createRol");
        Rol saveRol = rolRepository.save(rol);
        if (saveRol.getNombre() != null) {
            LOGGER.info("ROL GUARDADO CON EXITO");
            return saveRol;
        } else {
            LOGGER.info("ROL NO GUARDADO");
            throw new ResourceNotFoundException("Error al guardar rol nombre nulo");
        }
    }

    @Override
    public Optional<List<Rol>> findByRolName(String nombre) throws ResourceNotFoundException {
        LOGGER.info("BUSCANDO ROL POR NOMBRE - findByRolName");
       try {
           Stream<Rol> rolStream = (Stream<Rol>) rolRepository.findByRolName(nombre);
              return Optional.of(rolStream.collect(Collectors.toList()));
         } catch (Exception e) {
            LOGGER.info("ROL NO ENCONTRADO");
            throw new ResourceNotFoundException("Error al buscar rol por nombre");
       }
    }

    @Override
    public List<Rol> readAllRol() {
        LOGGER.info("OBTENIENDO TODOS LOS ROLES - readAllRol");
        Iterable<Rol> rolList = rolRepository.findAll();

        rolList.forEach(rol -> {
            if (rol.getNombre() == null) {
                LOGGER.error("ROL NO OBTENIDO");
                try {
                    throw new ResourceNotFoundException("Rol no encontrado");
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        LOGGER.info("ROLES OBTENIDOS CON EXITO");
        return (List<Rol>) rolList;
    }

    @Override
    public Rol updateRol(Rol rol) throws ResourceNotFoundException {
        LOGGER.info("ACTUALIZANDO ROL - updateRol");
        Optional<Rol> rolOptional = rolRepository.findById(rol.getIdRol());

        if (rolOptional.isPresent()) {
            LOGGER.info("ROL OBTENIDO CON EXITO");
            rolOptional = Optional.ofNullable(rolRepository.save(rol));
            return rolOptional.get();
        } else {
            LOGGER.error("ROL NO OBTENIDO");
            throw new ResourceNotFoundException("Rol no encontrado");

        }
    }

    @Override
    public Optional<Rol> deleteRolById(Long id) throws ResourceNotFoundException {
        LOGGER.info("ELIMINANDO ROL POR ID - deleteRolById");
        Optional<Rol> rol =  rolRepository.findById(id);

        rol.map(rol1 -> {
            rolRepository.delete(rol1);
            return rol1;
        }).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado" + id));

        LOGGER.info("ROL ELIMINADO CON EXITO");
        return rol;
    }

    //----------------------END. IMPLEMENTED METHODS---------------------------


    // metodo para insertar roles al desplegar la aplicacion
    @Override
    public void insertRoles() {

        LOGGER.info("INSERTANDO ROLES AL DESPLEGAR LA APLICACION - insertRoles");
        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");
        rolAdmin.setDescripcion("Rol de administrador mesas de votacion");
        rolRepository.save(rolAdmin);

        Rol rolUser = new Rol();
        rolUser.setNombre("USER");
        rolUser.setDescripcion("Rol de usuario");
        rolRepository.save(rolUser);

        LOGGER.info("ROLES INSERTADOS CON EXITO");

    }
}
