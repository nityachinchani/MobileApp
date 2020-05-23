package com.example.coen268project.Firebase;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
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

public abstract class FirebaseRepository {

    protected final void fireBaseAuthentication(final FirebaseAuth mFirebaseAuth, String email, String password, final CallBack callback)
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

    protected final void fireBaseCreateAuthenticatedUser(final FirebaseAuth mFirebaseAuth, String email, String password, final CallBack callback)
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

    protected final void fireBaseCreate(final DatabaseReference databaseReference, final Object model, final CallBack callback) {
        databaseReference.keepSynced(true);
        databaseReference.setValue(model, new DatabaseReference.CompletionListener() {
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

    protected final void fireBaseUpdateChildren(final DatabaseReference databaseReference, final Map map, final CallBack callback) {
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

    protected final void fireBaseDelete(final DatabaseReference databaseReference, final CallBack callback) {
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

    protected final void fireBaseReadData(final Query query, final CallBack callback) {
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

    protected final ValueEventListener fireBaseDataChangeListener(final Query query, final CallBack callback) {
        query.keepSynced(true);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        };
        query.addValueEventListener(valueEventListener);
        return valueEventListener;
    }

    protected final void fireBaseOfflineCreate(final DatabaseReference databaseReference, final Object model) {
        try {
            databaseReference.keepSynced(true);
            databaseReference.setValue(model);
        } catch (Exception e) {
        }
    }

    protected final void fireBaseOfflineUpdate(final DatabaseReference databaseReference, final String pushKey, final Object model) {
        try {
            databaseReference.keepSynced(true);
            Map<String, Object> stringObjectMap = new HashMap<>();

            stringObjectMap.put(pushKey, model);
            databaseReference.updateChildren(stringObjectMap);

        } catch (Exception e) {
        }
    }
}
