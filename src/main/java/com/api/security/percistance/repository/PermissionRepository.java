package com.api.security.percistance.repository;

import com.api.security.percistance.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    // Query para buscar un permiso por su url
    Optional<Permission> findByUrl(String url);

}


