package com.rotiking.admin.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rotiking.admin.R;
import com.rotiking.admin.adapter.OrderRecyclerAdapter;
import com.rotiking.admin.models.Order;

public class OrdersFragment extends Fragment {
    private RecyclerView ordersRV;
    private LinearLayout noOrdersI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRV = view.findViewById(R.id.order_rv);
        ordersRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ordersRV.setHasFixedSize(true);

        noOrdersI = view.findViewById(R.id.no_orders_i);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = FirebaseFirestore.getInstance().collection("orders").orderBy("time", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>().setQuery(query, Order.class).build();
        OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(options, noOrdersI);
        ordersRV.setAdapter(adapter);
        adapter.startListening();
    }
}