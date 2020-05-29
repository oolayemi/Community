package com.stylet.community.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stylet.community.Models.Post;
import com.stylet.community.R;

public class NewPostActivity extends AppCompatActivity {


    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    ImageView blogImage, userPhoto;
    Button postBtn;
    ProgressBar progressBar;
    EditText postDesc, postTitle;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);



        blogImage = findViewById(R.id.new_post_image);
        postBtn = findViewById(R.id.post_btn);
        progressBar = findViewById(R.id.new_post_progress);
        postDesc = findViewById(R.id.new_post_desc);
        userPhoto = findViewById(R.id.userPhoto);
        postTitle = findViewById(R.id.new_post_title);


        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(userPhoto);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                postBtn.setClickable(false);

                if (!TextUtils.isEmpty(postTitle.getText()) && !TextUtils.isEmpty(postDesc.getText()) && pickedImgUri != null){

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imagePathFile = storageReference.child(pickedImgUri.getLastPathSegment());
                    imagePathFile.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagePathFile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageDownloadLink = uri.toString();

                                    Post post = new Post(user.getDisplayName(),
                                                            postTitle.getText().toString(),
                                                            postDesc.getText().toString(),
                                                            imageDownloadLink,
                                                            user.getUid(),
                                                            user.getPhotoUrl().toString());
                                    
                                    addPost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    postBtn.setClickable(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    showMessage(e.getMessage());

                                }
                            });
                        }
                    });

                }
                else {
                    postBtn.setClickable(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    showMessage("Please fill in all fields");
                }
            }
        });

        blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }


            }
        });

    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        String key = myRef.getKey();

        post.setPostKey(key);

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added");
                progressBar.setVisibility(View.INVISIBLE);
                postBtn.setClickable(true);
                finish();
            }
        });

    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(NewPostActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }
            else
            {
                ActivityCompat.requestPermissions(NewPostActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }


    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            blogImage.setImageURI(pickedImgUri);

        }
    }



    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }
}
