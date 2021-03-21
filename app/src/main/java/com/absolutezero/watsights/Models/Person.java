package com.absolutezero.watsights.Models;

public class Person {
    int id;
    String name;
    boolean isElite;
    boolean isSpammer;

    public Person(int id, String name, boolean isElite, boolean isSpammer) {
        this.id = id;
        this.name = name;
        this.isElite = isElite;
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

    public boolean isElite() {
        return isElite;
    }

    public void setElite(boolean important) {
        isElite = important;
    }

    public boolean isSpammer() {
        return isSpammer;
    }

    public void setSpammer(boolean spammer) {
        isSpammer = spammer;
    }
}
