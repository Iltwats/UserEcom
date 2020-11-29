package com.atul.android.userecom.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    public String name;
    public float quantity;
    public int price;

    public CartItem() {
    }

    public CartItem(String name, float quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem(String name, int price) {
        this.name = name;
        this.price = price;
        quantity = 1;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
