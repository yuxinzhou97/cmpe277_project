package com.example.cmpe277_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MyListingsActivity extends AppCompatActivity implements RecyclerViewClickInterface {
    Button btn_back_to_menu2;
    RecyclerView rv_display2;
    DatabaseReference databaseReference2;
    ArrayList<ProductInformation> productList2;
    ArrayList<String> keyList2;
    ArrayList<ProductInformation> myItemList;
    ArrayList<String> myKeyList;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        Map<String, Integer> myItemKeyMap = new HashMap<String, Integer>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().trim();
        btn_back_to_menu2 = findViewById(R.id.btn_back_to_menu2);
        rv_display2 = findViewById(R.id.rv_display2);
        productList2 = new ArrayList<ProductInformation>();
        keyList2 = new ArrayList<String>();
//        clearAll();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference2 = database.getReference("Products");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("exist: " + dataSnapshot.hasChildren() + dataSnapshot.getChildrenCount());
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    ProductInformation product = data.getValue(ProductInformation.class);
                    productList2.add(product);
                    System.out.println("current product list size " + productList2.size());

                    String itemKey =  data.getKey();
                    keyList2.add(itemKey);
                }
                displayMyListings(currentUser, productList2, keyList2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        btn_back_to_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyListingsActivity.this, MenuActivity.class));
                finish();
            }
        });

    }

    private void clearAll() {
        if (productList2 != null) {
            productList2.clear();
        }
        productList2 = new ArrayList<ProductInformation>();
    }




    private void displayMyListings(String keyword, ArrayList<ProductInformation> productList2, ArrayList<String> keyList2) {
        myItemList = new ArrayList<ProductInformation>();
        myKeyList = new ArrayList<String>();


        for (int i = 0; i < productList2.size(); i++) {
            ProductInformation item = productList2.get(i);
            String itemUserID = item.getUserID().trim();
            if (itemUserID.equals(keyword)) {
                myItemList.add(item);
                String curKey = keyList2.get(i);
                myKeyList.add(curKey);
                // System.out.println(keyword + "==" + itemUserID + " match");
            } else {
                // System.out.println(keyword + "!=" + itemUserID + " do not match");
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_display2.setLayoutManager(linearLayoutManager);
        rv_display2.setHasFixedSize(true);
        CustomAdapter adapter = new CustomAdapter(myItemList, getApplicationContext(), this);
        rv_display2.setItemAnimator(new DefaultItemAnimator());
        rv_display2.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, EditListingActivity.class);

        String key = myKeyList.get(position);
        ProductInformation item = myItemList.get(position);
        intent.putExtra("key", key);
        intent.putExtra("imageUrl", item.getUrl());
        intent.putExtra("name", item.getName());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("price", item.getPrice());

        System.out.println("get position " + myItemList.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(int position) {
        Intent intent = new Intent(this, EditListingActivity.class);
        startActivity(intent);
        System.out.println("get position " + myItemList.get(position).getName());
    }
}