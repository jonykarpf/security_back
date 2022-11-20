package com.api.security.percistance.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long idPermission;
    private String url;
    private String metodo;

    @ManyToMany(mappedBy = "permissions")
    // crear tabla intermedia muchos a muchos con rol
    private Set<Rol> roles;

    // constructors

    public Permission() {
    }

    public Permission(Long idPermission, String url, String metodo) {
        this.idPermission = idPermission;
        this.url = url;
        this.metodo = metodo;
    }

    // getters and setters

    public Long getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(Long idPermission) {
        this.idPermission = idPermission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}

