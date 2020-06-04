package com.synnlabz.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity implements LocationListener {

    SupportMapFragment supportMapFragment;          //defining items
    FusedLocationProviderClient client;
    public LocationManager locationManager;

    private TextView mNameField, mBMIField, mWeightField, mCategoryField, mCountryField, mCityField, mTimeField;
    private ImageView mGenderField;
    private double longtitude, latitude;
    private int hour, minute;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mUserDatabase;

    private Button Save, Delete;

    private String userId, username, bmi, weight, gender, category, profileImageUrl, bestProvider;

    private ImageView mProfileImage;

    private Uri resultUri;
    public Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mNameField = (TextView) findViewById(R.id.username);            //initialzing components
        mBMIField = (TextView) findViewById(R.id.bmi);
        mWeightField = (TextView) findViewById(R.id.weight);
        mGenderField = (ImageView) findViewById(R.id.gender);
        mCategoryField = (TextView) findViewById(R.id.cat);
        mCountryField = (TextView) findViewById(R.id.country);
        mCityField = (TextView) findViewById(R.id.city);
        mTimeField = (TextView) findViewById(R.id.time);
        mProfileImage = (ImageView) findViewById(R.id.profileImage);
        Delete = (Button) findViewById(R.id.delete);

        Calendar now = Calendar.getInstance();      //get system calender
        hour = now.get(Calendar.HOUR_OF_DAY);       //get hours and minutes
        minute = now.get(Calendar.MINUTE);
        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);    //set format

        mTimeField.setText(time);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);        //initializing map fragment
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);     //get system location
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {      //cheking location is enables or not
            getCurrentLocation();       //if location is enabled
        } else {
            ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {     //same here
            Toast.makeText(getApplicationContext(),"Please Enable Your GPS",Toast.LENGTH_SHORT).show();
        }else {
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);        //if location is available calling these function
            get_location(location);
        }

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();       //get current users id from database
        firebaseUser = mAuth.getInstance().getCurrentUser();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();      //get users information

        mProfileImage.setOnClickListener(new View.OnClickListener() {       ///updating profile picture
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {      //deleting users account
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this, R.style.MyDialog);
                dialog.setTitle("Are you Sure??");      //alert dialog box
                dialog.setMessage("Deleting this account will result in completelt removing your data and account from the server and you won't be able to access the app");
                dialog.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Profile.this, "Your Account is Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Profile.this, Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Profile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {  //getting locations long and lat
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getApplicationContext(),"Please Enable Your GPS",Toast.LENGTH_SHORT).show();
        }else {
            try {
                longtitude = location.getLongitude();       //getting longtitude and latitude
                latitude = location.getLatitude();
            }catch (NullPointerException e){

            }

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void get_location(Location location) { //get users current location
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getApplicationContext(),"Please Enable Your GPS",Toast.LENGTH_SHORT).show();
        }else {
            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses=null;
                try {
                    addresses = geocoder.getFromLocation(latitude,longtitude,1);        //getting geocordinates and giving out
                    String country = addresses.get(0).getCountryName();     //device country name
                    String city = addresses.get(0).getLocality();           //device city name
                    mCountryField.setText(country);
                    mCityField.setText(city);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void getCurrentLocation() {     //showing current location in map
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location!=null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I'm Here");     //marker
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));      //map zoom level. change this value for zoom or zoomout
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    private void getUserInfo() {        //getting users information
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){      //retrieving values from database
                        username = map.get("name").toString();
                        mNameField.setText(username);
                    }
                    if(map.get("bmi")!=null){   //retrieving values from database
                        bmi = map.get("bmi").toString();
                        mBMIField.setText(bmi);
                    }
                    if(map.get("weight")!=null){        //retrieving values from database
                        weight = map.get("weight").toString();
                        mWeightField.setText(weight);
                    }
                    if(map.get("category")!=null){      //retrieving values from database
                        category = map.get("category").toString();
                        mCategoryField.setText(category);
                    }
                    if(map.get("gender")!=null){
                        gender = map.get("gender").toString();
                        if (gender=="1"){       //retrieving values from database
                            mGenderField.setImageDrawable(getResources().getDrawable(R.drawable.gender1));      //set image according to gender
                        }else if(gender=="2"){
                            mGenderField.setImageDrawable(getResources().getDrawable(R.drawable.gender2));
                        }
                    }
                    Glide.clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":     //setting profile image
                                Glide.with(getApplication()).load(R.drawable.default_user).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInformation() {        //saving profile picture to the database
        if(resultUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {    //Errorrr Handleee
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map userInfo = new HashMap();
                            userInfo.put("profileImageUrl", uri.toString());
                            mUserDatabase.updateChildren(userInfo);
                            Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(Profile.this, "Error Uploading Photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else{
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==44){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();       //calling method to get current location
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
            saveUserInformation();      //saving users informations
        }
    }

    public void backToHome(View view) {
        startActivity(new Intent(Profile.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
