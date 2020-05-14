package com.srkptechnologies.ivm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddOrder extends AppCompatActivity {

    private EditText oCustomerName, oCustomerContact;
    private TextView oDate, oItemCount, oTotalCost;
    private Button oConfirm;

    private RecyclerView RCVList;
    private Product[] products = null;

    private OrderTemplate orderTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        oCustomerName = (EditText)findViewById(R.id.Order_customerName);
        oCustomerContact = (EditText) findViewById(R.id.Order_customerContact);
        oDate = (TextView) findViewById(R.id.Order_date);
        oItemCount = (TextView) findViewById(R.id.Order_itemCount);
        oTotalCost = (TextView) findViewById(R.id.Order_totalCost);
        oConfirm = (Button) findViewById(R.id.Order_confirm);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        oDate.setText(date);



        products = Product.allProducts(this);

        if (products == null || products.length == 0) {
            Toast.makeText(this, "Add Product First!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddOrder.this, Order.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {

            orderTemplate = new OrderTemplate(products);
            RCVList = (RecyclerView) findViewById(R.id.Order_Items_RCV);
            RCVList.setLayoutManager(new LinearLayoutManager(this));
            if (products == null) {
                Toast.makeText(this, "No products to add!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                RCVList.setAdapter(new OrderRCVListAdapter(AddOrder.this, products, orderTemplate));
            }
        }

        oConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emptyField(oCustomerName.getText().toString())) {
                    if (orderTemplate.getItems() != 0) {
                        orderTemplate.setCustomerName(oCustomerName.getText().toString());
                        orderTemplate.setCustomerContact(oCustomerContact.getText().toString());
                        orderTemplate.setOrderDate(oDate.getText().toString());

                        byte[] blob = OrderTemplate.makeByte(orderTemplate);

                        boolean set = OrderTemplate.addOrder(AddOrder.this, blob);
                        if (set) {
                            for (int i=0; i<products.length; i++) {
                                int[][] orderList = orderTemplate.getOrderList();
                                if (orderList[i][1] > 0) {
                                    int id = orderList[i][0];
                                    int lessStock = orderList[i][1];
                                    Product p = Product.findByID(AddOrder.this, id);
                                    int prevStock = p.getInStock();
                                    int cStock = prevStock - lessStock;
                                    Product.updateProductStock(AddOrder.this, id, cStock);
                                }
                            }
                            Toast.makeText(AddOrder.this, "Order Added!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddOrder.this, "Unable to add Order!", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(AddOrder.this, Order.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddOrder.this, "Add at least one item!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddOrder.this, "Enter Customer Name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean emptyField(String object) {
        Boolean flag = false;
        object = object.trim();

        if (object.isEmpty() || object.length() == 0 || object.equals("") || object == null) {
            flag = true;
        }

        return flag;
    }
}
