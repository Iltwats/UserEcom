package com.atul.android.userecom;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atul.android.userecom.databinding.ActivityCartBinding;
import com.atul.android.userecom.databinding.CartItemViewBinding;
import com.atul.android.userecom.model.Cart;
import com.atul.android.userecom.model.CartItem;

import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        cart = (Cart) intent.getSerializableExtra("data");

        showCartItems();

        showItemsAndPrice();

    }


    private void showCartItems() {
        for (Map.Entry<String, CartItem> map : cart.map.entrySet()) {
            CartItemViewBinding b = CartItemViewBinding.inflate(
                    getLayoutInflater()
            );

            b.cartItemName.setText("" + map.getKey());

            b.cartItemPrice.setText("Rs. " + map.getValue().price);

            if (map.getValue().name.contains("kg")) {
                b.cartItemWeight.setText((int) (map.getValue().quantity) + " x Rs. " + (map.getValue().price) / ((int) (map.getValue().quantity)));
            } else {
                b.cartItemWeight.setText((int) (map.getValue().quantity) + "kg x Rs. " + (map.getValue().price) / ((int) (map.getValue().quantity)) + "/kg");
            }

            setupDeleteButton(b, map.getKey(), map.getValue());


            binding.cartItems.addView(b.getRoot());
        }
    }

    private void setupDeleteButton(CartItemViewBinding b, String key, CartItem value) {
        b.deleteCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cart.removeItemWithKey(key, value);

                binding.cartItems.removeView(b.getRoot());

                showItemsAndPrice();
            }
        });
    }

    private void showItemsAndPrice() {
        binding.items.setText("Items : " + cart.noOfItems);
        binding.price.setText("Price : Rs. " + cart.totalPrice);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent latestCartIntent = new Intent();
            latestCartIntent.putExtra("new", cart);
            setResult(RESULT_OK, latestCartIntent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
