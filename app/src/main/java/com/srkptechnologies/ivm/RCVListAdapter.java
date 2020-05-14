package com.srkptechnologies.ivm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RCVListAdapter extends RecyclerView.Adapter<RCVListAdapter.RCVListViewHolder> {

    Context context;
    Product[] products;

    public RCVListAdapter(Context context, Product[] products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RCVListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rcvlist_layout, parent, false);
        return new RCVListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCVListViewHolder holder, final int position) {

        String name = products[position].getName();
        String price = "₹ "+String.valueOf(products[position].getPrice());
        String inStock = "In Stock: "+String.valueOf(products[position].getInStock());

        holder.pName.setText(name);
        holder.pPrice.setText(price);
        holder.pInStock.setText(inStock);
        holder.pEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProduct.class);
                intent.putExtra("product_id", products[position].getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.length;
    }

    public class RCVListViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        TextView pName, pPrice, pInStock;
        ImageButton pEdit;

        public RCVListViewHolder(@NonNull View itemView) {
            super(itemView);
            pImage = (ImageView) itemView.findViewById(R.id.RCVList_product_image);
            pName = (TextView) itemView.findViewById(R.id.RCVList_product_name);
            pPrice = (TextView) itemView.findViewById(R.id.RCVList_product_price);
            pInStock = (TextView) itemView.findViewById(R.id.RCVList_product_inStock);
            pEdit = (ImageButton) itemView.findViewById(R.id.RCVList_product_edit);
        }
    }
}
