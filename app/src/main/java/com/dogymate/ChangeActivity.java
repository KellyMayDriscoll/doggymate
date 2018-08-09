package com.dogymate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.dogymate.util.Cons;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ChangeActivity extends AppCompatActivity {


    private Spinner inputAge,inputBreed,inputSpayed,inputSex,inputFrequency,inputHost,inputDistance;
    private ArrayAdapter<CharSequence> ageAdapter,breedAdapter,sexAdapter,spayedAdapter,frequencyAdapter,hostAdapter,distanceAdapter;
    private String kgod,breed,sprayed,ksex,kfreequency,khost,kdistance;
    private String[] listitems;
    boolean[] checkeditems;
    Button choose;
    ArrayList<Integer> mUserItems=new ArrayList<>();
    ArrayList<String> addItems=new ArrayList<>();
    ArrayList<String> putdogcharacter=new ArrayList<>();

    Button search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        choose=(Button)findViewById(R.id.button6);
        listitems=getResources().getStringArray(R.array.age);
        checkeditems=new boolean[listitems.length];

        inputAge=(Spinner)findViewById(R.id.spinner1);
        inputBreed=(Spinner)findViewById(R.id.spinner2);
        inputSex=(Spinner)findViewById(R.id.spinner3);
        inputSpayed=(Spinner)findViewById(R.id.spinner4);
        search=(Button)findViewById(R.id.search);


        ageAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.age2,android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAge.setAdapter(ageAdapter);

        breedAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.breed2,android.R.layout.simple_spinner_item);
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputBreed.setAdapter(breedAdapter);

        sexAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.sex2,android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSex.setAdapter(sexAdapter);

        /*frequencyAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.frequency,android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputFrequency.setAdapter(frequencyAdapter);

        hostAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.host,android.R.layout.simple_spinner_item);
        hostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputHost.setAdapter(hostAdapter);

        distanceAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.distance,android.R.layout.simple_spinner_item);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputDistance.setAdapter(distanceAdapter);*/

        spayedAdapter=ArrayAdapter.createFromResource(ChangeActivity.this,R.array.Spayed2,android.R.layout.simple_spinner_item);
        spayedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSpayed.setAdapter(spayedAdapter);

        inputAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kgod=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"How old should be doq who you are seraching?",Toast.LENGTH_LONG).show();
            }
        });

        inputBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                breed=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"What should be the breed of dog who you are seraching?",Toast.LENGTH_LONG).show();
            }
        });

        /*choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(ChangeActivity.this);
                mBuilder.setTitle("Choose character of your Doggy");
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
                            Toast.makeText(ChangeActivity.this,listitems[mUserItems.get(i)],Toast.LENGTH_LONG).show();
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

        inputSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ksex=inputSex.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"What should be the gender of the dog who you are seraching?",Toast.LENGTH_LONG).show();
            }
        });

        inputSpayed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sprayed=inputSpayed.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"Enter spayed",Toast.LENGTH_LONG).show();
            }
        });

        /*inputFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kfreequency=inputFrequency.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"Enter frequency",Toast.LENGTH_LONG).show();
            }
        });

        inputHost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                khost=inputHost.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"Enter host",Toast.LENGTH_LONG).show();
            }
        });

        inputDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kdistance=inputDistance.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChangeActivity.this,"Enter distance",Toast.LENGTH_LONG).show();
            }
        });*/


        findViewById(R.id.age_spinner_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputAge.performClick();
            }
        });

        findViewById(R.id.breed_spinner_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBreed.performClick();
            }
        });

        findViewById(R.id.sex_spinner_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSex.performClick();
            }
        });

        findViewById(R.id.spayed_spinner_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSpayed.performClick();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChangeActivity.this,SearchActivity.class);
                intent.putExtra(Cons.AGE,inputAge.getSelectedItem().toString());
                intent.putExtra(Cons.BREEDS,inputBreed.getSelectedItem().toString());
                intent.putExtra(Cons.SEX,inputSex.getSelectedItem().toString());
                intent.putExtra(Cons.SPRAYEDS,inputSpayed.getSelectedItem().toString());
                SearchActivity.pretraga=true;
                //SearchActivity.ages=inputAge.getSelectedItem().toString();
                //SearchActivity.breeds=inputBreed.getSelectedItem().toString().trim();
                //SearchActivity.sexs=inputSex.getSelectedItem().toString();
                //SearchActivity.sprayeds=inputSpayed.getSelectedItem().toString();


                //SearchActivity.hosts=inputHost.getSelectedItem().toString();
                //SearchActivity.frequencys=inputFrequency.getSelectedItem().toString();
                //SearchActivity.distances=inputDistance.getSelectedItem().toString();
                startActivity(intent);
            }
        });

    }
}
