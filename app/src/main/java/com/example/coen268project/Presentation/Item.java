package com.example.coen268project.Presentation;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Model.ItemRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class Item extends FirebaseRepository implements ItemRepository {
    private DatabaseReference itemDatabaseReference;

    public Item() {
        itemDatabaseReference = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("itemTable");
    }

    @Override
    public void createItem(String itemName, String category, String location, String price, String description, String pictureName, final CallBack callBack) {
        String pushKey = itemDatabaseReference.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String sellerId = user.getUid();
        if (!pushKey.isEmpty()) {
            ItemDao item = new ItemDao(pushKey, itemName, category, location, price, description, sellerId, "", Utility.ItemStatus.POSTED.toString(), pictureName);
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
    public ItemDao getItem(String itemId) {
        final ItemDao[] item = {null};
        if (!itemId.isEmpty()) {
            Query query = itemDatabaseReference.child(itemId);
            firebaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            ItemDao itemDao = dataSnapshot.getValue(ItemDao.class);
                            item[0] = itemDao;
                        } else
                            item[0] = null;
                    } else
                        item[0] = null;
                }

                @Override
                public void onError(Object object) {
                    item[0] = null;
                }
            });
        } else {
            item[0] = null;
        }

        return item[0];
    }

    @Override
    public ArrayList<ItemDao> getSpecificItems(String category, String location) {
        final ArrayList<ItemDao> itemArrayList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByChild("category").equalTo(category).orderByChild("location").equalTo(location);
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            itemArrayList.add(suggestionSnapshot.getValue(ItemDao.class));
                        }
                    }
                }
            }

            @Override
            public void onError(Object object) {
                itemArrayList.add(null);
            }
        });
        return itemArrayList;
    }

    @Override
    public ArrayList<ItemDao> getAllItems() {
        final ArrayList<ItemDao> itemArrayList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                           itemArrayList.add(suggestionSnapshot.getValue(ItemDao.class));
                        }
                    }
                }
            }

            @Override
            public void onError(Object object) {
                itemArrayList.add(null);
            }
        });
        return itemArrayList;
    }

    @Override
    public ArrayList<String> getAllLocations() {
        final ArrayList<String> locationList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            locationList.add(suggestionSnapshot.child("location").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onError(Object object) {
                locationList.add(null);
            }
        });
        return locationList;
    }
}
