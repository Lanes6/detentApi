package com.example.sig.Objects;

public class User {
    private int id_user;
    private String login;
    private String mail;
    private String hash_mdp;
    private String secret_token;
    private String secret_token_refrech;

    public String getSecret_token_refrech() {
        return secret_token_refrech;
    }

    public void setSecret_token_refrech(String secret_token_refrech) {
        this.secret_token_refrech = secret_token_refrech;
    }

    public User() {
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHash_mdp() {
        return hash_mdp;
    }

    public void setHash_mdp(String hash_mdp) {
        this.hash_mdp = hash_mdp;
    }

    public String getSecret_token() {
        return secret_token;
    }

    public void setSecret_token(String secret_token) {
        this.secret_token = secret_token;
    }
}
