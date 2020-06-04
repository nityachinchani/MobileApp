package com.example.coen268project.Firebase;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public abstract class FirebaseRepository {
    protected final void firebaseAuthentication(final FirebaseAuth mFirebaseAuth, String email, String password, final CallBack callback)
    {
        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onSuccess(null);
                }
                else {
                    callback.onError(task);
                }
            }
        });
    }

    protected final void firebaseCreateAuthenticatedUser(final FirebaseAuth mFirebaseAuth, String email, String password, final CallBack callback)
    {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onSuccess(mFirebaseAuth.getCurrentUser().getUid());
                }
                else {
                    callback.onError(task);
                }
            }
        });
    }

    protected void firebaseUploadImageToStorage(final StorageReference storageReference, String name, Uri contentUri, final CallBack callback){
        StorageReference image = storageReference.child("images/"+name);
        UploadTask uploadTask = image.putFile(contentUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public  void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                callback.onSuccess(urlTask.getResult().toString());
            }
        });
    }

    protected final void firebaseCreate(final DatabaseReference databaseReference, final Object model, final CallBack callback) {
        databaseReference.keepSynced(true);
        databaseReference.setValue(model, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onSuccess(databaseReference.getKey());
                } else {
                    callback.onError(databaseError);
                }
            }
        });
    }

    protected final void firebaseUpdateChildren(final DatabaseReference databaseReference, final Map map, final CallBack callback) {
        databaseReference.keepSynced(true);
        databaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onSuccess(FirebaseConstants.SUCCESS);
                } else {
                    callback.onError(databaseError);
                }
            }
        });
    }

    protected final void firebaseDelete(final DatabaseReference databaseReference, final CallBack callback) {
        databaseReference.keepSynced(true);
        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(databaseError);
                }
            }
        });
    }

    protected final void firebaseReadData(final Query query, final CallBack callback) {
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

}
