package com.example.coen268project.Presentation;
import android.app.Activity;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Model.AccountRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Account extends FirebaseRepository implements AccountRepository {
    private Activity activity;
    private DatabaseReference accountDatabaseReference;
    private FirebaseAuth accountAuthentication;

    public Account(Activity activity) {
        this.activity = activity;
        accountDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("accountTable");
        accountAuthentication = FirebaseInstance.AUTHENTICATION;
    }

    @Override
    public void authenticateAccount(String email, String password, final CallBack callBack) {
        if (!email.isEmpty() && !password.isEmpty()) {
            fireBaseAuthentication(accountAuthentication, email, password, new CallBack() {
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
            fireBaseCreateAuthenticatedUser(accountAuthentication, email, password, new CallBack() {
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
            fireBaseCreate(databaseReference, account, new CallBack() {
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
    public void updateAccount(String userName, HashMap map, final CallBack callBack) {
        if (!userName.isEmpty()) {
            DatabaseReference databaseReference = accountDatabaseReference.child(userName);
            fireBaseUpdateChildren(databaseReference, map, new CallBack() {
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
    public void deleteAccount(String userName, final CallBack callBack) {
        if (!userName.isEmpty()) {
            DatabaseReference databaseReference = accountDatabaseReference.child(userName);
            fireBaseDelete(databaseReference, new CallBack() {
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
    public void getAccount(String userName, final CallBack callBack) {
        if (!userName.isEmpty()) {
            Query query = accountDatabaseReference.child(userName);
            fireBaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            AccountDao account = dataSnapshot.getValue(AccountDao.class);
                            callBack.onSuccess(account);
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
        } else {
            callBack.onError(FirebaseConstants.FAIL);
        }
    }

    @Override
    public void getAllAccounts(final CallBack callBack) {
        Query query = accountDatabaseReference.orderByKey();
        fireBaseReadData(query, new CallBack() {
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
