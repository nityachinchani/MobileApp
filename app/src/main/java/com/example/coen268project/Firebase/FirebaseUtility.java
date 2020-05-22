package com.example.coen268project.Firebase;
import android.app.Activity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtility {
    public static void removeFireBaseChildListener(FirebaseRequestModel firebaseRequestModel) {
        if (firebaseRequestModel != null) {
            if (firebaseRequestModel.getListener() != null && firebaseRequestModel.getQuery() != null) {
                ChildEventListener childEventListener = (ChildEventListener) firebaseRequestModel.getListener();
                firebaseRequestModel.getQuery().removeEventListener(childEventListener);
            }
        }
    }

    public static void removeFireBaseValueEventListener(FirebaseRequestModel firebaseRequestModel) {
        if (firebaseRequestModel != null) {
            if (firebaseRequestModel.getListener() != null && firebaseRequestModel.getQuery() != null) {
                ValueEventListener valueEventListener = (ValueEventListener) firebaseRequestModel.getListener();
                firebaseRequestModel.getQuery().removeEventListener(valueEventListener);
            }
        }
    }

}
