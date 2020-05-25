package com.example.coen268project.Presentation;
import android.app.Activity;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Model.ItemRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Item extends FirebaseRepository implements ItemRepository {
    private Activity activity;
    private DatabaseReference itemDatabaseReference;
    private StorageReference storageReference;

    public Item(Activity activity) {
        this.activity = activity;
        itemDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("itemTable");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void createItem(String itemName, String category, String location, String price, String description, String pictureUrl, String sellerId, String buyerId, final CallBack callBack) {
        String pushKey = itemDatabaseReference.push().getKey();
        if (!pushKey.isEmpty()) {
            ItemDao item = new ItemDao(pushKey, itemName, category, location, price, description, pictureUrl, sellerId, buyerId);
            DatabaseReference databaseReference = itemDatabaseReference.child(pushKey);
            firebaseCreate(databaseReference, item, new CallBack() {
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
    public void updateItem(String itemId, HashMap map, final CallBack callBack) {
        if (!itemId.isEmpty()) {
            DatabaseReference databaseReference = itemDatabaseReference.child(itemId);
            firebaseUpdateChildren(databaseReference, map, new CallBack() {
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
    public void deleteItem(String itemId, final CallBack callBack) {
        if (!itemId.isEmpty()) {
            DatabaseReference databaseReference = itemDatabaseReference.child(itemId);
            firebaseDelete(databaseReference, new CallBack() {
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
    public void getItem(String itemId, final CallBack callBack) {
        if (!itemId.isEmpty()) {
            Query query = itemDatabaseReference.child(itemId);
            firebaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            ItemDao account = dataSnapshot.getValue(ItemDao.class);
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
    public void getSpecificItems(String category, String location, CallBack callBack) {

    }

    @Override
    public void getAllItems(final CallBack callBack) {
        Query query = itemDatabaseReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<ItemDao> itemArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            // itemArrayList.add(suggestionSnapshot.child("itemId").getValue().toString());
                            ItemDao item = suggestionSnapshot.getValue(ItemDao.class);
                            itemArrayList.add(item);
                        }
                        callBack.onSuccess(itemArrayList.toArray());
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
