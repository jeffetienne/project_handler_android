package com.example.project_handler.Model;

import java.io.Serializable;
import java.util.Date;

public class DynamicReference implements Serializable {
    private int Id;
    private String Code;
    private String Texte;
    private Question Question;
    private int QuestionId;
    private String CreePar;
    private Date CreeLe;

    public DynamicReference(int id,
                            String code,
                            String texte,
                            Question question,
                            int questionId,
                            String creePar,
                            Date creeLe) {
        Id = id;
        Code = code;
        Texte = texte;
        Question = question;
        QuestionId = questionId;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public DynamicReference(int id,
                            String code,
                            String texte,
                            int questionId,
                            String creePar,
                            Date creeLe) {
        Id = id;
        Code = code;
        Texte = texte;
        QuestionId = questionId;
        CreePar = creePar;
        CreeLe = creeLe;
    }

    public DynamicReference(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
