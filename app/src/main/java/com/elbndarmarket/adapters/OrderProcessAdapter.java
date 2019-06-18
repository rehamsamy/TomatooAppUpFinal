package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.interfaces.CategoryItemClickListner;
import com.elbndarmarket.model.UserOrderModel;

import java.util.List;

public class OrderProcessAdapter extends RecyclerView.Adapter<OrderProcessAdapter.ViewHolder> {

    private Context mcontext;
    private List<UserOrderModel.OrderData> list;
    private CategoryItemClickListner itemClickListner;

    public OrderProcessAdapter(Context mcontext, List<UserOrderModel.OrderData> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(CategoryItemClickListner listener) {
        itemClickListner = listener;
    }


    @NonNull
    @Override
    public OrderProcessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.order_process_row, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProcessAdapter.ViewHolder viewHolder, int position) {
        viewHolder.orderNum_txtV.setText(mcontext.getString(R.string.order_Num) + "# " + String.valueOf(list.get(position).getOrder_id()));
        viewHolder.orderState_txtV.setText(list.get(position).getOrder_Status());
        viewHolder.order_time_txtV.setText(list.get(position).getOrder_updated_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderNum_txtV, orderState_txtV, order_time_txtV;

        public ViewHolder(@NonNull View itemView, final CategoryItemClickListner listner) {
            super(itemView);
            orderNum_txtV = itemView.findViewById(R.id.process_orderNum_txtV_id);
            orderState_txtV = itemView.findViewById(R.id.orderProcess_txtV_id);
            order_time_txtV = itemView.findViewById(R.id.time_ago_txtV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
