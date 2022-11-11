package com.api.security.percistance.entity;

import javax.persistence.*;

@Entity
@Table(name = "usuarios_roles")
public class UsuarioRol {
    // attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioRolId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    // constructors

    public UsuarioRol() {
    }

    public UsuarioRol(Long usuarioRolId, Usuario usuario, Rol rol) {
        this.usuarioRolId = usuarioRolId;
        this.usuario = usuario;
        this.rol = rol;
    }

    // getters and setters

    public Long getUsuarioRolId() {
        return usuarioRolId;
    }

    public void setUsuarioRolId(Long usuarioRolId) {
        this.usuarioRolId = usuarioRolId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
