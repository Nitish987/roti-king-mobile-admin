package com.rotiking.admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rotiking.admin.CreateDeliveryAgentActivity;
import com.rotiking.admin.R;
import com.rotiking.admin.models.Agent;

import java.util.List;

public class AgentRecyclerAdapter extends RecyclerView.Adapter<AgentRecyclerAdapter.AgentHolder> {
    private final List<Agent> agents;

    public AgentRecyclerAdapter(List<Agent> agents) {
        this.agents = agents;
    }

    @NonNull
    @Override
    public AgentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AgentHolder holder, int position) {
        Agent agent = agents.get(position);
        holder.setPhoto(agent.getPhoto());
        holder.setName(agent.getName());
        holder.setPhone(agent.getPhone());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CreateDeliveryAgentActivity.class);
            intent.putExtra("AGENT", agent);
            intent.putExtra("NEW", false);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }

    public static class AgentHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name, phone;

        public AgentHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }

        public void setPhoto(String photo) {
            if (!photo.equals("None")) Glide.with(this.photo.getContext()).load(photo).into(this.photo);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setPhone(String phone) {
            String phone_ = "+91" + phone;
            this.phone.setText(phone_);
        }
    }
}
