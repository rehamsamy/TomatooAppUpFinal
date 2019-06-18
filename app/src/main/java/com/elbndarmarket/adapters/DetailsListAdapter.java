package com.elbndarmarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.model.OrderDetailsModel;
import com.elbndarmarket.model.OrderListModel;
import com.elbndarmarket.utils.PreferencesHelper;

import java.util.List;

public class DetailsListAdapter extends BaseAdapter {

    private Context mcontext;
    private List<OrderDetailsModel.Products> list;

    public DetailsListAdapter(Context mcontext, List<OrderDetailsModel.Products> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.checkout_row_item, parent, false);
        TextView name_txtV = view.findViewById(R.id.check_order_item_title_txtV_id);
        TextView price_txtV = view.findViewById(R.id.check_order_item_price_txtV_id);

        if (PreferencesHelper.getSomeStringValue(mcontext).equals("ar"))
            name_txtV.setText(list.get(position).getProduct_name_ar());
        else
            name_txtV.setText(list.get(position).getProduct_name_en());
        price_txtV.setText(String.valueOf(list.get(position).getPrice()));
        return view;
    }
}
