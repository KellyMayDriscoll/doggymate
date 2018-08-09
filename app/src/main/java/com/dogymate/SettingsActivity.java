package com.dogymate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText newName,newMobile;
    private Button upload,resetpass,delete;
    DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        newName=(EditText)findViewById(R.id.editText2);
        upload=(Button)findViewById(R.id.button3);
        resetpass=(Button)findViewById(R.id.button4);
        delete=(Button)findViewById(R.id.button6);

        user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imenovo=newName.getText().toString();
                databaseReference.child("username").setValue(imenovo);
                if(!imenovo.trim().equals(""))
               startActivity(new Intent(SettingsActivity.this,ChoiceActivity.class));
                else
                    Toast.makeText(SettingsActivity.this,"Please, enter new name/",Toast.LENGTH_SHORT).show();
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,RessetPasswordActivty.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                databaseReference.child("dogname").removeValue();
                databaseReference.child("dogage").removeValue();
                databaseReference.child("dogbreed").removeValue();
                databaseReference.child("dogcharacter").removeValue();
                databaseReference.child("dogsex").removeValue();
                databaseReference.child("dogfrequency").removeValue();
                databaseReference.child("doghost").removeValue();
                databaseReference.child("vak").removeValue();
                databaseReference.child("spayed").removeValue();
                databaseReference.child("about").removeValue();*/
                startActivity(new Intent(SettingsActivity.this,UploadActivity.class));
                finishAffinity();
            }
        });
    }
}
