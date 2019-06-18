package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.model.CartProductsModel;
import com.elbndarmarket.model.CheckOutListModel;

import java.util.List;

public class CheckOutRecyclerAdapter extends RecyclerView.Adapter<CheckOutRecyclerAdapter.ViewHolder> {

    private Context mcontext;
    private List<CartProductsModel.Product> list;

    public CheckOutRecyclerAdapter(Context mcontext, List<CartProductsModel.Product> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public CheckOutRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.checkout_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutRecyclerAdapter.ViewHolder viewHolder, int position) {
        CartProductsModel.Product itemData = list.get(position);
        viewHolder.item_title_txtV.setText(itemData.getProduct_name_en());
        viewHolder.item_price_txtV.setText(String.valueOf(itemData.getPrice() * itemData.getQuentity()));
        viewHolder.item_price_txtV.append("$");
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_title_txtV;
        TextView item_price_txtV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title_txtV = itemView.findViewById(R.id.check_order_item_title_txtV_id);
            item_price_txtV = itemView.findViewById(R.id.check_order_item_price_txtV_id);
        }
    }
}
