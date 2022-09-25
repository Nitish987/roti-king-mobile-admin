package com.rotiking.admin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rotiking.admin.R;
import com.rotiking.admin.models.Agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectAgentRecyclerAdapter extends RecyclerView.Adapter<AgentRecyclerAdapter.AgentHolder> {
    private final Activity activity;
    private final List<Agent> agents;
    private final  String orderId;
    private final LinearLayout noDeliveryAgentI;

    public SelectAgentRecyclerAdapter(Activity activity, List<Agent> agents, String orderId, LinearLayout noDeliveryAgentI) {
        this.activity = activity;
        this.agents = agents;
        this.orderId = orderId;
        this.noDeliveryAgentI = noDeliveryAgentI;
    }

    @NonNull
    @Override
    public AgentRecyclerAdapter.AgentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentRecyclerAdapter.AgentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AgentRecyclerAdapter.AgentHolder holder, int position) {
        noDeliveryAgentI.setVisibility(View.INVISIBLE);

        Agent agent = agents.get(position);
        holder.setPhoto(agent.getPhoto());
        holder.setName(agent.getName());
        holder.setPhone(agent.getPhone());

        holder.itemView.setOnClickListener(view -> {
            Map<String, Object> map = new HashMap<>();
            map.put("agentName", agent.getName());
            map.put("agentPhone", "+91" + agent.getPhone());
            map.put("agentUid", agent.getUid());
            FirebaseFirestore.getInstance().collection("orders").document(orderId).update(map).addOnSuccessListener(unused -> activity.finish()).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Unable to allot delivery agent.", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }
}
