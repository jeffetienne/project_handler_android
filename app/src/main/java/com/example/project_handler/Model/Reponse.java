package com.example.project_handler.Model;

import java.util.Date;

public class Reponse {
    private int Id;
    private String Valeur;
    private Question Question;
    private int QuestionId;
    private String CreePar;
    private Date CreeLe;

    public Reponse(int id, String valeur, com.example.project_handler.Model.Question question, int questionId, String creePar, Date creeLe) {
        Id = id;
        Valeur = valeur;
        Question = question;
        QuestionId = questionId;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public int getId() {
        return Id;
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
