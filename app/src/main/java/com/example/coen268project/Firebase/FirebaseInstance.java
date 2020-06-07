package com.example.coen268project.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseInstance {
    private FirebaseInstance() {
    }
    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    public static final FirebaseAuth AUTHENTICATION = FirebaseAuth.getInstance();
}
