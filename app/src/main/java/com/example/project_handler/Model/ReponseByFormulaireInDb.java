package com.example.project_handler.Model;

import java.util.Date;

public class ReponseByFormulaireInDb {
    private int Id;
    private int Groupe;
    private String Valeur;
    private int QuestionId;
    private String QuestionName;
    private String QuestionDescription;
    private int ReferenceId;
    private String Code;
    private String Texte;
    private String CreePar;
    private Date CreeLe;
    private int ComponentId;

    public ReponseByFormulaireInDb(int id,
                                   int groupe,
                                   String valeur,
                                   int questionId,
                                   String questionName,
                                   String questionDescription,
                                   int referenceId,
                                   String code,
                                   String texte,
                                   String creePar,
                                   Date creeLe,
                                   int ComponentId) {
        Id = id;
        Groupe = groupe;
        Valeur = valeur;
        QuestionId = questionId;
        QuestionName = questionName;
        QuestionDescription = questionDescription;
        ReferenceId = referenceId;
        Code = code;
        Texte = texte;
        CreePar = creePar;
        CreeLe = creeLe;
        ComponentId = ComponentId;
    }

    public ReponseByFormulaireInDb(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getGroupe() {
        return Groupe;
    }

    public void setGroupe(int groupe) {
        Groupe = groupe;
    }

    public String getValeur() {
        return Valeur;
    }

    public void setValeur(String valeur) {
        Valeur = valeur;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    public String getQuestionDescription() {
        return QuestionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        QuestionDescription = questionDescription;
    }

    public int getReferenceId() {
        return ReferenceId;
    }

    public void setReferenceId(int referenceId) {
        ReferenceId = referenceId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getTexte() {
        return Texte;
    }

    public void setTexte(String texte) {
        Texte = texte;
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

    public int getComponentId() {
        return ComponentId;
    }

    public void setComponentId(int componentId) {
        ComponentId = componentId;
    }
}
