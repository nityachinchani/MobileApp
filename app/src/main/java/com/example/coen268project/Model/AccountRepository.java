package com.example.coen268project.Model;
import com.example.coen268project.Firebase.CallBack;
import com.google.firebase.storage.StorageReference;

public interface AccountRepository {
    void authenticateAccount(String email, String password, CallBack callback);
    void createUserWithEmailAndPassword(String email, String password, CallBack callback);
    void createAccount(String pushKey, String userName, String email, String phoneNumber, String password, CallBack callBack);
}
