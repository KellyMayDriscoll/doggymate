package com.dogymate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SetFiltersFirstTimeActivity extends AppCompatActivity {

    private Spinner inputFrequency,inputHost,inputDistance;
    private ArrayAdapter<CharSequence> frequencyAdapter,hostAdapter,distanceAdapter;
    private Button baton,choosebtn;
    private String khost,kfreequency,kdistance;
    private String[] listitems;
    boolean[] checkeditems;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_filters_first_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listitems=getResources().getStringArray(R.array.age);
        checkeditems=new boolean[listitems.length];

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());


        inputFrequency= (Spinner)findViewById(R.id.spinner9);
        inputHost=(Spinner)findViewById(R.id.spinner10);
        inputDistance=(Spinner)findViewById(R.id.spinner11);

        baton=(Button)findViewById(R.id.button7);


        frequencyAdapter=ArrayAdapter.createFromResource(SetFiltersFirstTimeActivity.this,R.array.frequency,android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputFrequency.setAdapter(frequencyAdapter);

        hostAdapter=ArrayAdapter.createFromResource(SetFiltersFirstTimeActivity.this,R.array.host,android.R.layout.simple_spinner_item);
        hostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputHost.setAdapter(hostAdapter);

        distanceAdapter=ArrayAdapter.createFromResource(SetFiltersFirstTimeActivity.this,R.array.distance,android.R.layout.simple_spinner_item);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputDistance.setAdapter(distanceAdapter);


        /*inputBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                breed=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetFiltersFirstTimeActivity.this,"What is the breed of dog who you search?",Toast.LENGTH_LONG).show();
            }
        });

        inputSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ksex=inputSex.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetFiltersFirstTimeActivity.this,"What is the gender of your dog?",Toast.LENGTH_LONG).show();
            }
        });*/

        inputFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kfreequency=inputFrequency.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetFiltersFirstTimeActivity.this,"Enter frequency",Toast.LENGTH_LONG).show();
            }
        });

        inputHost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                khost=inputHost.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetFiltersFirstTimeActivity.this,"Enter host",Toast.LENGTH_LONG).show();
            }
        });

        inputDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kdistance=inputDistance.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetFiltersFirstTimeActivity.this,"Enter distance",Toast.LENGTH_LONG).show();
            }
        });

        /*choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(SetFiltersFirstTimeActivity.this);
                mBuilder.setTitle("Choose age of Doggy who you are searching");
                mBuilder.setMultiChoiceItems(listitems, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked) {
                            if (!mUserItems.contains(position))
                                mUserItems.add(position);
                            else

                                mUserItems.remove((Integer)position);
                        }
                    }
                });

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<mUserItems.size();i++){
                            addItems.add(listitems[mUserItems.get(i)]);
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<listitems.length;i++)
                        {
                            checkeditems[i]=false;
                            mUserItems.clear();
                        }
                    }
                });
                AlertDialog mDialog=mBuilder.create();
                mDialog.show();
            }
        });*/

        baton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("dogfrequency").setValue(kfreequency);
                databaseReference.child("doghost").setValue(khost);
                databaseReference.child("dogdistance").setValue(kdistance);
                Intent menjaza=new Intent(SetFiltersFirstTimeActivity.this,ChoiceActivity.class);
                startActivity(menjaza);
            }
        });
    }
}
