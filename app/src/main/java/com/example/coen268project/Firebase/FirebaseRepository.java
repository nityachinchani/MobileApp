package com.example.coen268project.Firebase;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.firebase.storage.FirebaseStorage;
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

    protected void firebaseUploadImageToStorage(final StorageReference storageReference, String name, final Uri contentUri, final CallBack callback){
        final StorageReference image = storageReference.child("images/"+name);
        final UploadTask uploadTask = image.putFile(contentUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                callback.onError(e.getMessage());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(
                    UploadTask.TaskSnapshot taskSnapshot) {
                double progress
                        = (100.0
                        * taskSnapshot.getBytesTransferred()
                        / taskSnapshot.getTotalByteCount());
                callback.onError((int)progress);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       callback.onSuccess(uri.toString());
                    }
                });
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

    /**
     * Fetch data with child event listener
     *
     * @param query                 to add childEvent listener
     * @param firebaseChildCallBack callback for event handling
     * @return ChildEventListener
     */
    protected final ChildEventListener fireBaseChildEventListener(final Query query, final FirebaseChildCallback firebaseChildCallBack) {
        query.keepSynced(true);
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                firebaseChildCallBack.onChildAdded(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                firebaseChildCallBack.onChildChanged(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                firebaseChildCallBack.onChildRemoved(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                firebaseChildCallBack.onChildMoved(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseChildCallBack.onCancelled(databaseError);
            }
        };
    }

    /**
     * Fetch data with Value event listener
     *
     * @param query    to add childEvent listener
     * @param callback callback for event handling
     * @return ValueEventListener reference
     */
    protected final void firebaseValidateUser(final Query query, final CallBack callback) {
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    callback.onSuccess(true);
                }
                else {
                    callback.onSuccess(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

}
