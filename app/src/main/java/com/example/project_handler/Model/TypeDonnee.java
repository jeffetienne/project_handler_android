package com.example.project_handler.Model;

import java.io.Serializable;

public class TypeDonnee implements Serializable {
    private int Id;
    private String Name;

    public TypeDonnee(int id, String name) {
        Id = id;
        Name = name;
    }

    public TypeDonnee(){}

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
}
