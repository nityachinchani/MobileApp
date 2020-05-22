package com.example.coen268project.Presentation;
import android.app.Activity;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseChildCallback;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Firebase.FirebaseRequestModel;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Model.AccountRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        accountDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.ACCOUNT_TABLE);
        accountAuthentication = FirebaseInstance.AUTHENTICATION;
    }

    @Override
    public void authenticateAccount(String email, String password, final CallBack callBack) {
        accountAuthentication = FirebaseInstance.AUTHENTICATION;
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
        accountAuthentication = FirebaseInstance.AUTHENTICATION;
        if (!email.isEmpty() && !password.isEmpty()) {
            fireBaseCreateAuthenticatedUser(accountAuthentication, email, password, new CallBack() {
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
    public void createAccount(AccountDao account, final CallBack callBack) {
        String pushKey = accountDatabaseReference.push().getKey();
        if (account != null && !pushKey.isEmpty()) {
            account.setUserName(pushKey);
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
    public void deleteEmployee(String userName, final CallBack callBack) {
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
        Query query = accountDatabaseReference.orderByChild(FirebaseConstants.USER_NAME);
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<AccountDao> accountArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            AccountDao account = suggestionSnapshot.getValue(AccountDao.class);
                            accountArrayList.add(account);
                        }
                        callBack.onSuccess(accountArrayList);
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

    @Override
    public FirebaseRequestModel getAllAccountsByDataChangeEvent(final CallBack callBack) {
        Query query = accountDatabaseReference.orderByChild(FirebaseConstants.USER_NAME);
        ValueEventListener valueEventListener = fireBaseDataChangeListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<AccountDao> accountArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            AccountDao account = suggestionSnapshot.getValue(AccountDao.class);
                            accountArrayList.add(account);
                        }
                        callBack.onSuccess(accountArrayList);
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
        return new FirebaseRequestModel(valueEventListener, query);
    }

    @Override
    public FirebaseRequestModel getAllAccountByChildEvent(final FirebaseChildCallback firebaseChildCallBack) {
        Query query = accountDatabaseReference.orderByChild(FirebaseConstants.USER_NAME);
        ChildEventListener childEventListener = fireBaseChildEventListener(query, new FirebaseChildCallback() {
                    @Override
                    public void onChildAdded(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            AccountDao account = dataSnapshot.getValue(AccountDao.class);
                            firebaseChildCallBack.onChildAdded(account);
                        }
                    }

                    @Override
                    public void onChildChanged(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            AccountDao account = dataSnapshot.getValue(AccountDao.class);
                            firebaseChildCallBack.onChildChanged(account);
                        }
                    }

                    @Override
                    public void onChildRemoved(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            AccountDao account = dataSnapshot.getValue(AccountDao.class);
                            firebaseChildCallBack.onChildRemoved(account);
                        }
                    }

                    @Override
                    public void onChildMoved(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            AccountDao account = dataSnapshot.getValue(AccountDao.class);
                            firebaseChildCallBack.onChildMoved(account);
                        }
                    }

                    @Override
                    public void onCancelled(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            AccountDao account = dataSnapshot.getValue(AccountDao.class);
                            firebaseChildCallBack.onCancelled(account);
                        }
                    }
                }
        );
        query.addChildEventListener(childEventListener);
        return new FirebaseRequestModel(childEventListener, query);
    }
}
