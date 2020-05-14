package com.srkptechnologies.ivm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryRCVListAdapter extends RecyclerView.Adapter<OrderHistoryRCVListAdapter.OrderHistoryRCVListViewHolder> {

    private Context context;
    private OrderTemplate[] orderTemplates;

    public OrderHistoryRCVListAdapter(Context context, OrderTemplate[] orderTemplates) {
        this.context = context;
        this.orderTemplates = orderTemplates;
    }

    @NonNull
    @Override
    public OrderHistoryRCVListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_history_list_rcv_layout, parent, false);
        return new OrderHistoryRCVListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryRCVListViewHolder holder, final int position) {
        holder.oCustomerName.setText(orderTemplates[position].getCustomerName());
        holder.oCustomerContact.setText(orderTemplates[position].getCustomerContact());
        holder.oTotal.setText("₹ "+String.valueOf(orderTemplates[position].getTotal()));
        holder.oDate.setText(String.valueOf(orderTemplates[position].getOrderDate()));
        holder.oView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewOrderHistory.class);
                intent.putExtra("order_id", orderTemplates[position].getId());
                context.startActivity(intent);
            }
        });
        holder.oDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = orderTemplates[position].getId();
                Boolean set =  OrderTemplate.deleteOrderHistory(context, id);

                if (set) {
                    Toast.makeText(context, "Order History Deleted!", Toast.LENGTH_SHORT).show();
                    Intent intent = ((Activity)context).getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ((Activity)context).finish();
                    ((Activity)context).overridePendingTransition(0, 0);
                    ((Activity)context).startActivity(intent);
                    ((Activity)context).overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(context, "Some Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderTemplates.length;
    }

    public class OrderHistoryRCVListViewHolder extends RecyclerView.ViewHolder {

        private TextView oCustomerName, oCustomerContact, oTotal, oDate;
        private Button oDelete, oView;

        public OrderHistoryRCVListViewHolder(@NonNull View itemView) {
            super(itemView);
            oCustomerName = (TextView) itemView.findViewById(R.id.OrderHistoryList_customerName);
            oCustomerContact = (TextView) itemView.findViewById(R.id.OrderHistoryList_customerContact);
            oTotal = (TextView) itemView.findViewById(R.id.OrderHistoryList_total);
            oDate = (TextView) itemView.findViewById(R.id.OrderHistoryList_date);
            oDelete = (Button) itemView.findViewById(R.id.OrderHistoryList_delete);
            oView = (Button) itemView.findViewById(R.id.OrderHistoryList_view);
        }
    }
}
