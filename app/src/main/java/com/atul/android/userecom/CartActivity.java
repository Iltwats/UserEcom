package com.atul.android.userecom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.atul.android.userecom.constants.Constants;
import com.atul.android.userecom.databinding.ActivityCartBinding;
import com.atul.android.userecom.databinding.CartItemViewBinding;
import com.atul.android.userecom.fcm.FCMSender;
import com.atul.android.userecom.fcm.MessageFormatter;
import com.atul.android.userecom.model.Cart;
import com.atul.android.userecom.model.CartItem;
import com.atul.android.userecom.model.Orders;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    Cart cart;
    private MyApp app;
    Timestamp timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        cart = (Cart) intent.getSerializableExtra("data");
        setup();

        showCartItems();

        showItemsAndPrice();

    }
    private void setup() {
        app = (MyApp) getApplicationContext();
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

    public void placeOrder(View view) {
        List<CartItem> orderItems = new ArrayList<>();

        for (Map.Entry<String, CartItem> map : cart.map.entrySet()) {
            orderItems.add(map.getValue());
        }
        long seed = System.currentTimeMillis();
        String orderID = new Random(seed).nextInt(1000000000) + 529430187 + "" + cart.noOfItems + "" + cart.totalPrice;
        Orders newOrder = new Orders(
                orderID,
                Timestamp.now(),
                orderItems,
                Orders.OrderStatus.PLACED,
                cart.totalPrice,
                cart.noOfItems
        );
        if (!orderItems.isEmpty()) {
            app.db.collection(Constants.ORDER).document(orderID)
                    .set(newOrder)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CartActivity.this, "Order Successfully Placed!", Toast.LENGTH_SHORT).show();
                            new FCMSender().send(MessageFormatter.getSampleMessage("admin", "New Order!", "new order with " + orderID ), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("qwerty","Failed to send notification.");
                                    Toast.makeText(CartActivity.this, "note Placed!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("ytrewq","Success to send notification.");

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CartActivity.this, "Failed to place order!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(CartActivity.this, "Failed to place order! Cart is empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
