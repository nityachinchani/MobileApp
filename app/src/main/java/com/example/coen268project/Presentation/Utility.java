package com.example.coen268project.Presentation;
import android.net.Uri;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Utility extends FirebaseRepository {
    private StorageReference storageReference;

    public Utility()
    {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static String getCurrentUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int index = user.getEmail().indexOf('@');
        return user.getEmail().substring(0,index);
    }

    public enum ItemStatus
    {
        POSTED("POSTED"),
        SOLD("SOLD"),
        BOOKED("BOOKED");

        private final String name;

        ItemStatus(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }

        public static int getIndex(String name) {
            return ItemStatus.valueOf(name).ordinal();
        }

        public static String[] toArray() {
            ArrayList<String> itemStatusArray = new ArrayList<>();
            for (ItemStatus status: ItemStatus.values()
            ) {
                itemStatusArray.add(status.name);
            }
            return  itemStatusArray.toArray(new String[0]);
        }
    }

    public enum CommunicationType
    {
        TEXT,
        AUDIO,
        VIDEO
    }

    public enum BillingStatus
    {
        PENDING("PENDING"),
        DELIVERED("DELIVERED"),
        RECEIVED("RECEIVED");

        private final String name;

        BillingStatus(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }

        public static String[] toArray() {
            ArrayList<String> billingStatusArray = new ArrayList<>();
            for (BillingStatus status: BillingStatus.values()
            ) {
                billingStatusArray.add(status.name);
            }
            return  billingStatusArray.toArray(new String[0]);
        }
    }

    public enum Category
    {
        CATEGORY1("Sofa and Dining"),
        CATEGORY2("Bed and Wardrobes"),
        CATEGORY3("Home Decor and Garden"),
        CATEGORY4("Kids Furniture"),
        CATEGORY5("Other Household Items");

        private final String name;

        Category(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }

        public static String[] toArray() {
            ArrayList<String> categoryArray = new ArrayList<>();
            for (Category category: Category.values()
                 ) {
                    categoryArray.add(category.name);
            }
            return  categoryArray.toArray(new String[0]);
        }
    }

    public static String getCurrentUserId() { FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    public void uploadImageToStorage(String name, Uri contentUri, final CallBack callBack){
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
}
