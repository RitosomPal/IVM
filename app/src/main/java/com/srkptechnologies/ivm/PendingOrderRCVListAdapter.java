package com.srkptechnologies.ivm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class PendingOrderRCVListAdapter extends RecyclerView.Adapter<PendingOrderRCVListAdapter.PendingOrderRCVListViewHolder> {

    private Context context;
    private OrderTemplate[] orderTemplates;

    public  PendingOrderRCVListAdapter(Context context, OrderTemplate[] orderTemplates) {
        this.context = context;
        this.orderTemplates = orderTemplates;
    }

    @NonNull
    @Override
    public PendingOrderRCVListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pending_order_list_rcv_layout, parent, false);
        return new PendingOrderRCVListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderRCVListViewHolder holder, final int position) {
        holder.oCustomerName.setText(orderTemplates[position].getCustomerName());
        holder.oCustomerContact.setText(orderTemplates[position].getCustomerContact());
        holder.oTotal.setText("₹ "+String.valueOf(orderTemplates[position].getTotal()));
        holder.oDate.setText(String.valueOf(orderTemplates[position].getOrderDate()));
        holder.oView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewOrder.class);
                intent.putExtra("order_id", orderTemplates[position].getId());
                context.startActivity(intent);
            }
        });
        holder.oPaid.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                int id = orderTemplates[position].getId();
                Boolean set =  OrderTemplate.paidOrder(context,id, OrderTemplate.makeByte(orderTemplates[position]));

                if (set) {
                    Toast.makeText(context, "Find Order In History!", Toast.LENGTH_SHORT).show();
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

    public class PendingOrderRCVListViewHolder extends RecyclerView.ViewHolder {

        private TextView oCustomerName, oCustomerContact, oTotal, oDate;
        private Button oPaid, oView;

        public PendingOrderRCVListViewHolder(@NonNull View itemView) {
            super(itemView);
            oCustomerName = (TextView) itemView.findViewById(R.id.PendingOrderList_customerName);
            oCustomerContact = (TextView) itemView.findViewById(R.id.PendingOrderList_customerContact);
            oTotal = (TextView) itemView.findViewById(R.id.PendingOrderList_total);
            oDate = (TextView) itemView.findViewById(R.id.PendingOrderList_date);
            oPaid = (Button) itemView.findViewById(R.id.PendingOrderList_paid);
            oView = (Button) itemView.findViewById(R.id.PendingOrderList_view);
        }
    }
}
