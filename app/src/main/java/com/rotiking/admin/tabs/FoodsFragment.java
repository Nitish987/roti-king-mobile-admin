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
import com.rotiking.admin.FoodActivity;
import com.rotiking.admin.R;
import com.rotiking.admin.adapter.FoodItemRecyclerAdapter;
import com.rotiking.admin.common.db.Database;
import com.rotiking.admin.models.Food;
import com.rotiking.admin.utils.Promise;

import java.util.List;

public class FoodsFragment extends Fragment {
    private View view;
    private FloatingActionButton addFoodBtn;
    private RecyclerView foodsRV;
    private CircularProgressIndicator foodsProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foods, container, false);

        addFoodBtn = view.findViewById(R.id.add_food_btn);
        foodsProgress = view.findViewById(R.id.foods_progress);

        foodsRV = view.findViewById(R.id.foods_RV);
        foodsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        foodsRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        addFoodBtn.setOnClickListener(v -> {
            Food food = new Food("", "", "", "", "", "", "", true, 0, 0, 0);
            Intent intent = new Intent(view.getContext(), FoodActivity.class);
            intent.putExtra("FOOD", food);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Database.getFoodList(view.getContext(), new Promise<List<Food>>() {
            @Override
            public void resolving(int progress, String msg) {
                foodsProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void resolved(List<Food> o) {
                FoodItemRecyclerAdapter adapter = new FoodItemRecyclerAdapter(o);
                foodsRV.setAdapter(adapter);
            }

            @Override
            public void reject(String err) {
                Toast.makeText(view.getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }
}