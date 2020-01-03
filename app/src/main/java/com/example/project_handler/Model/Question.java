package com.example.project_handler.Model;

import java.io.Serializable;
import java.util.Date;

public class Question implements Serializable {
    private int Id;
    private String Name;
    private String Message;
    private Formulaire Formulaire;
    private int FormulaireId;
    private Component Component;
    private int ComponentId;
    private TypeDonnee TypeDonnee;
    private int TypeDonneeId;
    private int Minimum;
    private int Maximum;
    private Boolean Required;
    private String CreePar;
    private Date CreeLe;

    public Question(int id,
                    String name,
                    String message,
                    Formulaire formulaire,
                    int formulaireId,
                    Component component,
                    int componentId,
                    TypeDonnee typeDonnee,
                    int typeDonneeId,
                    int minimum,
                    int maximum,
                    Boolean required,
                    String creePar,
                    Date creeLe) {
        Id = id;
        Name = name;
        Message = message;
        Formulaire = formulaire;
        FormulaireId = formulaireId;
        Component = component;
        ComponentId = componentId;
        TypeDonnee = typeDonnee;
        TypeDonneeId = typeDonneeId;
        Minimum = minimum;
        Maximum = maximum;
        Required = required;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public Question(){}

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

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public com.example.project_handler.Model.Formulaire getFormulaire() {
        return Formulaire;
    }

    public void setFormulaire(com.example.project_handler.Model.Formulaire formulaire) {
        Formulaire = formulaire;
    }

    public int getFormulaireId() {
        return FormulaireId;
    }

    public void setFormulaireId(int formulaireId) {
        FormulaireId = formulaireId;
    }

    public com.example.project_handler.Model.Component getComponent() {
        return Component;
    }

    public void setComponent(com.example.project_handler.Model.Component component) {
        Component = component;
    }

    public int getComponentId() {
        return ComponentId;
    }

    public void setComponentId(int componentId) {
        ComponentId = componentId;
    }

    public com.example.project_handler.Model.TypeDonnee getTypeDonnee() {
        return TypeDonnee;
    }

    public void setTypeDonnee(com.example.project_handler.Model.TypeDonnee typeDonnee) {
        TypeDonnee = typeDonnee;
    }

    public int getTypeDonneeId() {
        return TypeDonneeId;
    }

    public void setTypeDonneeId(int typeDonneeId) {
        TypeDonneeId = typeDonneeId;
    }

    public int getMinimum() {
        return Minimum;
    }

    public void setMinimum(int minimum) {
        Minimum = minimum;
    }

    public int getMaximum() {
        return Maximum;
    }

    public void setMaximum(int maximum) {
        Maximum = maximum;
    }

    public Boolean getRequired() {
        return Required;
    }

    public void setRequired(Boolean required) {
        Required = required;
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
