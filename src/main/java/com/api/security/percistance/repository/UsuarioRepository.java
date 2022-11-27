package com.api.security.percistance.repository;

import com.api.security.percistance.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    // Query para buscar un usuario por su nombre de usuario
    Optional<Usuario> findByNickname(String nickname);

    // Query para buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // JPQL: buscaar por email y password
    @Query(value = "SELECT * FROM user WHERE email=? AND password=?;", nativeQuery = true)
    Optional<Usuario> validateLogin(String email, String password);

}

