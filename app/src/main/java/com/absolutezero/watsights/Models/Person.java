package com.absolutezero.watsights.Models;

public class Person {
    int id;
    String name;
    boolean isImportant;
    boolean isSpammer;

    public Person(int id, String name, boolean isImportant, boolean isSpammer) {
        this.id = id;
        this.name = name;
        this.isImportant = isImportant;
        this.isSpammer = isSpammer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isSpammer() {
        return isSpammer;
    }

    public void setSpammer(boolean spammer) {
        isSpammer = spammer;
    }
}
