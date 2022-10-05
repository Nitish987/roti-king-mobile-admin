package com.rotiking.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rotiking.admin.common.auth.Auth;
import com.rotiking.admin.tabs.DispatcherFragment;
import com.rotiking.admin.tabs.FoodsFragment;
import com.rotiking.admin.tabs.OrdersFragment;
import com.rotiking.admin.tabs.ProfileFragment;
import com.rotiking.admin.tabs.ToppingsFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView tabs;
    private ImageButton customerQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Auth.isUserAuthenticated(this)) {
            toLoginActivity();
        }

        tabs = findViewById(R.id.tabs);
        customerQuery = findViewById(R.id.help_customer);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onStart() {
        super.onStart();
        Auth.setAuthStateListener(firebaseAuth -> {
            if (!Auth.isUserAuthenticated(this)) {
                toLoginActivity();
            }
        });

        tabs.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.orders:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
                    break;
                case R.id.foods:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoodsFragment()).commit();
                    break;
                case R.id.toppings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToppingsFragment()).commit();
                    break;
                case R.id.dispatchers:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DispatcherFragment()).commit();
                    break;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    break;
            }
            return true;
        });

        customerQuery.setOnClickListener(view -> {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}