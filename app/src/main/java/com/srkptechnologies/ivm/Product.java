package com.srkptechnologies.ivm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Product {

    private int Id;
    private String Name;
    private double Price;
    private int InStock;

    public Product() {}
    public Product(int Id, String Name, double Price, int InStock) {
        this.Id = Id;
        this.Name = Name;
        this.Price = Price;
        this.InStock = InStock;
    }

    public void setId(int Id) { this.Id = Id; }
    public void setName(String Name) { this.Name = Name; }
    public void setPrice(double Price) { this.Price = Price; }
    public void setInStock(int InStock) { this.InStock = InStock; }
    public int getId() { return Id; }
    public String getName() { return Name; }
    public double getPrice() { return Price; }
    public int getInStock() { return InStock; }

    public static Product[] allProducts(Context context) {
        Product[] p;

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM ivm_products", new String[]{});

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            p = new Product[cursor.getCount()];
            int c=0;

            do {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                int inStock = cursor.getInt(3);
                p[c++] = new Product(id, name, price, inStock);
            } while (cursor.moveToNext());
        } else {
            p = null;
        }

        return p;
    }
    public static Product findByID(Context context, int id) {
        Product p = null;

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM ivm_products WHERE _id = "+id, new String[]{});

        if (cursor != null) {
            cursor.moveToFirst();
            String name = cursor.getString(1);
            double price = cursor.getDouble(2);
            int inStock = cursor.getInt(3);
            p = new Product(id, name, price, inStock);
        }

        return p;
    }
    public static boolean addProduct(Context context, String Name, double Price, int InStock) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", Name);
        values.put("price", Price);
        values.put("inStock", InStock);

        long result = database.insert("ivm_products", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean updateProduct (Context context, int Id, String Name, double Price, int InStock) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", Name);
        values.put("price", Price);
        values.put("inStock", InStock);

        database.update("ivm_products", values, "_id = ?", new String[] { String.valueOf(Id) });

        return true;
    }
    public static boolean updateProductStock (Context context, int Id, int InStock) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("inStock", InStock);

        database.update("ivm_products", values, "_id = ?", new String[] { String.valueOf(Id) });

        return true;
    }
    public static boolean deleteProduct (Context context, int Id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("ivm_products", "_id = ?", new String[] { String.valueOf(Id) });
        return true;
    }
}
