package com.srkptechnologies.ivm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewRCVAdapter extends RecyclerView.Adapter<OrderViewRCVAdapter.OrderViewRCVViewHolder> {

    Context context;
    Product[] products;
    private int[][] OrderList;

    public OrderViewRCVAdapter(Context context, Product[] products, OrderTemplate orderTemplate) {
        int[][] temp = orderTemplate.getOrderList();
        int totlaCount = 0;

        for (int i=0; i<temp.length; i++) {
            if (temp[i][1] > 0) {
                totlaCount++;
            }
        }

        OrderList = new int[totlaCount][2];
        totlaCount = 0;

        for (int i=0; i<temp.length; i++) {
            if (temp[i][1] > 0) {
                OrderList[totlaCount][0] = temp[i][0];
                OrderList[totlaCount++][1] = temp[i][1];
            }
        }

        this.products = products;
    }

    @NonNull
    @Override
    public OrderViewRCVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rcvlist_order_view_layout, parent,false);
        return new OrderViewRCVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewRCVViewHolder holder, int position) {
        int id = OrderList[position][0];
        int quantity  = OrderList[position][1];
        String name = "";
        double price = 0;
        double total = 0;

        for (int i=0; i < products.length; i++) {
            if (products[i].getId() == id) {
                name = products[i].getName();
                price = products[i].getPrice();
                break;
            }
        }

        total = price * quantity;

        holder.ovName.setText(name);
        holder.ovPrice.setText(String.valueOf(price));
        holder.ovQuantity.setText(String.valueOf(quantity));
        holder.ovTotal.setText(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return OrderList.length;
    }

    public class OrderViewRCVViewHolder extends RecyclerView.ViewHolder {

        private TextView ovName, ovPrice, ovQuantity, ovTotal;

        public OrderViewRCVViewHolder(@NonNull View itemView) {
            super(itemView);
            ovName = (TextView) itemView.findViewById(R.id.Order_View_RCV_Name);
            ovPrice = (TextView) itemView.findViewById(R.id.Order_View_RCV_Price);
            ovQuantity = (TextView) itemView.findViewById(R.id.Order_View_RCV_Quantity);
            ovTotal = (TextView) itemView.findViewById(R.id.Order_View_RCV_Total);
        }
    }
}
