package com.example.sig.Objects;

public class Report {
    private int id_report;
    private int id_user;
    private int id_object;
    private String description;

    public Report() {
    }

    public int getId_report() {
        return id_report;
    }

    public void setId_report(int id_report) {
        this.id_report = id_report;
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
}
