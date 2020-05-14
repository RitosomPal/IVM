package com.srkptechnologies.ivm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ViewOrder extends AppCompatActivity {

    private RecyclerView RCVList;
    private Product[] products = null;
    private OrderTemplate orderTemplate;

    private TextView oName, oContact, oDate, oItem, oTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        oName = (TextView) findViewById(R.id.Order_View_customerName);
        oContact = (TextView) findViewById(R.id.Order_View_customerContact);
        oItem = (TextView) findViewById(R.id.Order_View_itemCount);
        oDate = (TextView) findViewById(R.id.Order_View_date);
        oTotal = (TextView) findViewById(R.id.Order_View_totalCost);

        final int orderId = getIntent().getIntExtra("order_id", 0);

        products = Product.allProducts(this);
        orderTemplate = OrderTemplate.findByID(this, orderId);

        if (orderTemplate == null) {
            Toast.makeText(this, "Order Not Found!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            RCVList = (RecyclerView) findViewById(R.id.Order_View_Items_RCV);
            RCVList.setLayoutManager(new LinearLayoutManager(this));
            if (products == null || products.length == 0) {
                Toast.makeText(this, "Add Product First!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewOrder.this, Order.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                RCVList.setAdapter(new OrderViewRCVAdapter(ViewOrder.this, products, orderTemplate));
            }

            oName.setText(orderTemplate.getCustomerName());
            oContact.setText(orderTemplate.getCustomerContact());
            oItem.setText(String.valueOf(orderTemplate.getItems()));
            oDate.setText(orderTemplate.getOrderDate());
            oTotal.setText("₹ "+String.valueOf(orderTemplate.getTotal()));
        }
    }
}
