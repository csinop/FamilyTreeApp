package com.example.familytreeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TreeDisplay extends AppCompatActivity {

    //graph
    private Graph<Person> theGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_display);

        buildGraphFromFile();
    }


    // graph building methods
    public void buildGraphFromFile(){
        SharedPreferences filePathPref = this.getSharedPreferences("filePreferences",MODE_PRIVATE);
        String s = filePathPref.getString("filePath",null);
        File newFile = new File(s);
        try {
            ArrayList<Person> peopleList = readPeople(newFile);
            theGraph = createGraph(peopleList);
        } catch (FileNotFoundException e) {
            Log.d("information","file not found");
            e.printStackTrace();
        }
    }

    public Graph.Node<Person> findParent(Person person, @NonNull ArrayList<Person> aList) {
        for(int i=0;i<aList.size();i++) {
            if(person.getName_of_parent().equals(aList.get(i).getName())) {
                return new Graph.Node<>(aList.get(i));
            }
        }
        return null;
    }

    public Graph<Person> createGraph(@NonNull ArrayList<Person> aList) {
        Graph<Person> aGraph = new Graph<>();
        for(int i=0; i<aList.size();i++) {
            Graph.Node<Person> newNode = new Graph.Node<>(aList.get(i));
            if(aList.get(i).getName_of_parent() == null) {
                aGraph.addNode(newNode);
            }else {
                Graph.Node<Person> parentNode = findParent(aList.get(i), aList);
                aGraph.addNode(newNode);
                newNode.addDestination(parentNode, 1);
                parentNode.getItem().getChildrenList().add(newNode.getItem());
                newNode.getItem().setParent(parentNode.getItem());
            }
        }
        return aGraph;
    }

    public ArrayList<Person> readPeople(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<Person> aList = new ArrayList<>();
        while (sc.hasNextLine()) {
            String[] temp = sc.nextLine().split(",");
            if(temp.length == 1) {
                Person dummyPerson = new Person(temp[0], TreeDisplay.this);
                aList.add(dummyPerson);
            }else if(temp.length == 2) {
                Person dummyPerson = new Person(temp[0], temp[1], TreeDisplay.this);
                aList.add(dummyPerson);
            }else if(temp.length == 3) {
                Person dummyPerson = new Person(temp[0], temp[1], temp[2], TreeDisplay.this);
                aList.add(dummyPerson);
            }else if(temp.length == 4) {
                Person dummyPerson = new Person(temp[0], temp[1], temp[2], temp[3], TreeDisplay.this);
                aList.add(dummyPerson);
            }
        }
        sc.close();
        return aList;
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