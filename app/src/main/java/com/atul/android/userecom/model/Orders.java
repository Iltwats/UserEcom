package com.atul.android.userecom.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Orders {
    public String orderID;
    public Timestamp orderTime;
    public List<CartItem> orderItems;
    public int action;
    public int total_price, total_items;

    public Orders() {
    }

    public Orders(String orderID, Timestamp orderTime, List<CartItem> orderItems, int action, int total_price, int total_items) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.orderItems = orderItems;
        this.action = action;
        this.total_price = total_price;
        this.total_items = total_items;
    }

    public static class OrderStatus {

        public static final int PLACED = 1 // Initially (U)
                , DELIVERED = 0, DECLINED = -1;     //(A)

    }

}
