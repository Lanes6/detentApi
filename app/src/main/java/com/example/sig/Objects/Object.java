package com.example.sig.Objects;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Object {
    private int id_object;
    private int id_user;
    private String titre;
    private String type;
    private String description;
    private LatLng goem;

    public Object() {
    }

    public int getId_object() {
        return id_object;
    }

    public void setId_object(int id_object) {
        this.id_object = id_object;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getGoem() {
        return goem;
    }

    public void setGoem(LatLng goem) {
        this.goem = goem;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
