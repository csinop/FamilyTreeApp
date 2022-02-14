package com.example.familytreeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TreeDisplay extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_display);
    }

    //localize on clicked person
    public void localizeOnClick(String key){

    }

    public int determineWeight(Graph.Node<Person> node){
        // up is +1
        // sides does not change the weight
        // down is -1
        return -1;
    }

    public void setSiblingWeights(Graph.Node<Person> node){
        //to find the sibling of a node, we need to go up one level and check its childrenList
        //weight for a sibling is

    }
    /*for(Person key:keys){
        String[] temp = valueOf(key).split(",");
        if(containsIgnoreCaseRegexp(temp[0], name)){
            similarPeople.add(key);
        }
    }*/
}