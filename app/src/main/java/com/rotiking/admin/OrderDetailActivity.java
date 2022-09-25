package com.rotiking.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rotiking.admin.adapter.CheckoutCartItemRecyclerAdapter;
import com.rotiking.admin.models.CartItem;
import com.rotiking.admin.models.CheckoutCartItem;
import com.rotiking.admin.models.Order;
import com.rotiking.admin.models.Topping;
import com.rotiking.admin.utils.DateParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {
    private RecyclerView orderItemRV;
    private ImageButton closeBtn;
    private TextView itemsTxt, orderNumberTxt, totalCartPriceTxt, deliveryCartPriceTxt, totalPayableTxt, nameTxt, phoneTxt, addressTxt, agentNameTxt, agentPhoneTxt, timeTxt, paymentMethodTxt;
    private AppCompatButton cancelOrderBtn, acceptOrderBtn;
    private LinearProgressIndicator orderStateIndicator;
    private TextView orderedStateTxt, orderedState, cookingState, dispatchedState, onWayState, deliveredState;
    private LinearLayout deliveryAgentDesk;

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = getIntent().getStringExtra("ORDER");

        closeBtn = findViewById(R.id.close);
        itemsTxt = findViewById(R.id.items);
        orderNumberTxt = findViewById(R.id.order_number);
        totalCartPriceTxt = findViewById(R.id.total_price);
        deliveryCartPriceTxt = findViewById(R.id.delivery_price);
        totalPayableTxt = findViewById(R.id.payable_price);
        cancelOrderBtn = findViewById(R.id.cancel);
        nameTxt = findViewById(R.id.name);
        phoneTxt = findViewById(R.id.phone);
        addressTxt = findViewById(R.id.address);
        agentNameTxt = findViewById(R.id.agent_name);
        agentPhoneTxt = findViewById(R.id.agent_phone);
        deliveryAgentDesk = findViewById(R.id.delivery_agent_desk);
        timeTxt = findViewById(R.id.time);
        paymentMethodTxt = findViewById(R.id.payment_method);
        orderStateIndicator = findViewById(R.id.order_state_indicator);
        orderedStateTxt = findViewById(R.id.order_state_text);
        orderedState = findViewById(R.id.ordered_state);
        cookingState = findViewById(R.id.cooking_state);
        dispatchedState = findViewById(R.id.dispatched_state);
        onWayState = findViewById(R.id.on_way_state);
        deliveredState = findViewById(R.id.delivered_state);
        acceptOrderBtn = findViewById(R.id.accept);

        orderItemRV = findViewById(R.id.ordered_item_rv);
        orderItemRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        orderItemRV.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("orders").document(orderId).addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                Order order = value.toObject(Order.class);

                assert order != null;
                CheckoutCartItemRecyclerAdapter adapter = new CheckoutCartItemRecyclerAdapter(createOrderItemList(order.getItems()));
                orderItemRV.setAdapter(adapter);

                String tcp = "\u20B9 " + order.getTotalPrice();
                totalCartPriceTxt.setText(tcp);

                String dp_ = "\u20B9 " + order.getDeliveryPrice();
                deliveryCartPriceTxt.setText(dp_);

                String tp_ = "\u20B9 " + order.getPayablePrice();
                totalPayableTxt.setText(tp_);

                String c_ = order.getItems().size() + " Food Items";
                itemsTxt.setText(c_);

                String o_ = "#~order~" + order.getOrderNumber();
                orderNumberTxt.setText(o_);

                String t_ = DateParser.parse(new Date(order.getTime()));
                timeTxt.setText(t_);

                paymentMethodTxt.setText(order.getPaymentMethod());
                nameTxt.setText(order.getName());
                addressTxt.setText(order.getAddress());
                phoneTxt.setText(order.getPhone());
                orderedStateTxt.setText(getOrderStateTxt(order.getOrderState()));

                if (order.getAgentName() != null && order.getAgentPhone() != null) {
                    agentNameTxt.setText(order.getAgentName());
                    agentPhoneTxt.setText(order.getAgentPhone());
                }

                switch (order.getOrderState()) {
                    case 0:
                        orderStateIndicator.setProgressCompat(0, true);
                        orderedState.getBackground().setTint(getColor(R.color.red));
                        break;
                    case 1:
                        orderStateIndicator.setProgressCompat(25, true);
                        orderedState.getBackground().setTint(getColor(R.color.red));
                        cookingState.getBackground().setTint(getColor(R.color.red));

                        acceptOrderBtn.setVisibility(View.GONE);
                        acceptOrderBtn.setEnabled(false);
                        break;
                    case 2:
                        orderStateIndicator.setProgressCompat(50, true);
                        orderedState.getBackground().setTint(getColor(R.color.red));
                        cookingState.getBackground().setTint(getColor(R.color.red));
                        dispatchedState.getBackground().setTint(getColor(R.color.red));

                        acceptOrderBtn.setVisibility(View.GONE);
                        acceptOrderBtn.setEnabled(false);
                        break;
                    case 3:
                        orderStateIndicator.setProgressCompat(75, true);
                        orderedState.getBackground().setTint(getColor(R.color.red));
                        cookingState.getBackground().setTint(getColor(R.color.red));
                        dispatchedState.getBackground().setTint(getColor(R.color.red));
                        onWayState.getBackground().setTint(getColor(R.color.red));

                        deliveryAgentDesk.setEnabled(false);

                        acceptOrderBtn.setVisibility(View.GONE);
                        acceptOrderBtn.setEnabled(false);

                        cancelOrderBtn.setVisibility(View.GONE);
                        cancelOrderBtn.setEnabled(false);
                        break;
                    case 4:
                        orderStateIndicator.setProgressCompat(100, true);
                        orderedState.getBackground().setTint(getColor(R.color.red));
                        cookingState.getBackground().setTint(getColor(R.color.red));
                        dispatchedState.getBackground().setTint(getColor(R.color.red));
                        onWayState.getBackground().setTint(getColor(R.color.red));
                        deliveredState.getBackground().setTint(getColor(R.color.red));

                        if (order.getSecureNumber() != null) {
                            deliveredState.getBackground().setTint(getColor(R.color.light_red));

                            String notDelivered = "Not Delivered Yet";
                            orderedStateTxt.setText(notDelivered);
                        }

                        acceptOrderBtn.setVisibility(View.GONE);
                        acceptOrderBtn.setEnabled(false);

                        cancelOrderBtn.setVisibility(View.GONE);
                        cancelOrderBtn.setEnabled(false);
                        break;
                }

                if (!order.isOrderSuccess()) {
                    orderStateIndicator.setProgressCompat(0, true);
                    acceptOrderBtn.setVisibility(View.GONE);
                    acceptOrderBtn.setEnabled(false);

                    String cancelMsg = "Order was Canceled.";
                    cancelOrderBtn.setText(cancelMsg);
                    cancelOrderBtn.setEnabled(false);
                    orderedStateTxt.setText(cancelMsg);

                    deliveryAgentDesk.setVisibility(View.GONE);
                }
            }
        });

        deliveryAgentDesk.setOnClickListener(view -> {
            Intent intent = new Intent(this, SelectDeliveryAgentActivity.class);
            intent.putExtra("ORDER_ID", orderId);
            startActivity(intent);
        });

        acceptOrderBtn.setOnClickListener(view -> {
            if (agentNameTxt.getText().toString().equals(getString(R.string.no_name)) || agentPhoneTxt.getText().toString().equals(getString(R.string.no_phone))) {
                Toast.makeText(this, "No delivery agent selected.", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("orderState", 1);
                FirebaseFirestore.getInstance().collection("orders").document(orderId).update(map).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Order Accepted.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to accept order.", Toast.LENGTH_SHORT).show();
                });
            }
        });

        cancelOrderBtn.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Order Cancel");
            alert.setMessage("Are you sure, you want to cancel this order.");
            alert.setCancelable(true);
            alert.setPositiveButton("Yes", (dialogInterface, i) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("orderSuccess", false);
                FirebaseFirestore.getInstance().collection("orders").document(orderId).update(map)
                        .addOnSuccessListener(unused -> Toast.makeText(this, "Order was canceled.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Unable to cancel Order.", Toast.LENGTH_SHORT).show());
            });
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });

        closeBtn.setOnClickListener(view -> finish());
    }

    private List<CheckoutCartItem> createOrderItemList(List<CartItem> items) {
        List<CheckoutCartItem> checkoutCartItems = new ArrayList<>();
        for (CartItem cartItem : items) {
            StringBuilder orderName = new StringBuilder();
            orderName.append(cartItem.getFood_data().getName()).append("(").append(cartItem.getQuantity()).append(")");
            if (!cartItem.getTopping_ids().equals("None")) {
                orderName.append(" + Toppings(");
                for (Topping topping : cartItem.getToppings()) {
                    orderName.append(topping.getName()).append(", ");
                }
                orderName.replace(orderName.length() - 2, orderName.length(), ")");
            }
            CheckoutCartItem item = new CheckoutCartItem(orderName.toString(), cartItem.getTotal_price());
            checkoutCartItems.add(item);
        }
        return checkoutCartItems;
    }

    private String getOrderStateTxt(int state) {
        switch (state) {
            case 0:
                return "Ordered...";
            case 1:
                return "Cooking...";
            case 2:
                return "Dispatched...";
            case 3:
                return "On way...";
            case 4:
                return "Delivered...";
            default:
                return "";
        }
    }
}