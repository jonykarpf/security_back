package com.api.security.web.controller;

import com.api.security.domain.service.UsuarioServiceImpl;
import com.api.security.percistance.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;



    @GetMapping("/all")
    public List<Usuario> getAllUsers(){
        return this.usuarioService.findAllUsuarios();
    }

    @GetMapping("/by_id/{id}")
    public Optional<Usuario> getUserById(@PathVariable("id") int id){
        return this.usuarioService.findUsuarioById( id);
    }

    @GetMapping("/by_nickname/{nickname}")
    public Optional<Usuario> getUserByNickname(@PathVariable("nickname") String nickname){
        return this.usuarioService.findUsuarioByNickname(nickname);
    }

    @GetMapping("/by_email/{email}")
    public Optional<Usuario> getUserByEmail(@PathVariable("email") String email){
        return this.usuarioService.findUsuarioByEmail(email);
    }

    @PostMapping("/insert")
    public ResponseEntity<Usuario> insertUser(@RequestBody Usuario usuario){
        return this.usuarioService.saveUsuario(usuario);
    }

    @PostMapping("/login")
    public Usuario loginUser(@RequestBody Usuario usuario){
        return this.usuarioService.login(usuario);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable("id") int id, @RequestBody Usuario usuario){
        return this.usuarioService.update(Math.toIntExact(id), usuario);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") int id){
        return this.usuarioService.delete(Math.toIntExact(id));
    }
}
