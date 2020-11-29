package com.atul.android.userecom.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.atul.android.userecom.databinding.VarientItemBinding;
import com.atul.android.userecom.databinding.VarientPickerDialogBinding;
import com.atul.android.userecom.model.Cart;
import com.atul.android.userecom.model.Product;
import com.atul.android.userecom.model.Varient;

public class VarientPickerDialog {
    VarientPickerDialogBinding binding;
    Context context;
    Product product;
    Cart cart;

    public VarientPickerDialog(Context context, Product product, Cart cart) {
        this.context = context;
        this.product = product;
        this.cart = cart;
        binding = VarientPickerDialogBinding.inflate(LayoutInflater.from(context));
    }

    public void showDialog(OnVarientsPickedListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(product.name)
                .setCancelable(false)
                .setView(binding.getRoot())
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (cart.totalItemMap.containsKey(product.name)) {
                            int quantity = cart.totalItemMap.get(product.name);
                            if (quantity > 0) {
                                listener.onVarientsPicked(quantity);
                            }
                        } else {
                            listener.onRemoveAll();
                        }
                    }
                })
                .setNegativeButton("REMOVE ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cart.removeAllVarientsProductFromCart(product);
                        listener.onRemoveAll();
                    }
                })
                .show();

        setupVarientsInDialog();

    }

    private void setupVarientsInDialog() {
        for(Varient varient : product.varientsList) {
            VarientItemBinding b = VarientItemBinding.inflate(
                    LayoutInflater.from(context),
                    binding.getRoot(),
                    true
            );

            b.variantName.setText(varient.name + " - Rs. " + varient.price);

            setupVarientsButtons(b, varient);
            showPreviousData(b, varient);
        }
    }

    private void showPreviousData(VarientItemBinding b, Varient varient) {
        int quantity = cart.getVarientQuantityFromCart(product, varient);
        if (quantity > 0) {
            b.qty.setText("" + quantity);
            b.qty.setVisibility(View.VISIBLE);
            b.remove.setVisibility(View.VISIBLE);
        }
    }

    private void setupVarientsButtons(VarientItemBinding b, Varient varient) {
        b.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.addVarientBasedProductToCart(product, varient);

                b.qty.setText("" + qty);
                if (qty == 1) {
                    b.qty.setVisibility(View.VISIBLE);
                    b.remove.setVisibility(View.VISIBLE);
                }
            }
        });

        b.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.removeVarientBasedProductFromCart(product, varient);

                b.qty.setText("" + qty);
                if (qty == 0) {
                    b.qty.setVisibility(View.GONE);
                    b.remove.setVisibility(View.GONE);
                }
            }
        });

    }

    public interface OnVarientsPickedListener {
        void onVarientsPicked(int quantity);
        void onRemoveAll();
    }
}
