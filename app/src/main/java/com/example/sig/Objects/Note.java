package com.example.sig.Objects;

public class Note {
    private int id_note;
    private int id_user;
    private int id_object;
    private String description;
    private double note;

    public Note() {
    }

    public int getId_note() {
        return id_note;
    }

    public void setId_note(int id_note) {
        this.id_note = id_note;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
