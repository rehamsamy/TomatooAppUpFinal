package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elbndarmarket.R;
import com.elbndarmarket.interfaces.NavRecyclerClickListner;
import com.elbndarmarket.model.NavItemModel;

import java.util.List;

public class NavMenuAdapter extends RecyclerView.Adapter<NavMenuAdapter.ViewHolder> {

    Context mcontext;
    List<NavItemModel> list;
    private NavRecyclerClickListner itemClickListner;

    public NavMenuAdapter(Context mcontext, List<NavItemModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(NavRecyclerClickListner listener) {
        itemClickListner = listener;
    }

    @NonNull
    @Override
    public NavMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.menu_layout_item, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull NavMenuAdapter.ViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(list.get(position).getImage());
        viewHolder.item_title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView item_title;

        public ViewHolder(@NonNull View itemView, final NavRecyclerClickListner navRecyclerClickListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sideMenu_icon_id);
            item_title = itemView.findViewById(R.id.sideMenu_title_txtV_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (navRecyclerClickListner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            navRecyclerClickListner.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
