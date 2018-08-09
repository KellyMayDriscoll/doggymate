package com.dogymate;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dogymate.util.Cons;
import com.dogymate.util.SingleShotLocationProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChoiceActivity extends AppCompatActivity {


    private ImageView iSearch,iUpload,iIntroduce,iAbout;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLocation;
    boolean homeVisible;
    FirebaseAuth auth;
    android.support.v7.widget.Toolbar toolbar;
    double latitude;
    double longitude;
    private FloatingActionButton fabtn;
    DatabaseReference databaseReference;
    FirebaseUser user;
    private String user_id = "";
    private static final String REG_TOKEN ="REG_TOKEN";
    private Button newMessage;
    private Boolean is_from_upload_activity = false;

    private String username = "",password = "";
    String provider = LocationManager.GPS_PROVIDER;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {
                        if(locationManager.getLastKnownLocation(provider)!=null)
                        {
                            Location location = locationManager.getLastKnownLocation(provider);
                            userLocation= new LatLng(location.getLatitude(),location.getLongitude());
                            if (is_from_upload_activity){
                                UpdateLatLong();
                            }
                        }
                        locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);

                    }

                }
            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        homeVisible=true;

        if(getIntent() != null){
            username = getIntent().getStringExtra(Cons.USER_NAME);
            password = getIntent().getStringExtra(Cons.PASSWORD);

            if( getIntent().hasExtra("FromUploadActivity")) {
                is_from_upload_activity = getIntent().getBooleanExtra("FromUploadActivity", false);
            }
        }

        //progressdialog = new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null) {
            if(null == auth.getUid()) {
                Toast.makeText(this, "Please Repoen...", Toast.LENGTH_LONG).show();
                finish();
            }else {
                user_id = auth.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
            }
        }
        else {
            user=FirebaseAuth.getInstance().getCurrentUser();
            user_id = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        }

       /* if(LoginActivity.id.equals(""))
        {
            user= FirebaseAuth.getInstance().getCurrentUser();
            databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());}
        else
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(LoginActivity.id);
        }*/

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("username")){
                    username=String.valueOf(dataSnapshot.child("username").getValue());

                    //check doggy lat long
                    if (dataSnapshot.hasChild("lat") && dataSnapshot.hasChild("lon")){
                        Float lati = Float.parseFloat(dataSnapshot.child("lat").getValue().toString());
                        Float longi = Float.parseFloat(dataSnapshot.child("lon").getValue().toString());
                        if(lati < 1 || longi <1){
                            // location not updated
                            is_from_upload_activity = true;
                            onPostResume();
                        }
                    }else {
                        // location not updated
                        is_from_upload_activity = true;
                        onPostResume();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SearchActivity.pretraga=false;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                userLocation =new LatLng(location.getLatitude(),location.getLongitude());
                UpdateLatLong();
                //Toast.makeText(getApplicationContext(),"Location is found!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {


            }

            @Override
            public void onProviderDisabled(String provider) {

           /*     if(homeVisible) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChoiceActivity.this)
                            .setMessage("Location inaccessible")
                            .setPositiveButton("Enable location", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    ChoiceActivity.this.startActivity(myIntent);
                                    //get gps
                                }
                            });

                    dialog.show();
                }
                else
                {
                    Intent PokreniActivity=(new Intent(getApplicationContext(), ChoiceActivity.class));
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(PokreniActivity);


                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext())
                            .setMessage("Location inaccessible")
                            .setPositiveButton("Enable location", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getApplicationContext().startActivity(myIntent);

                                    //get gps

                                }
                            });

                    dialog.show();
                }*/
            }
        };

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(false);
        crta.setBearingRequired(false);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_HIGH);
        provider = locationManager.getBestProvider(crta, true);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                if(locationManager.getLastKnownLocation(provider)!=null)
                {
                    Location location= locationManager.getLastKnownLocation(provider);//lastKnownLocation
                    userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    UpdateLatLong();
                }
                locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
            }
        }else{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(locationManager.getLastKnownLocation(provider)!=null)
            {
                Location location = locationManager.getLastKnownLocation(provider);//lastKnownLocation
                userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                UpdateLatLong();
            }
            locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
        }
        newMessage = findViewById(R.id.new_message_button);
        iSearch=(ImageView)findViewById(R.id.imageView2);
        iUpload=(ImageView)findViewById(R.id.imageView4);
        iIntroduce=(ImageView)findViewById(R.id.imageView5);
        iAbout=(ImageView)findViewById(R.id.imageView6);

        auth = FirebaseAuth.getInstance();

        iSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sintent = new Intent(ChoiceActivity.this, ChangeActivity.class);
                startActivity(sintent);
            }
        });
        iUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent uintent = new Intent(ChoiceActivity.this, MyChatsActivity.class);
                    startActivity(uintent);
            }
        });
        iIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iintent=new Intent(ChoiceActivity.this,IntroduceActivity.class);
                startActivity(iintent);
            }
        });
        iAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aintent=new Intent(ChoiceActivity.this,AboutUsActivity.class);
                startActivity(aintent);
            }
        });

        newMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                iUpload.performClick();
            }
        });

    }

    private void UpdateLatLong() {
        if (null != userLocation && userLocation.latitude > 0) {
            if (is_from_upload_activity) {
                databaseReference.child("lat").setValue(userLocation.latitude);
                databaseReference.child("lon").setValue(userLocation.longitude);
                is_from_upload_activity = false;
            }
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SingleShotLocationProvider.requestSingleUpdate(ChoiceActivity.this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        if (null != location){
                            if (location.latitude > 0 && location.longitude > 0){
                                userLocation = new LatLng(location.latitude,location.longitude);
                            }
                        }
                    }
                });

        if (!user_id.isEmpty()) {

            new AsyncTask<Void,Void,Boolean>(){
                @Override
                protected Boolean doInBackground(Void... voids) {
                    try {
                        String push_token = FirebaseInstanceId.getInstance().getToken(getString(R.string.authorizedEntity), getString(R.string.scope));
                        if (push_token != null) {
                            Map<String, Object> update_values = new HashMap<>();
                            update_values.put("push_token", push_token);
                            databaseReference.updateChildren(update_values);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }.execute();

            FirebaseDatabase.getInstance().getReference("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("unreaded_message")){
                        if(dataSnapshot.child("unreaded_message").getValue(Boolean.class)){
                            newMessage.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(this, "Error geting user profile , Exiting!!!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings){
            Intent settingsintent=new Intent(ChoiceActivity.this,SettingsActivity.class);
            startActivity(settingsintent);
        }else if(item.getItemId() == R.id.action_profile){
            startActivity(new Intent(ChoiceActivity.this,SearchActivity.class).putExtra(Cons.SELF_PROFILE,true));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled && is_from_upload_activity) {
            new AlertDialog.Builder(ChoiceActivity.this)
                    .setTitle("Location inaccessible")
                    .setMessage("Your Doggy profile not updateded with location,GPS Access is required to update doggy location!")
                    .setPositiveButton("Enable location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getApplicationContext().startActivity(myIntent);
                        }
                    }).create().show();
        }
    }
}
