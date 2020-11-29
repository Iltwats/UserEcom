package com.atul.android.userecom.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.atul.android.userecom.MainActivity;
import com.atul.android.userecom.databinding.WbOrMultiWbItemBinding;
import com.atul.android.userecom.dialogs.VarientPickerDialog;
import com.atul.android.userecom.dialogs.WeightPickerDialog;
import com.atul.android.userecom.model.Cart;
import com.atul.android.userecom.model.Product;

public class WBProductViewBinder {
    WbOrMultiWbItemBinding binding;
    Product product;
    Cart cart;

    public WBProductViewBinder(WbOrMultiWbItemBinding binding, Product product, Cart cart) {
        this.binding = binding;
        this.product = product;
        this.cart = cart;
    }

    public void bindData() {
        binding.addBtnWb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupAddButton();
            }
        });

        binding.editBtnWb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupAddButton();
            }
        });

    }

    private void setupAddButton() {
        if (product.type == Product.WEIGHT_BASED) {
            showWeightBasedDialog();
        } else {
            showVarientBasedDialog();
        }
    }

    private void showWeightBasedDialog() {
        Context context = binding.getRoot().getContext();
        new WeightPickerDialog(context, product, cart).showDialog(new WeightPickerDialog.OnWeightPickedListener() {
            @Override
            public void onWeightPicked(int kg, int g) {
                updateQuantity(kg + " kg " + g + " g");
            }

            @Override
            public void onRemove() {
                hideViews();
            }
        });
    }

    private void showVarientBasedDialog() {
        Context context = binding.getRoot().getContext();

        new VarientPickerDialog(context, product, cart).showDialog(new VarientPickerDialog.OnVarientsPickedListener() {
            @Override
            public void onVarientsPicked(int quantity) {
                updateQuantity("" + quantity);
            }

            @Override
            public void onRemoveAll() {
                hideViews();
            }
        });
    }

    private void updateQuantity(String s) {
        binding.addBtnWb.setVisibility(View.GONE);
        binding.editBtnWb.setVisibility(View.VISIBLE);
        binding.quantityWb.setVisibility(View.VISIBLE);

        binding.quantityWb.setText(s);

        updateCheckOutSummary();
    }

    private void hideViews() {
        binding.addBtnWb.setVisibility(View.VISIBLE);
        binding.editBtnWb.setVisibility(View.GONE);
        binding.quantityWb.setVisibility(View.GONE);

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
