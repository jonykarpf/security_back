package com.api.security.percistance.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Rol {
    // attributes
    @Id
    private Long rolId;
    private String nombre;

    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
    private Set<UsuarioRol> usuarioRoles = new HashSet<>();

    // constructors
    public Rol() {
    }

    public Rol(Long rolId, String nombre) {
        this.rolId = rolId;
        this.nombre = nombre;
    }

    // getters and setters

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<UsuarioRol> getUsuarioRoles() {
        return usuarioRoles;
    }

    public void setUsuarioRoles(Set<UsuarioRol> usuarioRoles) {
        this.usuarioRoles = usuarioRoles;
    }
}
