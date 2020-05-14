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

public class OrderRCVListEditAdopter extends RecyclerView.Adapter<OrderRCVListEditAdopter.OrderRCVListEditViewHolder> {

    Context context;
    Product[] products;
    int totla_item = 0;
    double total_price = 0;
    private TextView oItemCount, oTotalCost;
    private OrderTemplate orderTemplate;
    private int[][] OrderList;
    int ps = 0;

    public  OrderRCVListEditAdopter(Context context, Product[] products, OrderTemplate orderTemplate) {
        this.context = context;
        this.products = products;
        this.orderTemplate = orderTemplate;
        this.oItemCount = (TextView) ((Activity)context).findViewById(R.id.Order_Edit_itemCount);
        this.oTotalCost = (TextView) ((Activity)context).findViewById(R.id.Order_Edit_totalCost);
        this.OrderList = orderTemplate.getOrderList();
        this.totla_item = orderTemplate.getItems();
        this.total_price = orderTemplate.getTotal();

        if(OrderList.length != products.length) {
            OrderTemplate temp = new OrderTemplate(products);
            int [][] newOrderList = temp.getOrderList();
            for (int i=0; i<OrderList.length; i++) {
                if (OrderList[i][0] == newOrderList[i][0]) {
                    newOrderList[i][1] = OrderList[i][1];
                }
            }
            orderTemplate.updateOrderList(newOrderList);
            this.OrderList = orderTemplate.getOrderList();
        }
    }

    @NonNull
    @Override
    public OrderRCVListEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_item_rcv_layout, parent, false);
        return new OrderRCVListEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderRCVListEditViewHolder holder, final int position) {
        String name = products[position].getName();
        String price = "₹ "+String.valueOf(products[position].getPrice());
        String inStock = String.valueOf(products[position].getInStock());
        int stock = 0;

        holder.pName.setText(name);
        holder.pPrice.setText(price);
        holder.pInStock.setText(inStock);
        holder.pQuantity.setMinValue(0);
        for (int i=0; i<OrderList.length; i++) {
            if (OrderList[i][0] == products[position].getId()) {
                stock = products[position].getInStock()+OrderList[i][1];
                holder.pQuantity.setMaxValue(stock);
                holder.pQuantity.setValue(OrderList[i][1]);
            }
        }
        final int updated_stock = stock;
        holder.pQuantity.setWrapSelectorWheel(true);
        holder.pQuantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                totla_item += i1 - i;
                total_price += products[position].getPrice() * (i1 - i);
                oItemCount.setText(String.valueOf(totla_item));
                oTotalCost.setText("₹ "+String.valueOf(total_price));
                products[position].setInStock(ps);
                orderTemplate.setItems(totla_item);
                orderTemplate.setTotal(total_price);
                orderTemplate.setOrderList(products[position].getId(), i1);
                int cus = updated_stock-i1;
                products[position].setInStock(cus);
            }
        });
    }

    @Override
    public int getItemCount() { return products.length; }

    public class OrderRCVListEditViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        TextView pName, pPrice, pInStock;
        NumberPicker pQuantity;

        public OrderRCVListEditViewHolder(@NonNull View itemView) {
            super(itemView);
            pImage = (ImageView) itemView.findViewById(R.id.OrderRCVList_product_image);
            pName = (TextView) itemView.findViewById(R.id.OrderRCVList_product_name);
            pPrice = (TextView) itemView.findViewById(R.id.OrderRCVList_product_price);
            pInStock = (TextView) itemView.findViewById(R.id.OrderRCVList_product_inStock);
            pQuantity = (NumberPicker) itemView.findViewById(R.id.OrderRCVList_product_quantity);
        }
    }
}
