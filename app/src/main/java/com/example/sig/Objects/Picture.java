package com.example.sig.Objects;

public class Picture {
    private int id_picture;
    private int id_user;
    private int id_object;
    private String saison;
    private String file;

    public Picture() {
    }

    public int getId_picture() {
        return id_picture;
    }

    public void setId_picture(int id_picture) {
        this.id_picture = id_picture;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_object() {
        return id_object;
    }

    public void setId_object(int id_object) {
        this.id_object = id_object;
    }

    public String getSaison() {
        return saison;
    }

    public void setSaison(String saison) {
        this.saison = saison;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
