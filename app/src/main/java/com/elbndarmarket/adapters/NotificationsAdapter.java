package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.model.CartProductsModel;
import com.elbndarmarket.model.NotificationTitle;
import com.elbndarmarket.model.NotificationsModel;
import com.google.gson.Gson;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context mcontext;
    private List<NotificationsModel.NotificationData> list;

    public NotificationsAdapter(Context mcntext, List<NotificationsModel.NotificationData> list) {
        this.mcontext = mcntext;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notification_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder viewHolder, int position) {
        NotificationsModel.NotificationData itemData = list.get(position);

        Gson gson = new Gson();
        NotificationTitle notificationTitle = gson.fromJson(itemData.getData(), NotificationTitle.class);
        Log.i("titl: ", itemData.getData());
        Log.i("titl: ", notificationTitle.getTitle());
        viewHolder.orderNum_txtV.setText(notificationTitle.getTitle());
        viewHolder.time_ago_txtV.setText(itemData.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(NotificationsModel.NotificationData item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderNum_txtV, orderProcess_txtV, time_ago_txtV;
        public LinearLayout view_back_ground;
        public ConstraintLayout view_foreground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNum_txtV = itemView.findViewById(R.id.orderNum_txtV_id);
            orderProcess_txtV = itemView.findViewById(R.id.orderProcess_txtV_id);
            time_ago_txtV = itemView.findViewById(R.id.time_ago_txtV);
            view_back_ground = itemView.findViewById(R.id.notification_view_back_ground);
            view_foreground = itemView.findViewById(R.id.notification_forground_layout);
        }
    }
}
