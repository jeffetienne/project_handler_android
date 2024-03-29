package com.example.project_handler.Model;

import java.io.Serializable;
import java.util.Date;

public class Projet implements Serializable {
    private int Id;
    private String Name;
    private String Description;
    private Domaine Domaine;
    private int DomaineId;
    private String CreePar;
    private String CreeLe;

    public Projet(int id, String name, String description, com.example.project_handler.Model.Domaine domaine, int domaineId, String creePar, String creeLe) {
        Id = id;
        Name = name;
        Description = description;
        Domaine = domaine;
        DomaineId = domaineId;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public Projet(){

    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public com.example.project_handler.Model.Domaine getDomaine() {
        return Domaine;
    }

    public void setDomaine(com.example.project_handler.Model.Domaine domaine) {
        Domaine = domaine;
    }

    public int getDomaineId() {
        return DomaineId;
    }

    public void setDomaineId(int domaineId) {
        DomaineId = domaineId;
    }

    public String getCreePar() {
        return CreePar;
    }

    public void setCreePar(String creePar) {
        CreePar = creePar;
    }

    public String getCreeLe() {
        return CreeLe;
    }

    public void setCreeLe(String creeLe) {
        CreeLe = creeLe;
    }
}
