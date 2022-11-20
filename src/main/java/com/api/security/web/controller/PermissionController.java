package com.api.security.web.controller;



import com.api.security.domain.service.impl.PermissionServiceImpl;
import com.api.security.exception.ResourceNotFoundException;
import com.api.security.percistance.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@RestController
@RequestMapping("/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {

    // Inyeccion de dependencia permission service
    @Autowired
    private PermissionServiceImpl permissionService;


    // ----------------------BEGIN. IMPLEMENTED METHODS---------------------------

    @PostMapping("/save")
    public ResponseEntity<?> savePermission(@RequestBody Permission permission) throws ResourceNotFoundException {
        ResponseEntity response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Permission permissionResponse = permissionService.savePermission(permission);

        if (permissionResponse != null) {
            response = new ResponseEntity(permissionResponse, HttpStatus.CREATED);

        } else {
            response = new ResponseEntity<>("PERMISSION NOT FOUND", HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/read")
    public ResponseEntity<?> readAllPermissions() throws ResourceNotFoundException {
        AtomicReference<ResponseEntity> response = new AtomicReference<>(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        List<Permission> permissionList = permissionService.getAllPermissions();

        // si hay permisos en la lista de permisos entonces se retorna la lista de permisos
        permissionList.forEach(permission -> {
            if (permission != null) {
                response.set(new ResponseEntity<>(permissionList, HttpStatus.OK));
            }
        });

        return response.get();
    }



    @GetMapping("/read/{id}")
    public ResponseEntity<?> readPermission(@PathVariable("id") Long id) throws ResourceNotFoundException {
        AtomicReference<ResponseEntity> response = new AtomicReference<>(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        Optional<Permission> permissionResponse = permissionService.getPermissionById(id);

        permissionResponse.ifPresent(permission -> {
            if (permission != null) {
                response.set(new ResponseEntity<>(permission, HttpStatus.OK));
            } else {
                response.set(new ResponseEntity<>("PERMISSION NOT FOUND", HttpStatus.NOT_FOUND));
            }
        });

        return response.get();
    }


    @PutMapping("/update")
    public ResponseEntity<?> updatePermission(@RequestBody Permission permission) throws ResourceNotFoundException {
        AtomicReference<ResponseEntity> response = new AtomicReference<>(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        List<Permission> permissionList = permissionService.getAllPermissions();

        // se recorre la lista de permisos para buscar el permiso a actualizar por su id
        permissionList.forEach(permission1 -> {
            if (permission1.getIdPermission().equals(permission.getIdPermission())) {
                Permission permissionResponse = null;
                try {
                    permissionResponse = permissionService.updatePermission(permission);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if (permissionResponse != null) {
                    response.set(new ResponseEntity<>(permissionResponse, HttpStatus.OK));
                } else {
                    response.set(new ResponseEntity<>("PERMISSION NOT FOUND", HttpStatus.NOT_FOUND));
                }
            }
        });

        return response.get();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable("id") Long id) throws ResourceNotFoundException {
        AtomicReference<ResponseEntity> response = new AtomicReference<>(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        Optional<Permission> permissionResponse = permissionService.deletePermissionById(id);

        // mapeamos el optional de permiso para verificar si el permiso existe
        permissionResponse.map(permission -> {
            if (permission != null) {
                response.set(new ResponseEntity<>(permission, HttpStatus.OK));
            } else {
                response.set(new ResponseEntity<>("PERMISSION NOT FOUND", HttpStatus.NOT_FOUND));
            }
            return response;
        });

        return response.get();
    }

    // ----------------------END. IMPLEMENTED METHODS---------------------------


}
