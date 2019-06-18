package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.elbndarmarket.R;
import com.elbndarmarket.interfaces.CategoryItemClickListner;
import com.elbndarmarket.model.Category;

import java.util.List;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder> {

    private Context mcontext;
    private List<Category> list;
    private CategoryItemClickListner itemClickListner;

    public AllCategoriesAdapter(Context mcontext, List<Category> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(CategoryItemClickListner listener) {
        itemClickListner = listener;
    }

    @NonNull
    @Override
    public AllCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.all_categories_row_item, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllCategoriesAdapter.ViewHolder viewHolder, int position) {
        Category category = list.get(position);
        viewHolder.item_txtV.setText(list.get(position).getCategory_name_ar());
        Glide.with(mcontext)
                .load(list.get(position).getCategoryImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView item_txtV;

        public ViewHolder(@NonNull View itemView, final CategoryItemClickListner listner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categoryItem_imageV_id);
            item_txtV = itemView.findViewById(R.id.categoryItem_name_txtV_id);

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
