package com.example.cmpe277_project;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<ProductInformation> itemList;
    private Context context;

    public CustomAdapter(ArrayList<ProductInformation> productList, Context context) {
        this.itemList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProductInformation item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice());

        // Picasso.get().load(Uri.parse(item.getUrl())).into(holder.imageView);
        // Glide library
        Glide.with(context).load(item.getUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name;
        public TextView description;
        public TextView price;
        public ProductInformation mItem;
        private RelativeLayout item_layout;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_image);
            name = view.findViewById(R.id.tv_name);
            price = view.findViewById(R.id.tv_price);
            description= view.findViewById(R.id.tv_description);
            item_layout = view.findViewById(R.id.item_layout);

        }
    }
}