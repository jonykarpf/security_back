package com.api.security.web.controller;

import com.api.security.domain.service.RolServiceImpl;
import com.api.security.percistance.entity.Permission;
import com.api.security.percistance.entity.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/rol")
public class RolController {


    @Autowired
    private RolServiceImpl rolService;

    @GetMapping("/all")
    public List<Rol> getAllRoles(){
        return this.rolService.index();
    }

    @GetMapping("/{id}")
    public Optional<Rol> getRolById(@PathVariable("id") Integer id){
        return this.rolService.showId(id);
    }

    @GetMapping("/validate/{idRol}")
    public ResponseEntity<Boolean> getRolById(@PathVariable("idRol") Integer idRol, @RequestBody Permission permission){
        return this.rolService.validateGrant(idRol, permission);
    }

    @PostMapping("/insert")
    public ResponseEntity<Rol> insertRol(@RequestBody Rol rol){
        return this.rolService.create(rol);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Rol> updateRol(@PathVariable("id") Integer id, @RequestBody Rol rol){
        return this.rolService.update(id, rol);
    }

    @PutMapping("/update/{idRol}/add_permission/{idPermission}")
    public ResponseEntity<Rol> updateRolAddPermission(@PathVariable("idRol") Integer idRol, @PathVariable("idPermission") int idPermission){
        return this.rolService.addPermission(idRol, idPermission);
    }

    @PutMapping("/update/{idRol}/remove_permission/{idPermission}")
    public ResponseEntity<Rol> updateRolRemovePermission(@PathVariable("idRol") Integer idRol, @PathVariable("idPermission") int idPermission){
        return this.rolService.removePermission(idRol, idPermission);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteRol(@PathVariable("id") Integer id){
        return this.rolService.delete(id);
    }
}
