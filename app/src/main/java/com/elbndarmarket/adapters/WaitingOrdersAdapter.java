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

public class WaitingOrdersAdapter extends RecyclerView.Adapter<WaitingOrdersAdapter.ViewHolder> {

    private Context mcontext;
    private List<UserOrderModel.OrderData> list;
    private CategoryItemClickListner itemClickListner;

    public WaitingOrdersAdapter(Context mcontext, List list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(CategoryItemClickListner listener) {
        itemClickListner = listener;
    }

    @NonNull
    @Override
    public WaitingOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.waiting_order_row_item, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingOrdersAdapter.ViewHolder viewHolder, int position) {
        viewHolder.orderNum_txtV.setText(mcontext.getString(R.string.order_Num) + "# " + list.get(position).getOrder_id());
        viewHolder.orderProcess_txtV.setText(list.get(position).getOrder_Status());
        viewHolder.time_ago_txtV.setText(list.get(position).getOrder_updated_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderNum_txtV, orderProcess_txtV, time_ago_txtV;

        public ViewHolder(@NonNull View itemView, final CategoryItemClickListner listner) {
            super(itemView);
            orderNum_txtV = itemView.findViewById(R.id.waiting_orderNum_txtV_id);
            orderProcess_txtV = itemView.findViewById(R.id.waiting_orderProcess_txtV_id);
            time_ago_txtV = itemView.findViewById(R.id.waiting_time_ago_txtV);

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
