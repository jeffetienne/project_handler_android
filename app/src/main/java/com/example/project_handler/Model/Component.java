package com.example.project_handler.Model;

import java.io.Serializable;

public class Component implements Serializable {
    private int Id;
    private String Name;

    public Component(int id, String name) {
        Id = id;
        Name = name;
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
}
