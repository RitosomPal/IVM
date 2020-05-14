package com.srkptechnologies.ivm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllOrderRCVListAdapter extends RecyclerView.Adapter<AllOrderRCVListAdapter.AllOrderRCVViewHolder> {

    private Context context;
    private OrderTemplate[] orderTemplates;

    public AllOrderRCVListAdapter(Context context, OrderTemplate[] orderTemplates) {
        this.context = context;
        this.orderTemplates = orderTemplates;
    }

    @NonNull
    @Override
    public AllOrderRCVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_list_rcv_layout, parent, false);
        return new AllOrderRCVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrderRCVViewHolder holder, final int position) {
        holder.oCustomerName.setText(orderTemplates[position].getCustomerName());
        holder.oCustomerContact.setText(orderTemplates[position].getCustomerContact());
        holder.oTotal.setText("₹ "+String.valueOf(orderTemplates[position].getTotal()));
        holder.oDate.setText(String.valueOf(orderTemplates[position].getOrderDate()));
        holder.oEditOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditOrder.class);
                intent.putExtra("order_id", orderTemplates[position].getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderTemplates.length;
    }

    public class AllOrderRCVViewHolder extends RecyclerView.ViewHolder {

        private TextView oCustomerName, oCustomerContact, oTotal, oDate;
        private ImageButton oEditOrder;

        public AllOrderRCVViewHolder(@NonNull View itemView) {
            super(itemView);
            oCustomerName = (TextView) itemView.findViewById(R.id.OrderList_customerName);
            oCustomerContact = (TextView) itemView.findViewById(R.id.OrderList_customerContact);
            oTotal = (TextView) itemView.findViewById(R.id.OrderList_total);
            oDate = (TextView) itemView.findViewById(R.id.OrderList_date);
            oEditOrder = (ImageButton) itemView.findViewById(R.id.OrderList_edit);
        }
    }
}
