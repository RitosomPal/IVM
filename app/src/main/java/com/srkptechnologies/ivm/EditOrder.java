package com.srkptechnologies.ivm;

import androidx.annotation.RequiresApi;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditOrder extends AppCompatActivity {

    private EditText oCustomerName, oCustomerContact;
    private TextView oDate, oItemCount, oTotalCost;
    private Button oUpdate, oDelete;

    private RecyclerView RCVList;
    private Product[] products = null;

    private OrderTemplate orderTemplate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        final int orderId = getIntent().getIntExtra("order_id", 0);

        oCustomerName = (EditText) findViewById(R.id.Order_Edit_customerName);
        oCustomerContact = (EditText) findViewById(R.id.Order_Edit_customerContact);
        oDate = (TextView) findViewById(R.id.Order_Edit_date);
        oItemCount = (TextView) findViewById(R.id.Order_Edit_itemCount);
        oTotalCost = (TextView) findViewById(R.id.Order_Edit_totalCost);
        oUpdate = (Button) findViewById(R.id.Order_Edit_update);
        oDelete = (Button) findViewById(R.id.Order_Edit_delete);

        products = Product.allProducts(this);
        orderTemplate = OrderTemplate.findByID(this, orderId);


        if (orderTemplate == null) {
            Toast.makeText(this, "Order Not Found!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            oCustomerName.setText(orderTemplate.getCustomerName());
            oCustomerContact.setText(orderTemplate.getCustomerContact());
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            oDate.setText(date.format(formatter));
            oTotalCost.setText("₹ " +String.valueOf(orderTemplate.getTotal()));
            oItemCount.setText(String.valueOf(orderTemplate.getItems()));

            RCVList = (RecyclerView) findViewById(R.id.Order_Edit_Items_RCV);
            RCVList.setLayoutManager(new LinearLayoutManager(this));
            if (products == null || products.length == 0) {
                Toast.makeText(this, "Add Product First!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrder.this, Order.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                RCVList.setAdapter(new OrderRCVListEditAdopter(EditOrder.this, products, orderTemplate));
            }

            oUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!emptyField(oCustomerName.getText().toString())) {
                        if (orderTemplate.getItems() != 0) {
                            orderTemplate.setCustomerName(oCustomerName.getText().toString());
                            orderTemplate.setCustomerContact(oCustomerContact.getText().toString());
                            orderTemplate.setOrderDate(oDate.getText().toString());

                            byte[] blob = OrderTemplate.makeByte(orderTemplate);

                            boolean set = OrderTemplate.updateOrder(EditOrder.this, orderId, blob);

                            if (set) {
                                int[][] currentList = orderTemplate.getOrderList();
                                for (int i=0; i<products.length; i++) {

                                    int id = products[i].getId();
                                    int stock = products[i].getInStock();

                                    Product.updateProductStock(EditOrder.this, id, stock);
                                }
                            } else {
                                Toast.makeText(EditOrder.this, "Unable to Update Order!", Toast.LENGTH_SHORT).show();
                            }

                            Intent intent = new Intent(EditOrder.this, Order.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditOrder.this, "Add at least one item!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditOrder.this, "Enter Customer Name!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            oDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean set = OrderTemplate.deleteOrder(EditOrder.this, orderId);
                    if (set) {
                        Toast.makeText(EditOrder.this, "Order Deleted!", Toast.LENGTH_SHORT).show();
                        for(int i=0; i<products.length; i++) {
                            int [][] ol = orderTemplate.getOrderList();
                            Product.updateProductStock(EditOrder.this,ol[i][0],products[i].getInStock()+ol[i][1]);
                        }
                    } else {
                        Toast.makeText(EditOrder.this, "Unable to delete order!", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(EditOrder.this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

        }

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
