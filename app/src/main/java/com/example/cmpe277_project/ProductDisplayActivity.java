package com.example.cmpe277_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDisplayActivity extends AppCompatActivity {

    private RecyclerView rv_display;

    private Button btn_search_submit;
    private Button btn_back_to_menu;
    private EditText et_search;
    private DatabaseReference databaseReference;
    private ArrayList<ProductInformation> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);
        btn_back_to_menu = findViewById(R.id.btn_back_to_menu);
        rv_display = findViewById(R.id.rv_display);
        btn_search_submit = findViewById(R.id.btn_search_submit);
        et_search = findViewById(R.id.et_search);
        productList = new ArrayList<ProductInformation>();
        clearAll();
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ProductInformation product = data.getValue(ProductInformation.class);
                    productList.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        for (ProductInformation item : productList) {
            System.out.println("name: " + item.getName());
            System.out.println("url: " + item.getUrl());
        }



        btn_back_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDisplayActivity.this, MenuActivity.class));
                finish();
            }
        });


        btn_search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = et_search.getText().toString();
                displaySearchResult(keyword);
            }
        });
    }

    private void clearAll() {
        if (productList != null) {
            productList.clear();
        }
        productList = new ArrayList<ProductInformation>();
    }


    private void displaySearchResult(String keyword) {
        ArrayList<ProductInformation> filteredList = new ArrayList<ProductInformation>();
        for (ProductInformation item : productList) {
            if (item.getName().contains(keyword)) {
                filteredList.add(item);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_display.setLayoutManager(linearLayoutManager);
        rv_display.setHasFixedSize(true);
        CustomAdapter adapter = new CustomAdapter(filteredList, getApplicationContext());
        rv_display.setItemAnimator(new DefaultItemAnimator());
        rv_display.setAdapter(adapter);

    }
}