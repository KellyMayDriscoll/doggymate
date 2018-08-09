package com.dogymate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RessetPasswordActivty extends AppCompatActivity {
    private EditText PassEmail;
    private Button btnko;
    private FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resset_password_activty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PassEmail = (EditText) findViewById(R.id.etEmail);
        btnko = (Button) findViewById(R.id.btnRP);
        fauth = FirebaseAuth.getInstance();
        btnko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emka = PassEmail.getText().toString().trim();
                if (TextUtils.isEmpty(emka))
                    Toast.makeText(RessetPasswordActivty.this, "Enter your e-mail address, please", Toast.LENGTH_LONG).show();
                else
                    fauth.sendPasswordResetEmail(emka).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RessetPasswordActivty.this, "Password reset sent on email!", Toast.LENGTH_LONG).show();
                                Intent intentic = new Intent(RessetPasswordActivty.this, RegisterActivity.class);
                                startActivity(intentic);
                            } else {
                                Toast.makeText(RessetPasswordActivty.this, "Error in sending password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
        });
    }
}
