package com.api.security.percistance.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.Serializable;


@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    // attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;
    @Column(name="nickname", nullable = false, unique = true, length = 50)
    private String nickname;
    @Column(name="email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name="idRol")
    @JsonIgnoreProperties("usuario")
    private Rol rol;


    // constructors

    public Usuario() {
    }


    public Usuario(Integer idUser, String nickname, String email, String password, Boolean active, Rol rol) {
        this.idUser = idUser;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.active = active;
        this.rol = rol;
    }

    public Integer getId() {
        return idUser;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public Rol getRol() {
        return rol;
    }


    public void setRol(Rol rol) {
        this.rol = rol;
    }
}

