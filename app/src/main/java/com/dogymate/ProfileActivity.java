package com.dogymate;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dogymate.util.Cons;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    private TextView txtAbout,txtOwnersName,txtCharacter,txtBreed,txtSex,txtAge,txtSprayed;
    private DatabaseReference databaseReference2;
    private FirebaseUser user;
    private ImageButton sendMessage,kuca;
    private Button srcagen;
    private ImageView imageView,sex_image;
    private String ima;
    private String user_name,kmei,kchar,kseks,kbrid,kejdz,kabaut,ksprejd,kvak;
    private ArrayList<String> mojkurac = new ArrayList<>();
    private String receiver_push_token = "";
    private String receiver_id="";
    private Boolean self_profile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(null != getIntent()){
            receiver_id = getIntent().getStringExtra(Cons.RECEIVER_ID);
            self_profile = getIntent().getBooleanExtra(Cons.SELF_PROFILE,false);
        }
        txtOwnersName=(TextView)findViewById(R.id.owners_name);
        txtCharacter=(TextView)findViewById(R.id.character);
        txtAge=(TextView)findViewById(R.id.age);
        txtSex=(TextView)findViewById(R.id.sex);
        txtBreed=(TextView)findViewById(R.id.breed);
        txtAbout=(TextView)findViewById(R.id.about);
        txtSprayed=(TextView)findViewById(R.id.sprayed);

        txtAbout.setMovementMethod(new ScrollingMovementMethod());
        txtCharacter.setMovementMethod(new ScrollingMovementMethod());

        imageView=(ImageView)findViewById(R.id.imageViewProfile);
        sex_image = findViewById(R.id.sex_image);


        srcagen=(Button)findViewById(R.id.button9);
        srcagen.setVisibility(self_profile ? View.GONE : View.VISIBLE);

        user=FirebaseAuth.getInstance().getCurrentUser();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(receiver_id +"/"+"image.jpg");


        databaseReference2= FirebaseDatabase.getInstance().getReference("Users").child(receiver_id);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ProfileActivity.this).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("push_token")){
                    receiver_push_token = dataSnapshot.child("push_token").getValue().toString();
                }
                kseks=String.valueOf(dataSnapshot.child("dogsex").getValue());
                txtSex.setText(kseks);
                sex_image.setImageResource(kseks.toLowerCase().contains("female") ? R.drawable.female : R.drawable.male);
                kmei = String.valueOf(dataSnapshot.child("dogname").getValue());
                kabaut=String.valueOf(dataSnapshot.child("about").getValue());
                txtAbout.setText(kabaut);
                kbrid=String.valueOf(dataSnapshot.child("dogbreed").getValue());
                txtBreed.setText(kbrid);
                kchar=String.valueOf(dataSnapshot.child("dogcharacter").getValue());
                txtCharacter.setText(kchar.replace("[","").replace("]",""));
                kejdz=String.valueOf(dataSnapshot.child("dogage").getValue());
                txtAge.setText(kejdz);
                user_name =String.valueOf(dataSnapshot.child("username").getValue());
                txtOwnersName.setText(user_name);
                ksprejd=String.valueOf(dataSnapshot.child("sprayed").getValue());
                kvak=String.valueOf(dataSnapshot.child("vak").getValue());
                if(ksprejd.equals("Yes") && kvak.equals("Yes"))
                    txtSprayed.setText("Sterilised"+"," + " " +"Vaccinated");
                else
                {
                    if(ksprejd.equals("No") && kvak.equals("Yes"))
                        txtSprayed.setText("Unsterilised"+"," + " " +"Vaccinated");
                    else {
                        if (ksprejd.equals("Yes") && kvak.equals("No"))
                            txtSprayed.setText("Sterilised" +"," + " " + "Unvaccinated");

                        else {
                            if (ksprejd.equals("No") && kvak.equals("No"))
                                txtSprayed.setText("Unsterilised" +"," + " " + "Unvaccinated");
                        }
                    }
                }
                getSupportActionBar().setTitle("Meet"+" "+kmei);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        srcagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UserDetails.chatWith=txtOwnersName.getText().toString();
                databaseReference2.child("Chat").child(user.getUid()).setValue(user.getDisplayName());
                if(!mojkurac.contains(txtOwnersName.getText().toString())) {
                    mojkurac.add(txtOwnersName.getText().toString());
                }
                startActivity(new Intent(ProfileActivity.this,Chat.class)
                        .putExtra("push_token",receiver_push_token).putExtra(Cons.RECEIVER_ID,receiver_id)
                .putExtra(Cons.RECEIVER_NAME,user_name));
            }
        });


    }

}
