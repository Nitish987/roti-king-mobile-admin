package com.rotiking.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.rotiking.admin.adapter.SelectAgentRecyclerAdapter;
import com.rotiking.admin.common.db.Database;
import com.rotiking.admin.models.Agent;
import com.rotiking.admin.utils.Promise;

import java.util.List;

public class SelectDeliveryAgentActivity extends AppCompatActivity {
    private RecyclerView agentsRV;
    private CircularProgressIndicator agentsProgress;
    private ImageButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_delivery_agent);

        agentsProgress = findViewById(R.id.delivery_user_progress);
        close = findViewById(R.id.close);

        agentsRV = findViewById(R.id.delivery_user_RV);
        agentsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        agentsRV.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Database.OthersAuth.getDeliveryAgentList(this, new Promise<List<Agent>>() {
            @Override
            public void resolving(int progress, String msg) {
                agentsProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void resolved(List<Agent> o) {
                agentsProgress.setVisibility(View.INVISIBLE);
                SelectAgentRecyclerAdapter adapter = new SelectAgentRecyclerAdapter(SelectDeliveryAgentActivity.this, o, getIntent().getStringExtra("ORDER_ID"));
                agentsRV.setAdapter(adapter);
            }

            @Override
            public void reject(String err) {
                Toast.makeText(SelectDeliveryAgentActivity.this, err, Toast.LENGTH_SHORT).show();
            }
        });

        close.setOnClickListener(view -> finish());
    }
}