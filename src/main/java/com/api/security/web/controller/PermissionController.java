package com.api.security.web.controller;


import com.api.security.domain.service.PermissionServiceImpl;
import com.api.security.percistance.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionServiceImpl permissionService;

    @GetMapping("/all")
    public List<Permission> getAllPermissions(){
        return this.permissionService.index();
    }

    @GetMapping("/{id}")
    public Optional<Permission> getPermissionById(@PathVariable("id") Integer id){
        return this.permissionService.showId(id);
    }

    @PostMapping("/insert")
    public ResponseEntity<Permission> insertPermission(@RequestBody Permission permission){
        return this.permissionService.create(permission);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable("id") Integer id, @RequestBody Permission permission){
        return this.permissionService.update(id, permission);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deletePermission(@PathVariable("id") Integer id){
        return this.permissionService.delete(id);
    }
}
