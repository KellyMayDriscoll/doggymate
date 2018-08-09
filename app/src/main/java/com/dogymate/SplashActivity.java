package com.dogymate;


import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000;
    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw4yGuhc3VG/M1d/kroqHKUfxKdkW91Rwgz4bDS3aFWu9zOvy1bpt8L6cV0fs8WU1UPDZ2p9qKCh+P6Q12aOB8ZbnpxKe9grwuTqakhGkVvtvxfdx/EAmanz33PDRTKQgKCP8joZcDj3MMN9DM6HIgIRmL/YaMmpZn2dMF7DDIzwfhzNu269g7EOtrmNtd7+BE2PE72kyDvmhNPwTZyFbAWIMRTRuUwEH0Pb3BoMVq0aMcQ9uvtHKLI95ebgF5J8XNTi3mTP3Xgig5FTiHhp8zDwbmShbZPgjuSBIyT5n+6CRLP1VtCFQrY23HGnxQ36+pYF7X2HUwrv4htYvwjkFlQIDAQAB";
    private static final byte[] SALT = new byte[] {-46, 65, 30, -128, -13, -78, 73, -64, 51, 38, -95, -45, 77, -117, -36, -113, -11, 62, -44, 89};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
  /*      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mintent=new Intent(SplashActivity.this,RegisterActivity.class);
                startActivity(mintent);
                finish();

            }
        },SPLASH_TIME_OUT);*/
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Construct the LicenseCheckerCallback. The library calls this when done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        // Construct the LicenseChecker with a Policy.
        mChecker = new LicenseChecker(
                this, new ServerManagedPolicy(this,
                new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY  // Your public licensing key.
        );
        mChecker.checkAccess(mLicenseCheckerCallback);
    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int reason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            AllowUser();
        }

        public void dontAllow(int reason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }

            if (reason == Policy.RETRY) {
                // If the reason received from the policy is RETRY, it was probably
                // due to a loss of connection with the service, so we should give the
                // user a chance to retry. So show a dialog to retry.
                new AlertDialog.Builder(SplashActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.connection_error)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(SplashActivity.this,SplashActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();
            } else {
                // Otherwise, the user is not licensed to use this app.
                // Your response should always inform the user that the application
                // is not licensed, but your behavior at that point can vary. You might
                // provide the user a limited access version of your app or you can
                // take them to Google Play to purchase the app.

                new AlertDialog.Builder(SplashActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.license_error)
                        .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                                startActivity(marketIntent);
                                finish();
                            }
                        }).create().show();
            }
        }

        @Override
        public void applicationError(int errorCode) {

        }
    }

    private void AllowUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null) {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() && dataSnapshot.hasChild("username") && null != dataSnapshot.child("username").getValue()){
                        Intent dog=new Intent(SplashActivity.this,ChoiceActivity.class);
                        startActivity(dog);
                        finish();
                    }else{
                        Intent cintent = new Intent(SplashActivity.this, RegisterActivity.class);
                        startActivity(cintent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Intent cintent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(cintent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChecker.onDestroy();

    }
}
