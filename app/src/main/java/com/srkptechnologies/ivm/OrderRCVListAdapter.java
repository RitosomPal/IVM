package com.srkptechnologies.ivm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRCVListAdapter extends RecyclerView.Adapter<OrderRCVListAdapter.OrderRCVListViewHolder> {

    Context context;
    Product[] products;
    int totla_item = 0;
    double total_price = 0;
    private TextView oItemCount, oTotalCost;
    private OrderTemplate orderTemplate;

    public OrderRCVListAdapter(Context context, Product[] products, OrderTemplate orderTemplate) {
        this.context = context;
        this.products = products;
        this.orderTemplate = orderTemplate;
        this.oItemCount = (TextView) ((Activity)context).findViewById(R.id.Order_itemCount);
        this.oTotalCost = (TextView) ((Activity)context).findViewById(R.id.Order_totalCost);
    }

    @NonNull
    @Override
    public OrderRCVListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_item_rcv_layout, parent, false);
        return new OrderRCVListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderRCVListViewHolder holder, final int position) {
        String name = products[position].getName();
        String price = "₹ "+String.valueOf(products[position].getPrice());
        String inStock = String.valueOf(products[position].getInStock());
        holder.pName.setText(name);
        holder.pPrice.setText(price);
        holder.pInStock.setText(inStock);
        holder.pQuantity.setMinValue(0);
        holder.pQuantity.setMaxValue(products[position].getInStock());
        holder.pQuantity.setWrapSelectorWheel(true);
        holder.pQuantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                totla_item += i1 - i;
                total_price += products[position].getPrice() * (i1 - i);
                oItemCount.setText(String.valueOf(totla_item));
                oTotalCost.setText(String.valueOf(total_price));
                orderTemplate.setItems(totla_item);
                orderTemplate.setTotal(total_price);
                orderTemplate.setOrderList(products[position].getId(), i1);
            }
        });

    }

    @Override
    public int getItemCount() { return products.length; }

    public class OrderRCVListViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        TextView pName, pPrice, pInStock;
        NumberPicker pQuantity;

        public OrderRCVListViewHolder(@NonNull View itemView) {
            super(itemView);
            pImage = (ImageView) itemView.findViewById(R.id.OrderRCVList_product_image);
            pName = (TextView) itemView.findViewById(R.id.OrderRCVList_product_name);
            pPrice = (TextView) itemView.findViewById(R.id.OrderRCVList_product_price);
            pInStock = (TextView) itemView.findViewById(R.id.OrderRCVList_product_inStock);
            pQuantity = (NumberPicker) itemView.findViewById(R.id.OrderRCVList_product_quantity);
        }
    }
}
