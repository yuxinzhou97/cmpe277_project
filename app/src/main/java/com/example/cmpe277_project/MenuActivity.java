package com.example.cmpe277_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;

public class MenuActivity extends AppCompatActivity {

    private Button btn_logout;
    private Button btn_list_item;
    private Button btn_search;
    // private Button btn_category;
    private Button btn_my_listings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_logout = findViewById(R.id.btn_logout);
        btn_list_item = findViewById(R.id.btn_list_item);
        btn_search = findViewById(R.id.btn_search);
        // btn_category = findViewById(R.id.btn_category);
        btn_my_listings = findViewById(R.id.btn_my_listings);




        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MenuActivity.this, "You have signed out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
                finish();
            }
        });



        btn_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ListingActivity.class));
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ProductDisplayActivity.class));
                finish();
            }
        });



        btn_my_listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MyListingsActivity.class));
                finish();
            }
        });

    }
}