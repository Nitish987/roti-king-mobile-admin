package com.rotiking.admin.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rotiking.admin.FoodActivity;
import com.rotiking.admin.R;
import com.rotiking.admin.models.Food;

public class FoodsFragment extends Fragment {
    private View view;
    private FloatingActionButton addFoodBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foods, container, false);

        addFoodBtn = view.findViewById(R.id.add_food_btn);

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
}