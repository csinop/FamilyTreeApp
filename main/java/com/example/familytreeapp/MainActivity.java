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
        initializeAppBasedOnSharedPreferences();
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
    public void initializeAppBasedOnSharedPreferences(){
        SharedPreferences filePathPref = this.getSharedPreferences("filePreferences",MODE_PRIVATE);
        String s = filePathPref.getString("filePath",null);
        if(s != null){
            if(!s.isEmpty()) {
                Log.d("filePath",s);
                setContentView(R.layout.search_layout);
                File newFile = new File(s);
                try {
                    setSearchButtonOnClick(readPeople(newFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setReselectButtonOnClick();
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
        ((ListView) findViewById(R.id.searchListView)).setAdapter(textAdapterOne);
        textAdapterOne.setDataList(aList);
    }


    // ListView methods end

    // graph methods
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

            if(temp.length == 1) {
                Person dummyPerson = new Person(temp[0], MainActivity.this);
                aList.add(dummyPerson);
            }else if(temp.length == 2) {
                Person dummyPerson = new Person(temp[0], temp[1], MainActivity.this);
                aList.add(dummyPerson);
            }else if(temp.length == 3) {
                Person dummyPerson = new Person(temp[0], temp[1], temp[2], MainActivity.this);
                aList.add(dummyPerson);
            }else if(temp.length == 4) {
                Person dummyPerson = new Person(temp[0], temp[1], temp[2], temp[3], MainActivity.this);
                aList.add(dummyPerson);
            }
        }
        sc.close();
        return aList;
    }
    // graph building methods end

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