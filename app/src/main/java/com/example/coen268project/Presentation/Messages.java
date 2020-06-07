package com.example.coen268project.Presentation;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseChildCallback;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Firebase.FirebaseRequestModel;
import com.example.coen268project.Model.MessagesDao;
import com.example.coen268project.Model.MessagesRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Messages extends FirebaseRepository implements MessagesRepository {
    private DatabaseReference allMessagesDatabaseReference;

    public Messages()
    {
        allMessagesDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("messageTable");
    }

    public void setBuyerName(String sellerId, String buyerId,final String buyerName, final CallBack callBack)
    {
        final DatabaseReference messageReference = allMessagesDatabaseReference.child(sellerId).child(buyerId);
        firebaseReadData(messageReference, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                DataSnapshot dataSnapshot = (DataSnapshot) object;
                if (!dataSnapshot.hasChild("buyerName")) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("buyerName", buyerName);
                    firebaseCreate(messageReference, map, new CallBack() {
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
                else
                    callBack.onSuccess(null);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }
    @Override
    public void createMessage(String sellerId, String buyerId, String uid, String name, String messageContent, final CallBack callBack) {
        MessagesDao messagesDao = new MessagesDao(uid, name, messageContent);
        final DatabaseReference messageReference = allMessagesDatabaseReference.child(sellerId).child(buyerId);
        String pushKey = messageReference.push().getKey();
        if (messagesDao != null && !pushKey.isEmpty()) {
            DatabaseReference databaseReference = messageReference.child(pushKey);
            firebaseCreate(databaseReference, messagesDao, new CallBack() {
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
    public void getMessage(String sellerId, String buyerId, String messageId, final CallBack callBack) {
        final MessagesDao[] messagesDao = new MessagesDao[1];
        DatabaseReference messageReference = allMessagesDatabaseReference.child(sellerId).child(buyerId);
        if (!messageId.isEmpty()) {
            Query query = messageReference.child(messageId);
            firebaseReadData(query, new CallBack() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String messageContent = dataSnapshot.child("messageContent").getValue().toString();
                            messagesDao[0] = new MessagesDao(uid, name, messageContent);
                        }
                        callBack.onSuccess(messagesDao[0]);
                    } else {
                        callBack.onSuccess(null);
                    }
                }

                @Override
                public void onError(Object object) {
                    callBack.onError(null);
                }
            });
        }

    }

    @Override
    public void getAllMessages(String sellerId, String buyerId, final CallBack callBack) {
        final ArrayList<MessagesDao> messageArrayList = new ArrayList<>();
        DatabaseReference messageReference = allMessagesDatabaseReference.child(sellerId).child(buyerId);
        Query query = messageReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            if(suggestionSnapshot.hasChildren()) {
                                String uid = suggestionSnapshot.child("uid").getValue().toString();
                                String name = suggestionSnapshot.child("name").getValue().toString();
                                String messageContent = suggestionSnapshot.child("messageContent").getValue().toString();
                                MessagesDao messagesDao = new MessagesDao(uid, name, messageContent);
                                messageArrayList.add(messagesDao);
                            }
                        }
                        if(messageArrayList.size() > 0) {
                            callBack.onSuccess(messageArrayList.toArray());
                        }
                        else
                            callBack.onSuccess(null);
                    } else {
                        callBack.onSuccess(null);
                    }
                } else {
                    callBack.onSuccess(null);
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(null);
            }
        });
    }

    @Override
    public void getAllBuyers(String sellerId, final CallBack callBack) {
        final HashMap<String, String> buyerMap = new HashMap<>();
        DatabaseReference messageReference = allMessagesDatabaseReference.child(sellerId);
        Query query = messageReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            String key = suggestionSnapshot.getKey();
                            String value = suggestionSnapshot.child("buyerName").getValue().toString();
                            buyerMap.put(key, value);
                        }
                        callBack.onSuccess(buyerMap);
                    } else {
                        callBack.onSuccess(null);
                    }
                } else {
                    callBack.onSuccess(null);
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(null);
            }
        });
    }

    @Override
    public FirebaseRequestModel getAllMessageByChildEvent(String sellerId, String buyerId, final FirebaseChildCallback firebaseChildCallBack) {
        final MessagesDao[] messagesDao = new MessagesDao[1];
        Query query = allMessagesDatabaseReference.child(sellerId).child(buyerId).orderByKey();
        ChildEventListener childEventListener = fireBaseChildEventListener(query, new FirebaseChildCallback() {
                    @Override
                    public void onChildAdded(Object object) {
                            if (object != null) {
                                DataSnapshot dataSnapshot = (DataSnapshot) object;
                                if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                                    String uid = dataSnapshot.child("uid").getValue().toString();
                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String messageContent = dataSnapshot.child("messageContent").getValue().toString();
                                    messagesDao[0] = new MessagesDao(uid, name, messageContent);
                                    firebaseChildCallBack.onChildAdded(messagesDao[0]);
                                }
                            } else {
                                firebaseChildCallBack.onChildAdded(null);
                            }
                    }

                    @Override
                    public void onChildChanged(Object object) {

                    }

                    @Override
                    public void onChildRemoved(Object object) {

                    }

                    @Override
                    public void onChildMoved(Object object) {

                    }

                    @Override
                    public void onCancelled(Object object) {

                    }
                }
        );
        query.addChildEventListener(childEventListener);
        return new FirebaseRequestModel(childEventListener, query);
    }
}
