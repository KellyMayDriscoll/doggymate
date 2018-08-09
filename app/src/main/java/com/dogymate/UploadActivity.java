package com.dogymate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.dogymate.util.SingleShotLocationProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    //Moje promenljive

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    //kraj promenljive


    private EditText inputNameofDog,inputAbout,inputOwnersName;
    private Spinner inputAge,inputBreed,inputSpayed,inputSex,inputVaccinated;
    private ArrayAdapter<CharSequence> ageAdapter,breedAdapter,sexAdapter,spayedAdapter,vaccinatedAdapter;
    private ImageView inputPicture;
    private Button submit, choose;
    private String kgod,breed,sprayed,ksex,kvaccinated;
    private String[] listitems;
    boolean[] checkeditems;
    int br=0;
    ArrayList<Integer> mUserItems=new ArrayList<>();
    ArrayList<String> addItems=new ArrayList<>();

    DatabaseReference databaseReference;

    ArrayList<String> dchar;
    FirebaseUser user;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://doggymate-87a20.appspot.com/");

    private float user_latitude = 0F,user_longitude=0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listitems = getResources().getStringArray(R.array.character);
        checkeditems = new boolean[listitems.length];


        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        submit = (Button) findViewById(R.id.button2);
        choose = (Button) findViewById(R.id.button5);

        inputPicture = (ImageView) findViewById(R.id.imageView7);

        inputNameofDog = (EditText) findViewById(R.id.editText);
        inputAbout = (EditText) findViewById(R.id.editText7);
        inputOwnersName = (EditText) findViewById(R.id.editText9);


        dchar = new ArrayList<>();

        inputAge = (Spinner) findViewById(R.id.spinner);
        inputBreed = (Spinner) findViewById(R.id.spinner4);
        inputSex = (Spinner) findViewById(R.id.spinner2);
        inputSpayed = (Spinner) findViewById(R.id.spinner8);
        inputVaccinated = (Spinner) findViewById(R.id.spinner15);

        vaccinatedAdapter = ArrayAdapter.createFromResource(UploadActivity.this, R.array.Spayed, android.R.layout.simple_spinner_item);
        vaccinatedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputVaccinated.setAdapter(vaccinatedAdapter);

        ageAdapter = ArrayAdapter.createFromResource(UploadActivity.this, R.array.age, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAge.setAdapter(ageAdapter);

        breedAdapter = ArrayAdapter.createFromResource(UploadActivity.this, R.array.breed, android.R.layout.simple_spinner_item);
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputBreed.setAdapter(breedAdapter);

        sexAdapter = ArrayAdapter.createFromResource(UploadActivity.this, R.array.sex, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSex.setAdapter(sexAdapter);

        spayedAdapter = ArrayAdapter.createFromResource(UploadActivity.this, R.array.Spayed, android.R.layout.simple_spinner_item);
        spayedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSpayed.setAdapter(spayedAdapter);

        //moeeee
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        inputPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
        //kraj moeeee
        //cukam, nabivam!


        inputAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kgod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "How old is your dog?", Toast.LENGTH_SHORT).show();
            }
        });

        inputBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                breed = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "What is the breed of your dog?", Toast.LENGTH_SHORT).show();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(UploadActivity.this);
                mBuilder.setTitle("Choose character of your Doggy");
                mBuilder.setMultiChoiceItems(listitems, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked && br<=3) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                                br++;
                            }
                            else
                                mUserItems.remove((Integer) position);
                        }
                        if(br>3)
                        {
                            mUserItems.remove((Integer)position);
                            checkeditems[position]=false;
                            Toast.makeText(UploadActivity.this,"Limit is 3!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < mUserItems.size(); i++) {
                            addItems.add(listitems[mUserItems.get(i)]);
                        }
                        choose.setEnabled(false);
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
                        for (int i = 0; i < listitems.length; i++) {
                            checkeditems[i] = false;
                            mUserItems.clear();
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        inputSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ksex = inputSex.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "What is the gender of your dog?", Toast.LENGTH_SHORT).show();
            }
        });

        inputSpayed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sprayed = inputSpayed.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "Is your doggy spayed?", Toast.LENGTH_SHORT).show();
            }
        });
        inputVaccinated.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kvaccinated = inputVaccinated.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "Is your doggy vaccinated?", Toast.LENGTH_SHORT).show();
            }
        });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!inputOwnersName.getText().toString().trim().equals("") && !inputNameofDog.getText().toString().trim().equals("") && !inputAbout.getText().toString().trim().equals(""))
                    {
                        databaseReference.child("username").setValue(inputOwnersName.getText().toString());
                        databaseReference.child("dogname").setValue(inputNameofDog.getText().toString());
                        databaseReference.child("dogbreed").setValue(breed);
                        databaseReference.child("dogage").setValue(kgod);
                        databaseReference.child("dogcharacter").setValue(addItems);
                        databaseReference.child("dogsex").setValue(ksex);
                        databaseReference.child("sprayed").setValue(sprayed);
                        databaseReference.child("about").setValue(inputAbout.getText().toString());
                        databaseReference.child("vak").setValue(kvaccinated);
                        if(user_latitude > 0 || user_longitude > 0) {
                            databaseReference.child("lat").setValue(user_latitude);
                            databaseReference.child("lon").setValue(user_longitude);
                        }


                        if (filePath != null) {
                            pd.show();

                            StorageReference childRef = storageRef.child(user.getUid() + "/" + "image.jpg");

                            //uploading the image
                            UploadTask uploadTask = childRef.putFile(filePath);

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pd.dismiss();
                                    Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UploadActivity.this, ChoiceActivity.class).putExtra("FromUploadActivity",true));
                                    finishAffinity();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(UploadActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(UploadActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    Toast.makeText(UploadActivity.this, "Fill empty fields", Toast.LENGTH_SHORT).show();
                }
            });
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                inputPicture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        boolean isGPSEnabled = ((LocationManager) this.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            SingleShotLocationProvider.requestSingleUpdate(UploadActivity.this,
                    new SingleShotLocationProvider.LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                            if (null != location){
                                user_latitude = location.latitude;
                                user_longitude = location.longitude;
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(UploadActivity.this, ChoiceActivity.class).putExtra("FromUploadActivity",false));
        finishAffinity();
    }
}
