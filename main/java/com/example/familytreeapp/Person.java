package com.example.familytreeapp;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

public class Person implements Comparable<Person>{
    private String year_of_birth = null;
    private String year_of_death = null;
    private final String name;
    private String name_of_parent=null;
    private final Context context;
    private ArrayList<Person> childrenList = new ArrayList<>();
    private Person parent;

    public Person(String name,String name_of_parent,String year_of_birth,String year_of_death,Context context) {
        this.name = name;
        this.name_of_parent = name_of_parent;
        this.year_of_birth = year_of_birth;
        this.year_of_death = year_of_death;
        this.context = context;
    }

    public Person(String name,String name_of_parent,String year_of_birth,Context context) {
        this.name = name;
        this.name_of_parent = name_of_parent;
        this.year_of_birth = year_of_birth;
        this.context = context;
    }

    public Person(String name,String name_of_parent,Context context) {
        this.name = name;
        this.name_of_parent = name_of_parent;
        this.context = context;
    }

    public Person(String name,Context context) {
        this.name = name;
        this.context = context;
    }

    @NonNull
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if(name != null)
            builder.append(name).append(" ");
        if (name_of_parent != null)
            builder.append(" ,").append(context.getResources().getString(R.string.parent)).append(" : ").append(name_of_parent).append(" ");
        if (year_of_birth != null)
            builder.append(" ,").append(context.getResources().getString(R.string.birth)).append(" : ").append(year_of_birth).append(" ");
        if (year_of_death != null)
            builder.append(" ,").append(context.getResources().getString(R.string.death)).append(" : ").append(year_of_death);

        return builder.toString();
    }
    //setters and getters

    public ArrayList<Person> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(ArrayList<Person> childrenList) {
        this.childrenList = childrenList;
    }

    public String getName() {
        return name;
    }
    public String getName_of_parent() {
        return name_of_parent;
    }

    @Override
    public int compareTo(Person o) {
        return Comparators.NAME.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<Person> NAME = Comparator.comparing(person -> person.name);
    }

    public Person getParent() {
        return parent;
    }

    public void setParent(Person parent) {
        this.parent = parent;
    }
}
