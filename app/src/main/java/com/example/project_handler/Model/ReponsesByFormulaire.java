package com.example.project_handler.Model;

import java.io.Serializable;
import java.util.Date;

public class ReponsesByFormulaire implements Serializable {
    private int Id;
    private int Groupe;
    private String Valeur;
    private Question Question;
    private int QuestionId;
    private int ReferenceId;
    private String Code;
    private String Texte;
    private String CreePar;
    private Date CreeLe;

    public ReponsesByFormulaire(int id,
                                int groupe,
                                String valeur,
                                int questionId,
                                int referenceId,
                                String code,
                                String texte,
                                String creePar,
                                Date creeLe) {
        Id = id;
        Groupe = groupe;
        Valeur = valeur;
        QuestionId = questionId;
        ReferenceId = referenceId;
        Code = code;
        Texte = texte;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public ReponsesByFormulaire(){}

    public int getId() {
        return Id;
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

    public com.example.project_handler.Model.Question getQuestion() {
        return Question;
    }

    public void setQuestion(com.example.project_handler.Model.Question question) {
        Question = question;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
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
}
