package com.example.coen268project.View.Video;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.R;
import com.example.coen268project.View.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Video_Chat_Activity extends AppCompatActivity  implements Session.SessionListener,
        PublisherKit.PublisherListener {

    private static String API_Key ="46777024";
    private static String SESSION_ID = "2_MX40Njc3NzAyNH5-MTU5MTI2Njk1NDc0NH5wS3FwaXFXOEYvMktEbUVaeE1LSHNWRnR-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00Njc3NzAyNCZzaWc9M2FiYTY0NDcxYjJhMjRhMDgzZWJjOGZjMDE2NWNlMDJkYjQ5OWY4ZTpzZXNzaW9uX2lkPTJfTVg0ME5qYzNOekF5Tkg1LU1UVTVNVEkyTmprMU5EYzBOSDV3UzNGd2FYRlhPRVl2TWt0RWJVVmFlRTFMU0hOV1JuUi1mZyZjcmVhdGVfdGltZT0xNTkxMjY3MDIwJm5vbmNlPTAuMTU1MDM0MzkxMDQ3ODA5MTQmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU5Mzg1OTAxOSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static String LOG_TAG = Video_Chat_Activity.class.getSimpleName();
    private static final  int RC_VIDEO_APP_PERMISSION = 124;
    private DatabaseReference userRef;
    private String userId = "";
    private ImageView closeVideoChatBtn;
    private FrameLayout mPublisherViewController,mSubscriberViewController;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userRef = FirebaseInstance.DATABASE.getReference(FirebaseConstants.DATABASE_ROOT).child("accountTable");

        closeVideoChatBtn = findViewById(R.id.close_video_chat_btn);
        mPublisherViewController = findViewById(R.id.publisher_container);
        mSubscriberViewController = findViewById(R.id.subscriber_contener);


        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(userId).hasChild("Ringing")){

                            userRef.child(userId).child("Ringing").removeValue();

                            if(mPublisher!=null){
                                mPublisher.destroy();
                            }

                            if(mSubscriber!=null){
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(Video_Chat_Activity.this, HomeActivity.class));
                            finish();
                        }

                        if(dataSnapshot.child(userId).hasChild("Calling")){

                            userRef.child(userId).child("Calling").removeValue();

                            if(mPublisher!=null){
                                mPublisher.destroy();
                            }

                            if(mSubscriber!=null){
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(Video_Chat_Activity.this,HomeActivity.class));
                            finish();
                        }
                        else {

                            if(mPublisher!=null){
                                mPublisher.destroy();
                            }

                            if(mSubscriber!=null){
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(Video_Chat_Activity.this,HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        requestPermissions();

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERMISSION)
    private void requestPermissions(){

        String[] perms ={Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(Video_Chat_Activity.this,perms))
        {
            mSession = new Session.Builder(this,API_Key,SESSION_ID).build();
            mSession.setSessionListener(Video_Chat_Activity.this);

            mSession.connect(TOKEN);
        }
        else {
            EasyPermissions.requestPermissions(this,"Hey this app needs Mic and Camera permission, please allow.",RC_VIDEO_APP_PERMISSION,perms);
        }

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG,"Session connected");

        mPublisher = new Publisher.Builder(Video_Chat_Activity.this).build();
        mPublisher.setPublisherListener(Video_Chat_Activity.this);

        mPublisherViewController.addView(mPublisher.getView());

        if(mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream Received");

        if(mSubscriber==null){
            mSubscriber = new Subscriber.Builder(this,stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewController.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream drop");

        if(mSubscriber!=null){
            mSubscriber =null;
            mSubscriberViewController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }
}
