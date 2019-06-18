package com.elbndarmarket.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.activities.OrderDetails;
import com.elbndarmarket.adapters.CompleteOrderAdapter;
import com.elbndarmarket.adapters.MyOrdersAdapter;
import com.elbndarmarket.interfaces.CategoryItemClickListner;
import com.elbndarmarket.model.UserOrderModel;

import java.util.List;

public class OrderCompletedFragment extends Fragment {

    List<UserOrderModel.OrderData> complete_list;
    RecyclerView complete_recyclerV;
    CompleteOrderAdapter completeOrderAdapter;
    ProgressBar progressBar;
    TextView no_available_data_txtV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            complete_list = getArguments().getParcelableArrayList("complete_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_completed, container, false);
        complete_recyclerV = view.findViewById(R.id.completed_recyclerV_id);
        progressBar = view.findViewById(R.id.completed_prgressBar_id);
        no_available_data_txtV = view.findViewById(R.id.completed_no_available_data_txtV_id);

        buildCompleteRecyclerV(complete_list);
        return view;
    }

    private void buildCompleteRecyclerV(final List<UserOrderModel.OrderData> complete_list) {
        complete_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        complete_recyclerV.setHasFixedSize(true);

        if (complete_list.size() > 0) {
            complete_recyclerV.setVisibility(View.VISIBLE);
            no_available_data_txtV.setVisibility(View.GONE);

            completeOrderAdapter = new CompleteOrderAdapter(getActivity(), complete_list);
            complete_recyclerV.setAdapter(completeOrderAdapter);

            completeOrderAdapter.setOnItemClickListener(new CategoryItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    // Go to details with Data
                    Intent intent = new Intent(getActivity(), OrderDetails.class);
                    intent.putExtra("order_data", complete_list.get(position));
                    getActivity().startActivity(intent);
                }
            });
        } else {
            complete_recyclerV.setVisibility(View.GONE);
            no_available_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
