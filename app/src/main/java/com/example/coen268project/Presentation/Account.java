package com.example.coen268project.Presentation;
import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Model.AccountRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;

public class Account extends FirebaseRepository implements AccountRepository {
    private Activity activity;
    private DatabaseReference accountDatabaseReference;
    private FirebaseAuth accountAuthentication;
    private StorageReference storageReference;

    public Account(Activity activity) {
        this.activity = activity;
        accountDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("accountTable");
        accountAuthentication = FirebaseInstance.AUTHENTICATION;
        storageReference = FirebaseStorage.getInstance().getReference();
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

    public void uploadImageToStorage(String name, Uri contentUri, final CallBack callBack) {
        firebaseUploadImageToStorage(storageReference, name, contentUri, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
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
    public void createAccount(String pushKey, String userName, String email, String phoneNumber, String password, String profilePicture, final CallBack callBack) {
        AccountDao account = new AccountDao(userName, email, phoneNumber, password, profilePicture);
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

    public void getAllAccounts(final CallBack callBack) {
        Query query = accountDatabaseReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<String> accountArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            accountArrayList.add(suggestionSnapshot.child("userName").getValue().toString());
                        }
                        callBack.onSuccess(accountArrayList.toArray());
                    } else
                        callBack.onSuccess(null);
                } else
                    callBack.onSuccess(null);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }
}
