package com.api.security.percistance.repository;

import com.api.security.percistance.entity.Rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    // query para buscar un rol por su nombre
    Optional<Rol> findByName(String name);


}
