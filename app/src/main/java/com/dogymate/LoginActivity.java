package com.dogymate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dogymate.util.Cons;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText mail,password,name;
    private Button btnl;
    private FirebaseUser user;
    private FirebaseAuth auth;
    DatabaseReference databaseReference;
    String imeyaid,imeyaidentifikaciju;
    String st;

    private String user_name="",user_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mail=(EditText)findViewById(R.id.editText13);
        password=(EditText)findViewById(R.id.Password);
        name=(EditText)findViewById(R.id.Emailce);
        btnl=(Button)findViewById(R.id.btnlog);
        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");



        btnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imejl = mail.getText().toString();
                final String sifra = password.getText().toString();
                final String user = name.getText().toString();
                final String pass = password.getText().toString();
                imeyaidentifikaciju = name.getText().toString();


                if (user.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please, enter your name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(imejl)) {
                    Toast.makeText(LoginActivity.this, "Please, enter your e-mail", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sifra)) {
                    Toast.makeText(LoginActivity.this, "Please, enter your password", Toast.LENGTH_SHORT).show();
                } else {

                    auth.signInWithEmailAndPassword(imejl, sifra)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        if (sifra.length() < 6)
                                            password.setError("Wrong password!");
                                        else {
                                            Toast.makeText(LoginActivity.this, "Authentication failed, check your email or password, or sign up if you don't have profile", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        String user_id = auth.getCurrentUser().getUid();
                                        databaseReference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists() && dataSnapshot.hasChild("username") && null != dataSnapshot.child("username").getValue()) {
                                                    Intent dog = new Intent(LoginActivity.this, ChoiceActivity.class);
                                                    startActivity(dog);
                                                    finish();
                                                } else {
                                                    Intent cintent = new Intent(LoginActivity.this, UploadActivity.class);
                                                    startActivity(cintent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Intent cintent = new Intent(LoginActivity.this, UploadActivity.class);
                                                startActivity(cintent);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }
}
