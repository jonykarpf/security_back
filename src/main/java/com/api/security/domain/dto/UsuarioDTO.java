package com.api.security.domain.dto;

public class UsuarioDTO {

    //Patron DATA TRANSFER OBJECT
    private Long id;
    private String username;
    private String email;
    private String rol;


    private Boolean enabled;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String username, String email, String rol,  Boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // toString

    @Override
    public String toString() {
        return "UsuarioDTO{" + "id=" + id + ", " +
                "username=" + username + ", email=" + email + ", " +
                "rol=" + rol+ ", enabled=" + enabled + '}';

    }
}
