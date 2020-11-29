package com.atul.android.userecom.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.atul.android.userecom.MainActivity;
import com.atul.android.userecom.databinding.SingleVbItemBinding;
import com.atul.android.userecom.model.Cart;
import com.atul.android.userecom.model.Product;

public class SingleVBProductViewBinder {
    SingleVbItemBinding binding;
    Product product;
    Cart cart;

    public SingleVBProductViewBinder(SingleVbItemBinding binding, Product product, Cart cart) {
        this.binding = binding;
        this.product = product;
        this.cart = cart;
    }

    public void bindData() {
        binding.addBtnSingleVb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.addVarientBasedProductToCart(product, product.varientsList.get(0));

                updateViews(1);
            }
        });

        binding.incrementBtnSingleVb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cart.addVarientBasedProductToCart(product, product.varientsList.get(0));

                updateViews(quantity);
            }
        });

        binding.decrementBtnSingleVb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cart.removeVarientBasedProductFromCart(product, product.varientsList.get(0));

                updateViews(quantity);
            }
        });

        if (cart.totalItemMap.containsKey(product.name + " " + product.varientsList.get(0).name)) {
            updateViews(cart.totalItemMap.get(product.name + " " + product.varientsList.get(0).name));
        }

    }

    private void updateViews(int quantity) {
        if (quantity == 1) {
            binding.addBtnSingleVb.setVisibility(View.GONE);
            binding.decrementBtnSingleVb.setVisibility(View.VISIBLE);
            binding.quantitySingleVb.setVisibility(View.VISIBLE);
            binding.incrementBtnSingleVb.setVisibility(View.VISIBLE);
        } else if (quantity == 0) {
            binding.addBtnSingleVb.setVisibility(View.VISIBLE);
            binding.decrementBtnSingleVb.setVisibility(View.GONE);
            binding.quantitySingleVb.setVisibility(View.GONE);
            binding.incrementBtnSingleVb.setVisibility(View.GONE);
        }

        binding.quantitySingleVb.setText("" + quantity);

        updateCheckOutSummary();

    }

    private void updateCheckOutSummary() {
        Context context = binding.getRoot().getContext();
        if (context instanceof MainActivity) {
            ((MainActivity) context).updateCheckOutSummary();
        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
