package com.example.coen268project.Presentation;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Model.ItemRepository;
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
    public void createItem(String sellerId, String itemName, String category, String location, String price, String description, String pictureName, final CallBack callBack) {
        String pushKey = itemDatabaseReference.push().getKey();
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
    public void getItem(final String itemId, final CallBack callBack) {
        final ItemDao[] itemDao = new ItemDao[1];
        if (!itemId.isEmpty()) {
            Query query = itemDatabaseReference.child(itemId);
            firebaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            String itemId = dataSnapshot.getKey();
                            String itemName = dataSnapshot.child("itemName").getValue().toString();
                            String category = dataSnapshot.child("category").getValue().toString();
                            String location = dataSnapshot.child("location").getValue().toString();
                            String price = dataSnapshot.child("price").getValue().toString();
                            String description = dataSnapshot.child("description").getValue().toString();
                            String sellerId = dataSnapshot.child("sellerId").getValue().toString();
                            String buyerId = dataSnapshot.child("buyerId").getValue().toString();
                            String itemStatus = dataSnapshot.child("itemStatus").getValue().toString();
                            String pictureName = dataSnapshot.child("pictureName").getValue().toString();
                            itemDao[0] = new ItemDao(itemId, itemName, category, location, price, description, sellerId, buyerId, itemStatus, pictureName);
                        }
                        callBack.onSuccess(itemDao[0]);
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
    public void getItemsByCategory(String category, final CallBack callBack) {
        final ArrayList<ItemDao> itemArrayList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByChild("category").equalTo(category);

        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            String itemId = suggestionSnapshot.getKey();
                            String itemName = suggestionSnapshot.child("itemName").getValue().toString();
                            String category = suggestionSnapshot.child("category").getValue().toString();
                            String location = suggestionSnapshot.child("location").getValue().toString();
                            String price = suggestionSnapshot.child("price").getValue().toString();
                            String description = suggestionSnapshot.child("description").getValue().toString();
                            String sellerId = suggestionSnapshot.child("sellerId").getValue().toString();
                            String buyerId = suggestionSnapshot.child("buyerId").getValue().toString();
                            String itemStatus = suggestionSnapshot.child("itemStatus").getValue().toString();
                            String pictureName = suggestionSnapshot.child("pictureName").getValue().toString();
                            ItemDao itemDao = new ItemDao(itemId, itemName, category, location, price, description, sellerId, buyerId, itemStatus, pictureName);
                            itemArrayList.add(itemDao);
                        }
                        callBack.onSuccess(itemArrayList.toArray());
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
    public void getItemsByLocation(String location, final CallBack callBack) {
        final ArrayList<ItemDao> itemArrayList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByChild("location").equalTo(location);

        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            String itemId = suggestionSnapshot.getKey();
                            String itemName = suggestionSnapshot.child("itemName").getValue().toString();
                            String category = suggestionSnapshot.child("category").getValue().toString();
                            String location = suggestionSnapshot.child("location").getValue().toString();
                            String price = suggestionSnapshot.child("price").getValue().toString();
                            String description = suggestionSnapshot.child("description").getValue().toString();
                            String sellerId = suggestionSnapshot.child("sellerId").getValue().toString();
                            String buyerId = suggestionSnapshot.child("buyerId").getValue().toString();
                            String itemStatus = suggestionSnapshot.child("itemStatus").getValue().toString();
                            String pictureName = suggestionSnapshot.child("pictureName").getValue().toString();
                            ItemDao itemDao = new ItemDao(itemId, itemName, category, location, price, description, sellerId, buyerId, itemStatus, pictureName);
                            itemArrayList.add(itemDao);
                        }
                        callBack.onSuccess(itemArrayList.toArray());
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





    public void getMyItems(String uid, final CallBack callBack) {
        final ArrayList<ItemDao> itemArrayList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByChild("sellerId").equalTo(uid);
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            String itemId = suggestionSnapshot.getKey();
                            String itemName = suggestionSnapshot.child("itemName").getValue().toString();
                            String category = suggestionSnapshot.child("category").getValue().toString();
                            String location = suggestionSnapshot.child("location").getValue().toString();
                            String price = suggestionSnapshot.child("price").getValue().toString();
                            String description = suggestionSnapshot.child("description").getValue().toString();
                            String sellerId = suggestionSnapshot.child("sellerId").getValue().toString();
                            String buyerId = suggestionSnapshot.child("buyerId").getValue().toString();
                            String itemStatus = suggestionSnapshot.child("itemStatus").getValue().toString();
                            String pictureName = suggestionSnapshot.child("pictureName").getValue().toString();
                            ItemDao itemDao = new ItemDao(itemId, itemName, category, location, price, description, sellerId, buyerId, itemStatus, pictureName);
                            itemArrayList.add(itemDao);
                        }
                        callBack.onSuccess(itemArrayList.toArray());
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
    public void getAllItems(final CallBack callBack) {
        final ArrayList<ItemDao> itemArrayList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            String itemId = suggestionSnapshot.getKey();
                            String itemName = suggestionSnapshot.child("itemName").getValue().toString();
                            String category = suggestionSnapshot.child("category").getValue().toString();
                            String location = suggestionSnapshot.child("location").getValue().toString();
                            String price = suggestionSnapshot.child("price").getValue().toString();
                            String description = suggestionSnapshot.child("description").getValue().toString();
                            String sellerId = suggestionSnapshot.child("sellerId").getValue().toString();
                            String buyerId = suggestionSnapshot.child("buyerId").getValue().toString();
                            String itemStatus = suggestionSnapshot.child("itemStatus").getValue().toString();
                            String pictureName = suggestionSnapshot.child("pictureName").getValue().toString();
                            ItemDao itemDao = new ItemDao(itemId, itemName, category, location, price, description, sellerId, buyerId, itemStatus, pictureName);
                            itemArrayList.add(itemDao);
                        }
                        callBack.onSuccess(itemArrayList.toArray());
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
    public void getAllLocations(final CallBack callBack) {
        final ArrayList<String> locationList = new ArrayList<>();
        Query query = itemDatabaseReference.orderByKey();
        firebaseReadData(query, new CallBack() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            locationList.add(suggestionSnapshot.child("location").getValue().toString());
                        }
                        callBack.onSuccess(locationList.stream().distinct().toArray());
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
}
