package com.srkptechnologies.ivm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderTemplate implements Serializable {
    private int Id;
    private String customerName;
    private String customerContact;
    private String orderDate;
    private String paidDate;
    private int items;
    private double total;

    private int[][] orderList;
    private int ListSize;

    public OrderTemplate(Product[] products) {
        this.ListSize = products.length;
        this.orderList = new int[ListSize][2];

        for (int i=0; i<ListSize; i++) {
            orderList[i][0] = products[i].getId();
            orderList[i][1] = 0;
        }
    }

    public void setId(int Id) { this.Id = Id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerContact(String customerContact) { this.customerContact = customerContact; }
    public void setItems(int items) { this.items = items; }
    public void setTotal(double total) { this.total = total; }
    public int getId() { return Id; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public String getCustomerName() { return customerName; }
    public String getCustomerContact() { return customerContact; }
    public int getItems() { return items; }
    public double getTotal() { return total; }

    public String getPaidDate() { return paidDate; }
    public void setPaidDate(String paidDate) { this.paidDate = paidDate; }

    public int[][] getOrderList() { return orderList; }
    public void setOrderList(int Id, int quantity) {
        ListSize = orderList.length;
        for (int i = 0; i<ListSize; i++) {
            if (orderList[i][0] == Id) {
                orderList[i][1] = quantity;
            }
        }
    }
    public void updateOrderList(int[][] orderList) { this.orderList = orderList; }

    public static byte[] makeByte(OrderTemplate orderTemplate) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(orderTemplate);
            byte[] orderAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(orderAsBytes);
            return orderAsBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static OrderTemplate readByte(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            OrderTemplate dataobj = (OrderTemplate) ois.readObject();
            return dataobj ;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OrderTemplate[] allOrders(Context context) {
        OrderTemplate[] o;

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM ivm_orders", new String[]{});

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();

            o = new OrderTemplate[cursor.getCount()];
            int c=0;

            do {
                o[c] = readByte(cursor.getBlob(1));
                o[c].setId(cursor.getInt(0));
                c++;
            } while (cursor.moveToNext());
        } else {
            o = null;
        }

        return o;
    }
    public static OrderTemplate findByID(Context context, int id) {
        OrderTemplate o = null;

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM ivm_orders WHERE _id = "+id, new String[]{});

        if (cursor != null) {
            cursor.moveToFirst();
            o = readByte(cursor.getBlob(1));
        }

        return o;
    }
    public static boolean addOrder(Context context, byte[] blob) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("orderList", blob);

        long result = database.insert("ivm_orders", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean updateOrder (Context context, int Id, byte[] blob) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("orderList", blob);

        database.update("ivm_orders", values, "_id = ?", new String[] { String.valueOf(Id) });

        return true;
    }
    public static boolean deleteOrder (Context context, int Id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("ivm_orders", "_id = ?", new String[] { String.valueOf(Id) });
        return true;
    }
    public static boolean paidOrder (Context context, int Id, byte[] blob) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put("orderList", blob);
        values.put("date", date);

        long result = database.insert("ivm_history", null, values);

        if (result == -1) {
            return false;
        } else {
            database.delete("ivm_orders", "_id = ?", new String[] { String.valueOf(Id) });
            return true;
        }
    }

    public static OrderTemplate[] allHistory(Context context) {
        OrderTemplate[] o;

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM ivm_history ORDER BY _id DESC", new String[]{});

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();

            o = new OrderTemplate[cursor.getCount()];
            int c=0;

            do {
                o[c] = readByte(cursor.getBlob(1));
                o[c].setId(cursor.getInt(0));
                o[c].setPaidDate(cursor.getString(2));
                c++;
            } while (cursor.moveToNext());
        } else {
            o = null;
        }

        return o;
    }
    public static OrderTemplate findHistoryByID(Context context, int id) {
        OrderTemplate o = null;

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM ivm_history WHERE _id = "+id, new String[]{});

        if (cursor != null) {
            cursor.moveToFirst();
            o = readByte(cursor.getBlob(1));
            o.setPaidDate(cursor.getString(2));
        }

        return o;
    }
    public static boolean deleteOrderHistory (Context context, int Id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("ivm_history", "_id = ?", new String[] { String.valueOf(Id) });
        return true;
    }

}
