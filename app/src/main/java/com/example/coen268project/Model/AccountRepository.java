package com.example.coen268project.Model;
import com.example.coen268project.Firebase.CallBack;

import java.util.HashMap;

public interface AccountRepository {
    void authenticateAccount(String email, String password, CallBack callback);
    void createUserWithEmailAndPassword(String email, String password, CallBack callback);
    void createAccount(String pushKey, String userName, String email, String phoneNumber, String password, String pictureName, CallBack callBack);
    void getAccount(String uid, CallBack callBack);
    void updateAccount(String accountId, HashMap map, CallBack callBack);
    void validateUser(String email, CallBack callBack);
}
