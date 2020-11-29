package com.atul.android.userecom.model;

import java.io.Serializable;
import java.util.List;

public class Inventory implements Serializable {
    public List<Product> productList;

    public Inventory() {
    }

    public Inventory(List<Product> productList) {
        this.productList = productList;
    }
}
