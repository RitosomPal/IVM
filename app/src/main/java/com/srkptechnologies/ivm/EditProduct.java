package com.srkptechnologies.ivm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProduct extends AppCompatActivity {

    private EditText product_Name, product_Price, product_InStock;
    private Button product_Edit, product_Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        final int productId = getIntent().getIntExtra("product_id",0);

        product_Name = (EditText) findViewById(R.id.product_name);
        product_Price = (EditText) findViewById(R.id.product_price);
        product_InStock = (EditText) findViewById(R.id.product_inStock);
        product_Edit = (Button) findViewById(R.id.product_edit);
        product_Delete = (Button) findViewById(R.id.product_delete);

        Product p = Product.findByID(this, productId);

        if (p == null) {
            Toast.makeText(this, "Product Not Found!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            product_Name.setText(p.getName());
            product_Price.setText(String.valueOf(p.getPrice()));
            product_InStock.setText(String.valueOf(p.getInStock()));

            product_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!emptyField(product_Name.getText().toString()) &&  !emptyField(product_Price.getText().toString()) && !emptyField(product_InStock.getText().toString())) {

                        String Name = product_Name.getText().toString();
                        double Price = Double.parseDouble(product_Price.getText().toString());
                        int inStock = Integer.parseInt(product_InStock.getText().toString());

                        boolean set = Product.updateProduct(EditProduct.this, productId, Name, Price, inStock);
                        if (set) {
                            Toast.makeText(EditProduct.this, "Product Updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProduct.this, "Unable to update product!", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(EditProduct.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditProduct.this, "All Filds are required!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            product_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean set = Product.deleteProduct(EditProduct.this, productId);
                    if (set) {
                        Toast.makeText(EditProduct.this, "Product Deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProduct.this, "Unable to delete product!", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(EditProduct.this, Dashboard.class);
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
