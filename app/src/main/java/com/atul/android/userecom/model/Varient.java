package com.atul.android.userecom.model;

import java.io.Serializable;

public class Varient implements Serializable {
    public String name;
    public int price;

    public Varient() {
    }

    public Varient(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - Rs." + price;
    }
}
