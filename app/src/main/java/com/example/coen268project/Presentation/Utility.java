package com.example.coen268project.Presentation;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class Utility extends FirebaseRepository {
    private StorageReference storageReference;
    public static String PROFILE = "Profile";
    public static String ITEM = "Item";

    private static String currentUserId;

    public Utility()
    {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        //return currentUserId;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    public static void setCurrentUserId(String currentUserId) {
        Utility.currentUserId = currentUserId;
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

        Category(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
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

    public StorageReference getProfilePicture()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        StorageReference images = storageReference.child("images/");
        final StorageReference[] reference = new StorageReference[1];
        images.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                          if(item.getName().startsWith(uid + "_" + PROFILE)) {
                              reference[0] = FirebaseStorage.getInstance().getReference().child(item.getPath());
                          }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                /*Glide.with(this)
                .load(storageReference)
                .into(image);*/
            return reference[0];
    }

    public StorageReference getItemPicture()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        StorageReference images = storageReference.child("images/");
        final StorageReference[] reference = new StorageReference[1];
        images.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            if(item.getName().startsWith(uid + "_" + ITEM)) {
                                reference[0] = FirebaseStorage.getInstance().getReference().child(item.getPath());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                /*Glide.with(this)
                .load(storageReference)
                .into(image);*/
        return reference[0];
    }
}
