package com.buzydevelopers.instagram;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView profilePic;
    EditText profileName, userName, Bio;
    Button createProfile;
    String stringUrl, stringName, stringUsername, stringBio;


    private static final int REQUEST_CODE = 1;
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference().child("Profiles");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilePic = findViewById(R.id.profilepic);
        profileName = findViewById(R.id.profilename);
        userName = findViewById(R.id.username);
        Bio = findViewById(R.id.bio);
        createProfile = findViewById(R.id.create);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_CODE);
            }
        });

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringName = profileName.getText().toString();
                stringUsername = userName.getText().toString();
                stringBio = Bio.getText().toString();
                addProfileToDatabase(stringUrl,stringName,stringUsername,stringBio);
            }
        });
    }

    private void addProfileToDatabase(String stringUrl, String stringName, String stringUsername, String stringBio) {

        ProfileDetails profileDetails = new ProfileDetails(stringUrl,stringName,stringUsername,stringBio);
        databaseReference.push().setValue(profileDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Profile Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Profile Creation Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();//copied

            StorageReference filePath = mStorage.child("wallpaperView").child(uri.getLastPathSegment());//navigated
            //pasting
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Adding image to the database
                    stringUrl = downloadUrl.toString();
                    Picasso.get().load(stringUrl).into(profilePic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Image not Selected",Toast.LENGTH_SHORT).show();
    }


}
