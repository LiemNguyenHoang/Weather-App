package com.example.wt.Model;

public class Weathers {
    private String Description,Icon,Id,Main;

    public Weathers() {
    }

    public Weathers(String description, String icon, String id, String main) {
        Description = description;
        Icon = icon;
        Id = id;
        Main = main;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMain() {
        return Main;
    }

    public void setMain(String main) {
        Main = main;
    }
}
