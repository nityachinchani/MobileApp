package com.example.coen268project.View.Video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.R;

import com.example.coen268project.View.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;



public class Calling_Activity extends AppCompatActivity {
    private String buyerID, sellerID,checker="";
    private String sellerUserImage ="",sellerUserName="",buyerUserImage ="",buyerUserName="",callingID="",ringingId="";
    private MediaPlayer mediaPlayer;
    private DatabaseReference userRef;
    private TextView nameContact;
    private ImageView ProfileImage,acceptCallBtn,cancelCallBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        sellerID=getIntent().getStringExtra("sellerId");
        buyerID=getIntent().getStringExtra("buyerId");
        userRef = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("accountTable");
        /*
         * buyer=sender
         * seller=receiver
         * */
        nameContact = findViewById(R.id.name_calling);
        ProfileImage = findViewById(R.id.profile_image_calling);
        acceptCallBtn = findViewById(R.id.make_call);
        cancelCallBtn = findViewById(R.id.make_cancel_call);

        cancelCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="clicked";
               // mediaPlayer.stop();
                cancelCallingUser();
            }
        });

        acceptCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // mediaPlayer.stop();

                final HashMap<String,Object> callingPickUp = new HashMap<>();
                callingPickUp.put("picked","picked");

                userRef.child(buyerID).child("Ringing")
                        .updateChildren(callingPickUp)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Intent intent = new Intent(Calling_Activity.this,Video_Chat_Activity.class);
                                    startActivity(intent);
                                }
                            }
                        });

            }
        });

        getAndSetUserProfileInfo();
    }


    private void getAndSetUserProfileInfo() {


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(sellerID).exists()){
//get and set variables to be solve tomorrow
                    //sellerUserImage = dataSnapshot.child(sellerID).child("image").getValue().toString();
                    //sellerUserImage = dataSnapshot.child(sellerID).child("name").getValue().toString();

                    //nameContact.setText(sellerUserImage);
                    //Picasso.get().load(sellerUserImage).placeholder(R.drawable.profile_image).into(ProfileImage);
                    //My.setImage(customProgressView,receiverUserImage,ProfileImage);
                }

                if(dataSnapshot.child(buyerID).exists()){

                    //buyerUserImage = dataSnapshot.child(buyerID).child("image").getValue().toString();
                    //buyerUserImage = dataSnapshot.child(buyerID).child("name").getValue().toString();

                    // nameContact.setText(receiverUserName);
                    //My.setImage(customProgressView,receiverUserImage,ProfileImage);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //mediaPlayer.start();

        userRef.child(sellerID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!checker.equals("clicked") && !dataSnapshot.hasChild("Calling") && !dataSnapshot.hasChild("Ringing"))
                        {

                            final HashMap<String,Object> callingInfo = new HashMap<>();

                            callingInfo.put("calling",sellerID);

                            userRef.child(buyerID).child("Calling")
                                    .updateChildren(callingInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                final HashMap<String,Object> ringingInfo = new HashMap<>();

                                                ringingInfo.put("ringing",buyerID);

                                                userRef.child(sellerID).child("Ringing")
                                                        .updateChildren(ringingInfo)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()){

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(sellerID).hasChild("Ringing") && !dataSnapshot.child(sellerID).hasChild("Calling")){
                    acceptCallBtn.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child(sellerID).child("Ringing").hasChild("picked"))
                {

                    //mediaPlayer.stop();
                    Intent intent = new Intent(Calling_Activity.this,Video_Chat_Activity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void cancelCallingUser() {

        // from sender side
        userRef.child(buyerID)
                .child("Calling")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("calling")){

                            callingID = dataSnapshot.child("calling").getValue().toString();
                            userRef.child(callingID)
                                    .child("Ringing")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                userRef.child(buyerID)
                                                        .child("Calling")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(Calling_Activity.this, HomeActivity.class));
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }

                                        }
                                    });
                        }
                        else {
                            startActivity(new Intent(Calling_Activity.this,HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        // from receiver side
        userRef.child(buyerID)
                .child("Ringing")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("ringing")){

                            ringingId = dataSnapshot.child("ringing").getValue().toString();
                            userRef.child(ringingId)
                                    .child("Calling")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                userRef.child(buyerID)
                                                        .child("Ringing")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(Calling_Activity.this,HomeActivity.class));
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }

                                        }
                                    });
                        }
                        else {
                            startActivity(new Intent(Calling_Activity.this, HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
