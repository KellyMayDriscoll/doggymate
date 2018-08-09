package com.dogymate;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dogymate.util.Cons;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1,reference2;
    private DatabaseReference databaseReference2,databaseReference3,databaseNotification;
    //private DatabaseReference databaseReference;
    private FirebaseUser user;

    private String push_token;
    private String receiver_id = "";
    private String receiver_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity);

        if(getIntent() != null){
            push_token = getIntent().getStringExtra("push_token");
            receiver_id = getIntent().getStringExtra(Cons.RECEIVER_ID);
            receiver_name = getIntent().getStringExtra(Cons.RECEIVER_NAME);
        }


        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference2= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseNotification=FirebaseDatabase.getInstance().getReference("notifications");

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = FirebaseDatabase.getInstance().getReference("messages").child(user.getUid() + "_" + receiver_id);
        reference2 = FirebaseDatabase.getInstance().getReference("messages").child( receiver_id + "_" + user.getUid());
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){

                    //sending push notification
                    if (null != push_token && !push_token.isEmpty()) {
                        new SendPushMessage(push_token, messageText).execute();
                    }

                    if (!receiver_id.isEmpty()) {
                        FirebaseDatabase.getInstance().getReference("Users").child(receiver_id).child("unreaded_message").setValue(true);
                    }

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", user.getDisplayName());
                    map.put("sender_id", user.getUid());
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        /*reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for(com.google.firebase.database.DataSnapshot child_snapshot : dataSnapshot.getChildren()){
                        System.out.println(child_snapshot.getValue().toString());
                        String message = child_snapshot.child("message").getValue().toString();
                        String userName = child_snapshot.child("user").getValue().toString();
                        String sender_id = child_snapshot.child("sender_id").getValue().toString();

                        if(sender_id.equals(user.getUid())){
                            addMessageBox("You:\n" + message, 1);
                        }
                        else{
                            addMessageBox(userName + ":\n" + message, 2);
                        }
                    }
                }
                databaseReference2.child("unreaded_message").setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {
                String message = dataSnapshot.child("message").getValue().toString();
                String userName = dataSnapshot.child("user").getValue().toString();
                String sender_id = dataSnapshot.child("sender_id").getValue().toString();

                if(sender_id.equals(user.getUid())){
                    addMessageBox("You:\n" + message, 1);
                }
                else{
                    addMessageBox(userName + ":\n" + message, 2);
                }
                databaseReference2.child("unreaded_message").setValue(false);

                System.out.println("onChildAdded");
            }

            @Override
            public void onChildChanged(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!receiver_id.isEmpty()){
            FirebaseDatabase.getInstance().getReference("Users").child(receiver_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("push_token")){
                        push_token = dataSnapshot.child("push_token").getValue().toString();
                    }

                    if(dataSnapshot.hasChild("username")){
                        String name = dataSnapshot.child("username").getValue().toString();
                        databaseReference2.child("Chat").child(receiver_id).setValue(name);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    private class SendPushMessage extends AsyncTask<String,Integer,Boolean> {
        private String message;
        private String push_token;

        SendPushMessage(String topic, String message) {
            this.push_token = topic;
            this.message = message;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject json = new JSONObject();
                JSONObject notificationObject = new JSONObject();
                notificationObject.put("body", message);
                notificationObject.put("title", getString(R.string.app_name));
                json.put("notification", notificationObject);

                JSONObject dataObject = new JSONObject();
                dataObject.put("body", "Firebase Message Data Testing");
                dataObject.put("title", getString(R.string.app_name));
                json.put("data", dataObject);

                //json.put("to", "/topics/" + topic);
                json.put("to",push_token);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                Request request = new Request.Builder()
                        .header("Authorization", "key=" + getString(R.string.LEGACY_SERVER_KEY))
                        .url("https://fcm.googleapis.com/fcm/send")
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String finalResponse = response.body().string();
                System.out.println(finalResponse);
            } catch (Exception e) {
                //Log.d(TAG,e+"");
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                //Toast.makeText(Chat.this, "Notifications sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
}