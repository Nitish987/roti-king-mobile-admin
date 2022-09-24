package com.rotiking.admin.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.rotiking.admin.R;
import com.rotiking.admin.ToppingActivity;
import com.rotiking.admin.adapter.ToppingItemRecyclerAdapter;
import com.rotiking.admin.common.db.Database;
import com.rotiking.admin.models.Topping;
import com.rotiking.admin.utils.Promise;

import java.util.List;

public class ToppingsFragment extends Fragment {
    private View view;
    private FloatingActionButton addToppingBtn;
    private RecyclerView toppingsRV;
    private CircularProgressIndicator toppingsProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_toppings, container, false);

        addToppingBtn = view.findViewById(R.id.add_topping_btn);
        toppingsProgress = view.findViewById(R.id.toppings_progress);

        toppingsRV = view.findViewById(R.id.toppings_RV);
        toppingsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        toppingsRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        addToppingBtn.setOnClickListener(v -> {
            Topping topping = new Topping("", "", "", "", true, 0,0);
            Intent intent = new Intent(view.getContext(), ToppingActivity.class);
            intent.putExtra("TOPPING", topping);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Database.getToppingList(view.getContext(), new Promise<List<Topping>>() {
            @Override
            public void resolving(int progress, String msg) {
                toppingsProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void resolved(List<Topping> o) {
                ToppingItemRecyclerAdapter adapter = new ToppingItemRecyclerAdapter(o);
                toppingsRV.setAdapter(adapter);
            }

            @Override
            public void reject(String err) {
                Toast.makeText(view.getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }
}