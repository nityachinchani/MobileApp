package com.example.coen268project.Presentation;
import android.net.Uri;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Utility extends FirebaseRepository {
    private StorageReference storageReference;

    public Utility()
    {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static enum ItemStatus
    {
        POSTED,
        SOLD,
        BOOKED
    }

    public static enum CommunicationType
    {
        TEXT,
        AUDIO,
        VIDEO
    }

    public static enum BillingStatus
    {
        PENDING,
        DELIVERED,
        RECEIVED
    }

    public static enum Category
    {
        CATEGORY1("Sofa and Dining"),
        CATEGORY2("Bed and Wardrobes"),
        CATEGORY3("Home Decor and Garden"),
        CATEGORY4("Kids Furniture"),
        CATEGORY5("Other Household Items");

        private final String name;

        private Category(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
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
}
