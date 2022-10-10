package com.rotiking.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.Query;
import com.rotiking.admin.adapter.SelectAgentRecyclerAdapter;
import com.rotiking.admin.common.db.Database;
import com.rotiking.admin.models.Agent;
import com.rotiking.admin.utils.Promise;

import java.util.List;

public class SelectDeliveryAgentActivity extends AppCompatActivity {
    private RecyclerView agentsRV;
    private ImageButton close;
    private LinearLayout noDeliveryAgentI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_delivery_agent);

        close = findViewById(R.id.close);
        noDeliveryAgentI = findViewById(R.id.no_delivery_i);

        agentsRV = findViewById(R.id.delivery_user_RV);
        agentsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        agentsRV.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = Database.getInstance().collection("delivery_user");
        FirestoreRecyclerOptions<Agent> options = new FirestoreRecyclerOptions.Builder<Agent>().setQuery(query, Agent.class).build();
        SelectAgentRecyclerAdapter adapter = new SelectAgentRecyclerAdapter(options, SelectDeliveryAgentActivity.this, getIntent().getStringExtra("ORDER_ID"), noDeliveryAgentI);
        agentsRV.setAdapter(adapter);
        adapter.startListening();

        close.setOnClickListener(view -> finish());
    }
}