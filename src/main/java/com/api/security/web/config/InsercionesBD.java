package com.api.security.web.config;

import com.api.security.domain.service.impl.PermissionServiceImpl;
import com.api.security.domain.service.impl.RolServiceImpl;
import com.api.security.domain.service.impl.UsuarioServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
// anotacion para suprimir la serialización de propiedades de objetos relacionados
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsercionesBD {


    //inyecccion de dependencia usuario service impl
    @Autowired
    private UsuarioServiceImpl usuarioService;


    // inyeccion de dependencia permission service impl
    @Autowired
    private PermissionServiceImpl permissionService;


    // inyeccion de dependencia role service impl
    @Autowired
    private RolServiceImpl roleService;



    // anotacion que llama al metodo al iniciar la aplication para insertar datos en la base de datos
    @PostConstruct
    public void insertarDatos() {

        //Insertar datos en la base de datos
        usuarioService.insertarUsuarios();

        // insertar permisos en la base de datos
        permissionService.insertarPermisos();

        // insertar roles en la base de datos
        roleService.insertRoles();



    }
}
