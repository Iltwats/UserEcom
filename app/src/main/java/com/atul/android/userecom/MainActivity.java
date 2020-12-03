package com.atul.android.userecom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.atul.android.userecom.adapter.ProductsAdapter;
import com.atul.android.userecom.constants.Constants;
import com.atul.android.userecom.databinding.ActivityMainBinding;
import com.atul.android.userecom.model.Cart;
import com.atul.android.userecom.model.Inventory;
import com.atul.android.userecom.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Cart cart = new Cart();
    private ProductsAdapter adapter;
    private List<Product> list;
    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.USER);
        setup();
        fetchProductsListFromCloudFirestore();

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.putExtra("data", cart);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Cart newCart = (Cart) data.getSerializableExtra("new");

                cart.changeCart(newCart);

                adapter.notifyDataSetChanged();

                updateCheckOutSummary();
            }
        }

    }

    private void setup() {
        app = (MyApp) getApplicationContext();
    }

    private void fetchProductsListFromCloudFirestore() {

        if (app.isOffline()) {
            app.showToast(MainActivity.this, "No Internet!");
            return;
        }

        app.showLoadingDialog(this);

        app.db.collection(Constants.INVENTORY).document(Constants.PRODUCTS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Inventory inventory = documentSnapshot.toObject(Inventory.class);
                            list = inventory.productList;
                        } else {
                            list = new ArrayList<>();
                        }
                        setupList();
                        app.hideLoadingDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        app.hideLoadingDialog();
                    }
                });

    }

    private void setupList() {
        adapter = new ProductsAdapter(this, list, cart);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecor);

        binding.recyclerView.setAdapter(adapter);
    }

    public void updateCheckOutSummary() {
        if (cart.noOfItems == 0) {
            binding.checkout.setVisibility(View.GONE);
        } else {
            binding.checkout.setVisibility(View.VISIBLE);
            binding.cartSummary.setText("Total: Rs. " + cart.totalPrice + "\n" + cart.noOfItems + " items");
        }
    }


}