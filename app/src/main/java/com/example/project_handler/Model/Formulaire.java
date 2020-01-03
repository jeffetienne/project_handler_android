package com.example.project_handler.Model;

import java.io.Serializable;
import java.util.Date;

public class Formulaire implements Serializable {
    private int Id;
    private String Name;
    private String Description;
    private TypeForm TypeForm;
    private int TypeFormId;
    private Projet Projet;
    private int ProjetId;
    private String CreePar;
    private Date CreeLe;

    public Formulaire(int id,
                      String name,
                      String description,
                      TypeForm typeForm,
                      int typeFormId,
                      Projet projet,
                      int projetId,
                      String creePar,
                      Date creeLe) {
        Id = id;
        Name = name;
        Description = description;
        TypeForm = typeForm;
        TypeFormId = typeFormId;
        Projet = projet;
        ProjetId = projetId;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public Formulaire(){}
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public com.example.project_handler.Model.TypeForm getTypeForm() {
        return TypeForm;
    }

    public void setTypeForm(com.example.project_handler.Model.TypeForm typeForm) {
        TypeForm = typeForm;
    }

    public int getTypeFormId() {
        return TypeFormId;
    }

    public void setTypeFormId(int typeFormId) {
        TypeFormId = typeFormId;
    }

    public com.example.project_handler.Model.Projet getProjet() {
        return Projet;
    }

    public void setProjet(com.example.project_handler.Model.Projet projet) {
        Projet = projet;
    }

    public int getProjetId() {
        return ProjetId;
    }

    public void setProjetId(int projetId) {
        ProjetId = projetId;
    }

    public String getCreePar() {
        return CreePar;
    }

    public void setCreePar(String creePar) {
        CreePar = creePar;
    }

    public Date getCreeLe() {
        return CreeLe;
    }

    public void setCreeLe(Date creeLe) {
        CreeLe = creeLe;
    }
}
