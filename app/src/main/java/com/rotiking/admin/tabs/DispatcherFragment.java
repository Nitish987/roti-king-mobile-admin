package com.rotiking.admin.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.rotiking.admin.CreateDeliveryAgentActivity;
import com.rotiking.admin.R;
import com.rotiking.admin.adapter.AgentRecyclerAdapter;
import com.rotiking.admin.common.db.Database;
import com.rotiking.admin.models.Agent;
import com.rotiking.admin.utils.Promise;

import java.util.List;

public class DispatcherFragment extends Fragment {
    private View view;
    private FloatingActionButton createDeliveryAgent;
    private RecyclerView agentsRV;
    private CircularProgressIndicator agentsProgress;
    private LinearLayout noDeliveryAgentI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dispatcher, container, false);

        createDeliveryAgent = view.findViewById(R.id.add_delivery_user_btn);
        agentsProgress = view.findViewById(R.id.delivery_user_progress);
        noDeliveryAgentI = view.findViewById(R.id.no_delivery_i);

        agentsRV = view.findViewById(R.id.delivery_user_RV);
        agentsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        agentsRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createDeliveryAgent.setOnClickListener(v -> {
            Agent agent = new Agent("", "", "", "", "");

            Intent intent = new Intent(view.getContext(), CreateDeliveryAgentActivity.class);
            intent.putExtra("AGENT", agent);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Database.OthersAuth.getDeliveryAgentList(view.getContext(), new Promise<List<Agent>>() {
            @Override
            public void resolving(int progress, String msg) {
                agentsProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void resolved(List<Agent> o) {
                agentsProgress.setVisibility(View.INVISIBLE);
                AgentRecyclerAdapter adapter = new AgentRecyclerAdapter(o, noDeliveryAgentI);
                agentsRV.setAdapter(adapter);
            }

            @Override
            public void reject(String err) {
                Toast.makeText(view.getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });
    }
}