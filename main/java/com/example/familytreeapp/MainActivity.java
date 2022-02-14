package com.example.familytreeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //graph
    private final Graph<Person> theGraph = new Graph<>();

    // permissions
    private static final int REQUEST_PERMISSIONS = 1242;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_COUNT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initializeAppBasedOnSharedPreferences();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        requestPermissions(PERMISSIONS,REQUEST_PERMISSIONS);
    }

    // onClick Methods
    public void setExplorerButtonOnClick(){
        findViewById(R.id.explorerButton).setOnClickListener(view ->{
            Intent i = new Intent(this, ExplorerActivity.class);
            startActivity(i);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setSearchButtonOnClick(ArrayList<Person> aList){
        findViewById(R.id.searchButton).setOnClickListener(view ->{
            EditText inputText = findViewById(R.id.searchInput);
            String name = inputText.getText().toString();
            ArrayList<Person> similarPeople = checkForSimilarNames(name,aList);
            if(!name.isEmpty()){
                if(!similarPeople.isEmpty()){
                    initializeSearchList(name,similarPeople);
                }else{
                    findViewById(R.id.searchListLayout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.searchDisplay)).setText(
                            getResources().getString(R.string.person_not_found) + " " + name + " " + getResources().getString(R.string.not_a_part_of_family));
                }
            }else{
                findViewById(R.id.searchListLayout).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.searchDisplay)).setText(getResources().getString(R.string.has_to_have_a_name));
            }
        });
    }

    public void setReselectButtonOnClick(){
        findViewById(R.id.reselectButton).setOnClickListener(view ->{
            Intent i = new Intent(this, ExplorerActivity.class);
            startActivity(i);
        });
    }
    // onClick Methods

    // SharedPreferences methods
    public void initializeAppBasedOnSharedPreferences() throws FileNotFoundException {
        SharedPreferences filePathPref = this.getSharedPreferences("filePreferences",MODE_PRIVATE);
        String s = filePathPref.getString("filePath",null);
        if(s != null){
            if(!s.isEmpty()) {
                Log.d("filePath",s);
                setContentView(R.layout.search_layout);
                File newFile = new File(s);
                ArrayList<Person> peopleList = readPeople(newFile);
                setSearchButtonOnClick(peopleList);
                setReselectButtonOnClick();
                createGraph(peopleList);
            }else{
                setContentView(R.layout.activity_main);
                setExplorerButtonOnClick();
            }
        }else{
            setContentView(R.layout.activity_main);
            setExplorerButtonOnClick();
        }
    }
    // SharedPreferences Methods end

    // ListView methods
    @SuppressLint("SetTextI18n")
    public void initializeSearchList(String name, ArrayList<Person> aList){
        TextAdapter<Person> textAdapterOne = new TextAdapter<>();
        findViewById(R.id.searchListLayout).setVisibility(View.VISIBLE);
        if(Locale.getDefault().getLanguage().equals("en")) {
            ((TextView) findViewById(R.id.searchDisplay)).setText(getResources().getString(R.string.search_results) + name);
        }
        else if(Locale.getDefault().getLanguage().equals("tr")){
            ((TextView) findViewById(R.id.searchDisplay)).setText(name + " " + getResources().getString(R.string.search_results));
        }
        ListView lView = findViewById(R.id.searchListView);
        lView.setAdapter(textAdapterOne);
        textAdapterOne.setDataList(aList);

        lView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected item text from ListView
            Person selectedPerson = (Person) parent.getItemAtPosition(position);
            Intent i = new Intent(view.getContext(), TreeDisplay.class);
            i.putExtra("name",selectedPerson.getName());
            startActivity(i);
        });
    }

    // ListView methods end

    // graph methods
    public void createGraph(@NonNull ArrayList<Person> aList) {
        Log.d("node", Integer.toString(aList.size()));
        for(int j = 0; j < aList.size(); j++) {
            Graph.Node<Person> newNode = new Graph.Node<>(aList.get(j));
            if(aList.get(j).getName_of_parent() == null) {//this if is for the greatest ancestor as he/she has no known parent
                theGraph.addNode(newNode);
            }else {
                Graph.Node<Person> parentNode = null;

                for (Graph.Node<Person> personNode : theGraph.getNodes()) {
                    if(newNode.getItem().getName_of_parent().equals(personNode.getItem().getName())){
                        parentNode = personNode;
                        Log.d("node", newNode.getItem().getName() + " : " + personNode.getItem().getName());
                    }
                }
                theGraph.addNode(newNode);
                if(parentNode != null) {
                    parentNode.addDestination(newNode, 1);
                    parentNode.getItem().getChildrenList().add(newNode.getItem());
                }
            }
        }
    }

    public ArrayList<Person> checkForSimilarNames(String name, @NonNull ArrayList<Person> aList){
        ArrayList<Person> similarPeople = new ArrayList<>();
        for(Person person : aList){
            if(containsIgnoreCaseRegexp(person.getName(), name)){
                similarPeople.add(person);
            }
        }
        Collections.sort(similarPeople);
        return similarPeople;
    }

    public ArrayList<Person> readPeople(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<Person> aList = new ArrayList<>();

        while (sc.hasNextLine()) {
            String[] temp = sc.nextLine().split(",");
            Person dummyPerson = null;
            if(temp.length == 1) {
                dummyPerson = new Person(temp[0], MainActivity.this);
                aList.add(dummyPerson);
            }else if(temp.length == 2) {
                dummyPerson = new Person(temp[0], temp[1], MainActivity.this);
                aList.add(dummyPerson);
            }else if(temp.length == 3) {
                dummyPerson = new Person(temp[0], temp[1], temp[2], MainActivity.this);
                aList.add(dummyPerson);
            }else if(temp.length == 4) {
                dummyPerson = new Person(temp[0], temp[1], temp[2], temp[3], MainActivity.this);
                aList.add(dummyPerson);
            }
            if(temp.length > 1 && dummyPerson != null) {
                for (int i = aList.size()/2; i < aList.size(); i++) {
                    if(temp[1].equals(aList.get(i).getName())){
                        dummyPerson.setParent(aList.get(i));
                    }
                }
            }
        }
        sc.close();
        return aList;
    }
    // graph methods end

    // permission methods
    private boolean arePermissionsDenied(){
        int p = 0;
        while(p < PERMISSIONS_COUNT){
            //if the permission is not granted, return true
            if(checkSelfPermission(PERMISSIONS[p]) == PackageManager.PERMISSION_DENIED){
                return true;
            }
            p++;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if(requestCode == REQUEST_PERMISSIONS && grantResults.length > 0){
            if(arePermissionsDenied()){
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }else{
                onResume();
            }
        }
    }
    // permission methods end

    // case insensitivity method
    public static boolean containsIgnoreCaseRegexp(String src, String what) {
        return Pattern.compile(Pattern.quote(what), Pattern.CASE_INSENSITIVE)
                .matcher(src).find();
    }
}