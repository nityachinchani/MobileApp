package com.example.coen268project.Presentation;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Model.AccountRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Account extends FirebaseRepository implements AccountRepository {
    private DatabaseReference accountDatabaseReference;
    private FirebaseAuth accountAuthentication;

    public Account() {
        accountDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("accountTable");
        accountAuthentication = FirebaseInstance.AUTHENTICATION;
    }

    @Override
    public void authenticateAccount(String email, String password, final CallBack callBack) {
        if (!email.isEmpty() && !password.isEmpty()) {
            firebaseAuthentication(accountAuthentication, email, password, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    callBack.onSuccess(FirebaseConstants.SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FirebaseConstants.FAIL);
        }
    }

    @Override
    public void createUserWithEmailAndPassword(String email, String password, final CallBack callBack) {
        if (!email.isEmpty() && !password.isEmpty()) {
            firebaseCreateAuthenticatedUser(accountAuthentication, email, password, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                   callBack.onSuccess(object);
                }

                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FirebaseConstants.FAIL);
        }
    }

    @Override
    public void createAccount(String pushKey, String userName, String email, String phoneNumber, String password, final CallBack callBack) {
        AccountDao account = new AccountDao(userName, email, phoneNumber, password);
        if (account != null && !pushKey.isEmpty()) {
            DatabaseReference databaseReference = accountDatabaseReference.child(pushKey);
            firebaseCreate(databaseReference, account, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    callBack.onSuccess(FirebaseConstants.SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FirebaseConstants.FAIL);
        }
    }
}
