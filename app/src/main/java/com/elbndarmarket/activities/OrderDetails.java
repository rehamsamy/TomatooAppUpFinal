package com.elbndarmarket.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.DetailsListAdapter;
import com.elbndarmarket.model.OrderDetailsModel;
import com.elbndarmarket.model.UserOrderModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity {

    @BindView(R.id.orderName_txtV)
    TextView orderName_toolbar_txtV;
    @BindView(R.id.order_details_icon_image_id)
    ImageView state_imageV;
    @BindView(R.id.current_order_state_txtV)
    TextView orderState_txtV;
    @BindView(R.id.order_number_txtV_id)
    TextView orderNumber_txtV;
    @BindView(R.id.order_date_txtV_id)
    TextView orderDate_txtV;
    @BindView(R.id.deliveryAddress_val_txtV_id)
    TextView deliveryAddress_txtV;
    @BindView(R.id.orderDetails_payment_val_txtV_id)
    TextView paymentMethod_txtV;
    @BindView(R.id.order_desc_listV_id)
    ListView order_desc_listV;
    @BindView(R.id.estimatedTime_val_txtV_id)
    TextView estimate_time_txtV;
    @BindView(R.id.details_price_val_txtV)
    TextView price_txtV;
    @BindView(R.id.details_deliveryFee_val_txtV)
    TextView deliveryFee_txtV;
    @BindView(R.id.total_price_val_txtV)
    TextView total_price_txtV;
    @BindView(R.id.order_details_layout_id)
    ConstraintLayout order_details_layout;


    private UserOrderModel.OrderData orderData;
    private DetailsListAdapter adapter;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        // Set Data to Views
        if (getIntent().hasExtra("order_data")) {
            orderData = getIntent().getExtras().getParcelable("order_data");
            Log.i("list:: ", orderData.getOrder_Status());
            Log.i("ord_id:: ", orderData.getOrder_id() + "");
            Log.i("token:: ", MainActivity.userData.getToken());
            Log.i("use_id:: ", MainActivity.userData.getId() + "");
            getOrderDetails(orderData.getOrder_id());
        }
    }

    private void getOrderDetails(final int order_id) {
        dialog = new SpotsDialog(OrderDetails.this, getString(R.string.loading), R.style.Custom);
        dialog.show();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<OrderDetailsModel> call = serviceInterface.getOrderDetails(MainActivity.userData.getId(), MainActivity.userData.getToken(), order_id);
        call.enqueue(new Callback<OrderDetailsModel>() {
            @Override
            public void onResponse(Call<OrderDetailsModel> call, Response<OrderDetailsModel> response) {
                order_details_layout.setVisibility(View.VISIBLE);
                OrderDetailsModel detailsModel = response.body();
                if (detailsModel.getStatus()) {
                    OrderDetailsModel.Information order_info = detailsModel.getOrder().getInformation();
                    List<OrderDetailsModel.Products> productsList = detailsModel.getOrder().getProducts();
                    orderName_toolbar_txtV.setText(getString(R.string.order_Num) + "# " + String.valueOf(order_info.getOrder_id()));
                    if (order_info.getOrder_Status().equals("processing")) {
                        state_imageV.setImageResource(R.drawable.process_icon_details);
                    } else if (order_info.getOrder_Status().equals("waiting")) {
                        state_imageV.setImageResource(R.drawable.waiting_icon_details);
                    } else if (order_info.getOrder_Status().equals("completed")) {
                        state_imageV.setImageResource(R.drawable.completed_icon_details);
                    }
                    orderState_txtV.setText(order_info.getOrder_Status());
                    orderNumber_txtV.setText(getString(R.string.order_Num) + "# " + String.valueOf(order_info.getOrder_id()));
                    orderDate_txtV.setText(String.valueOf(order_info.getOrder_updated_at()));
                    paymentMethod_txtV.setText(order_info.getPayMethod());

                    price_txtV.setText(String.valueOf(order_info.getTotalCost()));
                    deliveryFee_txtV.setText(String.valueOf(order_info.getTotalShippingCost() - order_info.getTotalCost()));
                    total_price_txtV.setText(String.valueOf(order_info.getTotalShippingCost()));

                    estimate_time_txtV.setText(order_info.getOrder_updated_at());
                    adapter = new DetailsListAdapter(OrderDetails.this, productsList);
                    order_desc_listV.setAdapter(adapter);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<OrderDetailsModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.orderDetails_back_txtV_id)
    public void go_back() {
        finish();
    }
}
