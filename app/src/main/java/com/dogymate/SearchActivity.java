package com.dogymate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dogymate.util.Cons;
import com.dogymate.util.SingleShotLocationProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    FloatingActionButton floatingActionButton;
    String ages = "Any age", sexs = "Any sex", breeds="Any breed", hosts, frequencys, distances,sprayeds="Yes";
    private String[] idievi=new String[10000];
    static boolean pretraga = true;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String age, breed, host, sex, frequency, dogyname, sprayed;
    double dist;
    double distance;
    double longit, latit;
    ArrayList<String> ch;
    Location ostali;
    LatLng latLng;
    TextView txt;
    boolean t=false;
    int br=0;
    String str;
    String[] stringcina;
    float[] distancici = new float[1];
    public String receiver_id;
    public Boolean self_profile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (null != getIntent()){
            self_profile = getIntent().getBooleanExtra(Cons.SELF_PROFILE,false);

            if(getIntent().hasExtra(Cons.AGE)){
                ages = getIntent().getStringExtra(Cons.AGE);
            }
            if(getIntent().hasExtra(Cons.BREEDS)){
                breeds = getIntent().getStringExtra(Cons.BREEDS);
            }
            if(getIntent().hasExtra(Cons.SEX)){
                sexs = getIntent().getStringExtra(Cons.SEX);
            }
            if(getIntent().hasExtra(Cons.SPRAYEDS)){
                sprayeds = getIntent().getStringExtra(Cons.SPRAYEDS);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ch = new ArrayList<>();
        ostali = new Location("ostali");
        txt = (TextView) findViewById(R.id.textView7);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setVisibility(self_profile ? View.GONE : View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, ChangeActivity.class));
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        SingleShotLocationProvider.requestSingleUpdate(SearchActivity.this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        if (null != location){
                            if (location.latitude > 0 && location.longitude > 0){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.latitude, location.longitude)));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            }
                        }
                    }
                });
        /*if (pretraga) {
            switch (distances) {
                case "1km":
                    distance = 1.0;
                    break;
                case "2km":
                    distance = 2.0;
                    break;
                case "5km":
                    distance = 5.0;
                    break;
                case "10km":
                    distance = 10.0;
                    break;
                case "More than 10km":
                    distance = 1000000.0;
                    break;
            }*/

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    dogyname = valueOf(ds.child("dogname").getValue());
                    age = valueOf(ds.child("dogage").getValue()).trim();
                    breed = valueOf(ds.child("dogbreed").getValue());
                    host = valueOf(ds.child("doghost").getValue());
                    frequency = valueOf(ds.child("dogfrequency").getValue());
                    sprayed = String.valueOf(ds.child("sprayed").getValue());
                    sex = valueOf(ds.child("dogsex").getValue());

                    if(null != ds.getKey() && (self_profile ? ds.getKey().equals(user.getUid()) : (!ds.getKey().equals(user.getUid())))){
                        if ((ds.hasChild("lat") && (null != ds.child("lat").getValue())) && (ds.hasChild("lon") && (null != ds.child("lon").getValue()))) {
                            latit = Double.valueOf(valueOf(ds.child("lat").getValue()));
                            longit = Double.valueOf(valueOf(ds.child("lon").getValue()));
                            ostali.setLatitude(latit);
                            ostali.setLongitude(longit);
                            if (age.equals(ages) && breed.equals(breeds) && sex.equals(sexs) && /*dist <= distance && !ds.getKey().equals(user.getUid()) && */ sprayed.equals(sprayeds)) {
                                idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                br++;
                                t = true;
                            }
                            if (ages.equals("Any age") && !breeds.equals("Any breed") && !sexs.equals("Any sex")) {
                                if (breed.equals(breeds) && sex.equals(sexs) && /*dist <= distance && !ds.getKey().equals(user.getUid()) &&  */sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                            if (breeds.equals("Any breed") && !ages.equals("Any age") && !sexs.equals("Any sex")) {
                                if (age.equals(ages) && sex.equals(sexs) && /*dist <= distance &&!ds.getKey().equals(user.getUid()) &&  */ sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                            if (sexs.equals("Any sex") && !breeds.equals("Any breed") && !ages.equals("Any age")) {
                                if (age.equals(ages) && breed.equals(breeds) && /*dist <= distance && !ds.getKey().equals(user.getUid()) &&  */ sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                            if (ages.equals("Any age") && breeds.equals("Any breed") && !sexs.equals("Any sex")) {
                                if (sex.equals(sexs) && /*dist <= distance && !ds.getKey().equals(user.getUid()) && */ sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                            if (ages.equals("Any age") && sexs.equals("Any sex") && !breeds.equals("Any breed")) {
                                if (breed.equals(breeds) && /*dist <= distance && !ds.getKey().equals(user.getUid()) &&  */sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                            if (breeds.equals("Any breed") && sexs.equals("Any sex") && !ages.equals("Any age")) {
                                if (age.equals(ages) && /*dist <= distance &&!ds.getKey().equals(user.getUid()) &&  */ sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                            if (breeds.equals("Any breed") && sexs.equals("Any sex") && ages.equals("Any age")) {
                                if (/*dist <= distance && !ds.getKey().equals(user.getUid()) &&  */sprayed.equals(sprayeds)) {
                                    idievi[br] = ds.getKey() + "," + longit + "," + latit;
                                    latLng = new LatLng(ostali.getLatitude(), ostali.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(dogyname).icon(BitmapDescriptorFactory.defaultMarker(self_profile ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    br++;
                                    t = true;
                                }
                            }
                        }
                    }
                }
                if(!t) {
                    Toast.makeText(SearchActivity.this, "No dogs matching your search. Please extend your search criteria.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String trazeniID;
        double trazenilon,trazenilat,kliknutilon,kliknutilat;
        kliknutilon=marker.getPosition().longitude;
        kliknutilat=marker.getPosition().latitude;
        for(int i=0;i<br;i++)
        {
            str=idievi[i];
            stringcina=str.split(",");
            trazeniID=stringcina[0];
            trazenilon=Double.valueOf(stringcina[1]);
            trazenilat=Double.valueOf(stringcina[2]);
            if(trazenilat==kliknutilat && trazenilon==kliknutilon) {
                receiver_id = trazeniID;
            }
        }
        startActivity(new Intent(SearchActivity.this, ProfileActivity.class).putExtra(Cons.RECEIVER_ID,receiver_id).putExtra(Cons.SELF_PROFILE,self_profile));
        return true;
    }
}
