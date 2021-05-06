package com.example.cmpe277_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ListingActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView iv_select_image;
    private Button btn_menu;
    private Button btn_browse;
    private String name, description, price;
    private Button btn_upload_product;
    private EditText name_product, description_product, price_product;
    private String downloadLink;


    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    String currentUser;

    CircularProgressIndicator circular_indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        circular_indicator = findViewById(R.id.circular_indicator);
        circular_indicator.setVisibility(View.INVISIBLE);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btn_upload_product = findViewById(R.id.btn_upload_product);

        btn_menu = findViewById(R.id.btn_menu);
        btn_browse = findViewById(R.id.btn_browse);

        name_product = findViewById(R.id.name_product);
        description_product = findViewById(R.id.description_product);
        price_product = findViewById(R.id.price_product);
        iv_select_image = findViewById(R.id.iv_select_image);



        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListingActivity.this, MenuActivity.class));
                finish();
            }

        });




        storageReference = FirebaseStorage.getInstance().getReference("ProductImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        progressDialog = new ProgressDialog(ListingActivity.this);

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });


        btn_upload_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadImage();

                Toast.makeText(getApplicationContext(), "Product uploaded Successfully!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ListingActivity.this, MenuActivity.class));
                finish();

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                iv_select_image.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {
        System.out.println("user id: " + currentUser);
        name = name_product.getText().toString();
        description = description_product.getText().toString();
        price = price_product.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(ListingActivity.this, "Please add product name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(ListingActivity.this, "Please add product description and contact info!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(ListingActivity.this, "Please add product price!", Toast.LENGTH_SHORT).show();
        } else if (FilePathUri == null) {
            Toast.makeText(ListingActivity.this, "Please add product image!", Toast.LENGTH_SHORT).show();
        } else {
            // progressDialog.setTitle("Product uploading...");
            // progressDialog.show();

            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    // progressDialog.dismiss();
                    storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadLink = uri.toString();
                            ProductInformation imageUploadInfo = new ProductInformation(name, downloadLink, description, price, currentUser);
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                        }
                    });


                }
            });

        }
    }

}