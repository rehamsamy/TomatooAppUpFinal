package com.elbndarmarket.activities;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.OrdersPagerAdapter;
import com.elbndarmarket.model.UserOrderModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrders extends AppCompatActivity {

    @BindView(R.id.myOrders_back_txtV_id)
    ImageView bcak_txtV;
    @BindView(R.id.orders_tabLayout_id)
    TabLayout tabLayout;
    TabItem process_tab;
    TabItem waiting_tab;
    TabItem completed_tab;
    @BindView(R.id.viewPager_id)
    ViewPager orders_ViewPager;
    @BindView(R.id.orders_layout_id)
    LinearLayout orders_layout;
    @BindView(R.id.no_available_orders_txtV)
    TextView no_orders_txtV;

    OrdersPagerAdapter pagerAdapter;
    private String TAG = this.getClass().getSimpleName();
    private List<UserOrderModel.OrderData> processing_list;
    private List<UserOrderModel.OrderData> waiting_list;
    private List<UserOrderModel.OrderData> completed_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        process_tab = findViewById(R.id.process_tab_id);
        waiting_tab = findViewById(R.id.waiting_tab_id);
        completed_tab = findViewById(R.id.completed_tab_id);

        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UserOrderModel> call = serviceInterface.getUserOrders(MainActivity.userData.getId(), MainActivity.userData.getToken());
        call.enqueue(new Callback<UserOrderModel>() {
            @Override
            public void onResponse(Call<UserOrderModel> call, Response<UserOrderModel> response) {
                processing_list = new ArrayList<>();
                waiting_list = new ArrayList<>();
                completed_list = new ArrayList<>();
                UserOrderModel orderModel = response.body();
                if (orderModel.getStatus()) {
                    orders_layout.setVisibility(View.VISIBLE);
                    no_orders_txtV.setVisibility(View.GONE);
                    List<UserOrderModel.OrderData> orderDataList = orderModel.getOrder();
                    Log.i(TAG, orderDataList.size() + "");
                    for (int i = 0; i < orderDataList.size(); i++) {
                        if (orderDataList.get(i).getOrder_Status().equals("Processing")) {
                            processing_list.add(orderDataList.get(i));
                        } else if (orderDataList.get(i).getOrder_Status().equals("Waiting")) {
                            waiting_list.add(orderDataList.get(i));
                        } else if (orderDataList.get(i).getOrder_Status().equals("Completed")) {
                            completed_list.add(orderDataList.get(i));
                        }
                    }

                    pagerAdapter = new OrdersPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), processing_list, waiting_list, completed_list);
                    orders_ViewPager.setAdapter(pagerAdapter);
                    orders_ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            orders_ViewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });

                } else {
                    orders_layout.setVisibility(View.GONE);
                    no_orders_txtV.setVisibility(View.VISIBLE);
//                    Toast.makeText(MyOrders.this, orderModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserOrderModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.myOrders_back_txtV_id)
    public void go_back() {
        finish();
    }
}
