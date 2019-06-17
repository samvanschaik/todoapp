package com.hsleiden.todoapp;

import com.google.firebase.database.FirebaseDatabase;

public class Utils {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase(){
        if(database == null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
