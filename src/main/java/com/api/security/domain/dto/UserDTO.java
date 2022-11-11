package com.api.security.domain.dto;

public class UserDTO {

    //Patron DATA TRANSFER OBJECT
    private Long id;
    private String username;
    private String email;
    private String rol;
    private String permission;

    private Boolean enabled;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String rol, String permission, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.permission = permission;
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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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
        return "UserDTO{" + "id=" + id + ", " +
                "username=" + username + ", email=" + email + ", " +
                "rol=" + rol + ", permission=" + permission + ", " +
                "enabled=" + enabled + '}';
    }
}
