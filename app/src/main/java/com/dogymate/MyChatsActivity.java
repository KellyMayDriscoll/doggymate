package com.dogymate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dogymate.model.ChatUser;
import com.dogymate.util.Cons;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyChatsActivity extends AppCompatActivity {
    ListView listakorisnika;
    TextView nemaniko;
    private ArrayList<ChatUser> chatUsers = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chats);
        listakorisnika=(ListView)findViewById(R.id.korisnickalista);
        nemaniko=(TextView)findViewById(R.id.textView38);

        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Chat");

        /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.hasChildren()){
                        chatUsers = new ArrayList<>();

                        for(DataSnapshot child_snapshot : dataSnapshot.getChildren()){
                            String value = child_snapshot.getValue(String.class);
                            if(null != value && !value.isEmpty()){
                                chatUsers.add(new ChatUser(child_snapshot.getKey(),value.trim()));
                            }
                        }

                        if(chatUsers.size() > 0) {
                            listakorisnika.setVisibility(View.VISIBLE);
                            nemaniko.setVisibility(View.GONE);
                            listakorisnika.setAdapter(new ArrayAdapter<ChatUser>(MyChatsActivity.this, android.R.layout.simple_list_item_1, chatUsers.toArray(new ChatUser[chatUsers.size()])));
                        }
                        else
                        {
                            listakorisnika.setVisibility(View.GONE);
                            nemaniko.setVisibility(View.VISIBLE);
                            nemaniko.setText("You don't have any chats");
                        }
                    }
                }
                AddChildEventListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AddChildEventListener();
            }
        });*/
        AddChildEventListener();

        listakorisnika.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatUser chatUser = (ChatUser) parent.getItemAtPosition(position);
                startActivity(new Intent(MyChatsActivity.this, Chat.class).putExtra(Cons.RECEIVER_ID,chatUser.getUser_id()).putExtra(Cons.RECEIVER_NAME,chatUser.getName()));
            }
        });
    }

    private void AddChildEventListener() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                if (null != value && !value.isEmpty()) {
                    chatUsers.add(new ChatUser(dataSnapshot.getKey(), value.trim()));
                    if(chatUsers.size() > 0) {
                        listakorisnika.setVisibility(View.VISIBLE);
                        nemaniko.setVisibility(View.GONE);
                        listakorisnika.setAdapter(new ArrayAdapter<ChatUser>(MyChatsActivity.this, android.R.layout.simple_list_item_1, chatUsers.toArray(new ChatUser[chatUsers.size()])));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
