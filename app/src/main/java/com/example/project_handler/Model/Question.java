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
}
