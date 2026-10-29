package com.portalpolinema.backend.auth.dto;

public class LoginResponse {
    private String tokenType;
    private String accessToken;
    private long expiresIn;

    private Integer id_user;
    private String email;
    private String nama;
    private String role;
    private String foto;

    public LoginResponse(String accessToken, long expiresIn, Integer id_user, String email, String nama, String role,
            String foto) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.id_user = id_user;
        this.email = email;
        this.nama = nama;
        this.role = role;
        this.foto = foto;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public Integer getId_user() {
        return id_user;
    }

    public String getEmail() {
        return email;
    }

    public String getNama() {
        return nama;
    }

    public String getRole() {
        return role;
    }

    public String getFoto() {
        return foto;
    }
}
