package com.srkptechnologies.ivm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    private EditText product_Name, product_Price, product_InStock;
    private Button product_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        product_Name = (EditText) findViewById(R.id.product_add_name);
        product_Price = (EditText) findViewById(R.id.product_add_price);
        product_InStock = (EditText) findViewById(R.id.product_add_inStock);
        product_Add = (Button) findViewById(R.id.product_add);

        product_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!emptyField(product_Name.getText().toString()) &&  !emptyField(product_Price.getText().toString()) && !emptyField(product_InStock.getText().toString())) {
                    String Name = product_Name.getText().toString();
                    double Price = Double.parseDouble(product_Price.getText().toString());
                    int inStock = Integer.parseInt(product_InStock.getText().toString());

                    boolean set = Product.addProduct(AddProduct.this, Name, Price, inStock);
                    if (set) {
                        Toast.makeText(AddProduct.this, "Item Added!", Toast.LENGTH_SHORT).show();
                        product_Name.setText("");
                        product_Price.setText("");
                        product_InStock.setText("");
                    } else {
                        Toast.makeText(AddProduct.this, "Unable to add Item!", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddProduct.this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddProduct.this, "All Filds are required!", Toast.LENGTH_SHORT).show();
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
