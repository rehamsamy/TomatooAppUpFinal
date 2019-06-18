package com.elbndarmarket.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.activities.OrderDetails;
import com.elbndarmarket.adapters.MyOrdersAdapter;
import com.elbndarmarket.adapters.OrderProcessAdapter;
import com.elbndarmarket.interfaces.CategoryItemClickListner;
import com.elbndarmarket.model.UserOrderModel;

import java.util.List;


public class OrderProcessFragment extends Fragment {

    List<UserOrderModel.OrderData> process_list;
    RecyclerView process_recyclerV;
    OrderProcessAdapter ordersAdapter;
    ProgressBar progressBar;
    TextView no_data_txtV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            process_list = getArguments().getParcelableArrayList("process_list");
            Log.i("process: ", process_list + "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_process, container, false);
        process_recyclerV = view.findViewById(R.id.process_recyclerV_id);
        progressBar = view.findViewById(R.id.process_prgressBar_id);
        no_data_txtV = view.findViewById(R.id.process_no_available_data_txtV_id);

        buildProcessRecyclerV(process_list);
        return view;
    }

    private void buildProcessRecyclerV(final List<UserOrderModel.OrderData> list) {
        process_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        process_recyclerV.setHasFixedSize(true);
        if (process_list.size() > 0) {
            process_recyclerV.setVisibility(View.VISIBLE);
            no_data_txtV.setVisibility(View.GONE);
            ordersAdapter = new OrderProcessAdapter(getActivity(), list);
            process_recyclerV.setAdapter(ordersAdapter);

            ordersAdapter.setOnItemClickListener(new CategoryItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    // Go to details with Data
                    Intent intent = new Intent(getActivity(), OrderDetails.class);
                    intent.putExtra("order_data", list.get(position));
                    getActivity().startActivity(intent);
                }
            });

        } else {
            process_recyclerV.setVisibility(View.GONE);
            no_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
