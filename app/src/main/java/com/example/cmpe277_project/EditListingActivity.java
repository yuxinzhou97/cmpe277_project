package com.example.cmpe277_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditListingActivity extends AppCompatActivity {


    ImageView iv_display_image;
    EditText et_edit_name;
    EditText et_edit_description;
    EditText et_edit_price;
    Button btn_submit_changes;
    Button btn_back_my_listings;
    Button btn_delete_listing;
    String currentUser;
    String newName, newDescription, newPrice;
    String key, name, description, price, imageUrl;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);



        iv_display_image = findViewById(R.id.iv_display_image);
        et_edit_name = findViewById(R.id.et_edit_name);
        et_edit_description = findViewById(R.id.et_edit_description);
        et_edit_price = findViewById(R.id.et_edit_price);
        btn_submit_changes = findViewById(R.id.btn_submit_changes);
        btn_back_my_listings = findViewById(R.id.btn_back_my_listings);
        btn_delete_listing = findViewById(R.id.btn_delete_listing);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().trim();

        key = getIntent().getExtras().getString("key");
        name = getIntent().getExtras().getString("name");
        description = getIntent().getExtras().getString("description");
        price = getIntent().getExtras().getString("price");
        imageUrl = getIntent().getExtras().getString("imageUrl");
        ref = FirebaseDatabase.getInstance().getReference("Products");



        Glide.with(getApplicationContext()).load(imageUrl).into(iv_display_image);
        et_edit_name.setText(name);
        et_edit_description.setText(description);
        et_edit_price.setText(price);

        btn_back_my_listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditListingActivity.this, MyListingsActivity.class));
                finish();
            }
        });

        btn_delete_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
                Toast.makeText(getApplicationContext(), "Product deleted Successfully!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditListingActivity.this, MyListingsActivity.class));
                finish();
            }
        });

        btn_submit_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
                Toast.makeText(getApplicationContext(), "Product updated Successfully!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditListingActivity.this, MyListingsActivity.class));
                finish();
            }
        });

    }

    public void updateItem() {
        System.out.println("user id: " + currentUser);

        newName = et_edit_name.getText().toString();
        newDescription = et_edit_description.getText().toString();
        newPrice = et_edit_price.getText().toString();

        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(EditListingActivity.this, "Please add product name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newDescription)) {
            Toast.makeText(EditListingActivity.this, "Please add product description and contact info!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newPrice)) {
            Toast.makeText(EditListingActivity.this, "Please add product price!", Toast.LENGTH_SHORT).show();
        } else {
            // DatabaseReference childRef = ref.child(key);
            ProductInformation updatedItem = new ProductInformation(newName, imageUrl, newDescription, newPrice, currentUser);
            ref.child(key).setValue(updatedItem);
        }
    }

    public void deleteItem() {
        ref.child(key).removeValue();
    }
}