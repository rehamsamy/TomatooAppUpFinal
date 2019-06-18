package com.elbndarmarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.elbndarmarket.R;
import com.elbndarmarket.activities.CategoryDetails;
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.model.Category;
import com.elbndarmarket.utils.PreferencesHelper;

import java.util.List;

public class MainCategoriesAdapter extends RecyclerView.Adapter<MainCategoriesAdapter.ViewHolder> {

    private Context mcontext;
    private List<Category> list;

    public MainCategoriesAdapter(Context mcontext, List<Category> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public MainCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.categories_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainCategoriesAdapter.ViewHolder holder, int position) {
        if (PreferencesHelper.getSomeStringValue(mcontext).equals("ar")) {
            holder.titl_txtV.setText(list.get(position).getCategory_name_ar());
        } else {
            holder.titl_txtV.setText(list.get(position).getCategory_name_en());
        }

        /**
         * Using Glide to handle image loading.
         * Learn more about Glide here:
         *
         */
        Glide.with(mcontext)
                .load(list.get(position).getCategoryImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        holder.category_progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        holder.category_progress.setVisibility(View.GONE);
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
                .into(holder.imageV);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titl_txtV;
        private ImageView imageV;
        private ProgressBar category_progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titl_txtV = itemView.findViewById(R.id.caterfory_item_name_txtV_id);
            imageV = itemView.findViewById(R.id.category_item_imageV_id);
            category_progress = itemView.findViewById(R.id.category_progress);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, CategoryDetails.class);
                    Log.i("cat_id: ", list.get(getAdapterPosition()).getCategory_id() + "");
                    Log.i("cat_name: ", list.get(getAdapterPosition()).getCategory_name_ar() + "");
                    intent.putExtra("category_id", list.get(getAdapterPosition()).getCategory_id());
                    if (PreferencesHelper.getSomeStringValue(mcontext).equals("ar"))
                        intent.putExtra("cat_name", list.get(getAdapterPosition()).getCategory_name_ar());
                    else
                        intent.putExtra("cat_name", list.get(getAdapterPosition()).getCategory_name_en());
                    mcontext.startActivity(intent);
                }
            });
        }
    }
}
