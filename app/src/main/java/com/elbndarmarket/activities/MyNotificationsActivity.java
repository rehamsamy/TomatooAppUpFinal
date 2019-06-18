package com.elbndarmarket.activities;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.CartAdapter;
import com.elbndarmarket.adapters.NotificationsAdapter;
import com.elbndarmarket.interfaces.RecyclerTouchHelperListner;
import com.elbndarmarket.model.CartProductsModel;
import com.elbndarmarket.model.DeleteNotificatiomModel;
import com.elbndarmarket.model.NotificationsModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotificationsActivity extends AppCompatActivity implements RecyclerTouchHelperListner {

    @BindView(R.id.notications_recyclerV_id)
    RecyclerView notifications_recyclerV;
    @BindView(R.id.myNotifications_back_txtV_id)
    ImageView back_txtV;
    @BindView(R.id.no_notification_txtV)
    TextView no_notification_txtV;
    @BindView(R.id.notifications_progress_id)
    ProgressBar progressBar;

    private NotificationsAdapter adapter;
    List<NotificationsModel.NotificationData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        Log.i("id: ", MainActivity.userData.getId() + "");
        Log.i("token: ", MainActivity.userData.getToken());

        getMyNotifications(MainActivity.userData.getId(), MainActivity.userData.getToken());
    }

    private void getMyNotifications(int id, String token) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<NotificationsModel> call = serviceInterface.getUserNotifications(id, token);
        call.enqueue(new Callback<NotificationsModel>() {
            @Override
            public void onResponse(Call<NotificationsModel> call, Response<NotificationsModel> response) {
                NotificationsModel notificationsModel = response.body();
                if (response.body().getStatus()) {
                    list = notificationsModel.getNotfication();
                    if (list.size() > 0) {
                        notifications_recyclerV.setVisibility(View.VISIBLE);
                        no_notification_txtV.setVisibility(View.GONE);
                        buildNoficationRecycler(list);
                    } else {
                        notifications_recyclerV.setVisibility(View.GONE);
                        no_notification_txtV.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NotificationsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildNoficationRecycler(List<NotificationsModel.NotificationData> list) {
        notifications_recyclerV.setLayoutManager(new LinearLayoutManager(this));
        notifications_recyclerV.setHasFixedSize(true);

        // Set Adapter to Recycler
        adapter = new NotificationsAdapter(this, list);
        notifications_recyclerV.setAdapter(adapter);
    }

    @OnClick(R.id.myNotifications_back_txtV_id)
    public void notifications_back() {
        finish();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            // backup of removed item for undo purpose
            final NotificationsModel.NotificationData deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            deleteNotification(MainActivity.userData.getId(), deletedItem.getId(), MainActivity.userData.getToken());

            // showing snack bar with Undo option
            Toast.makeText(this, getString(R.string.cart_item_removed), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteNotification(int id, String notifiable_id, String token) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DeleteNotificatiomModel> call = serviceInterface.deleteNotification(id, notifiable_id, token);
        call.enqueue(new Callback<DeleteNotificatiomModel>() {
            @Override
            public void onResponse(Call<DeleteNotificatiomModel> call, Response<DeleteNotificatiomModel> response) {

                if (response.body().getStatus()) {
                    Toast.makeText(MyNotificationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyNotificationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteNotificatiomModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
