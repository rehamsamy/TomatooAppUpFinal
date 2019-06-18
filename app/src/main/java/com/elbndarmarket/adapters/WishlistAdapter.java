package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.interfaces.RecyclerOnItemClickListner;
import com.elbndarmarket.model.WishListModel;
import com.elbndarmarket.utils.PreferencesHelper;
import com.elbndarmarket.utils.Urls;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    Context mcontext;
    ArrayList<WishListModel.WishlistItem> list;
    private RecyclerOnItemClickListner itemClickListner;

    public WishlistAdapter(Context mcontext, ArrayList<WishListModel.WishlistItem> list) {
        this.mcontext = mcontext;
        this.list = list;
    }


    public void setOnItemClickListener(RecyclerOnItemClickListner listener) {
        itemClickListner = listener;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.wishlist_item_row, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder viewHolder, int position) {

        WishListModel.WishlistItem wishListModel = list.get(position);
        if (PreferencesHelper.getSomeStringValue(mcontext).equals("ar")) {
            Log.i("lang: ", PreferencesHelper.getSomeStringValue(mcontext));
            viewHolder.item_name.setText(wishListModel.getProduct_name_ar());
            viewHolder.item_price.setText(wishListModel.getPrice() + "");
            viewHolder.item_price.append(" $");
        } else {
            viewHolder.item_name.setText(wishListModel.getProduct_name_en());
            viewHolder.item_price.setText(wishListModel.getPrice() + "");
            viewHolder.item_price.append(" $");
        }
        Glide.with(mcontext)
                .load(Urls.BASE_URL + wishListModel.getPhoto())
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
                .into(viewHolder.item_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, item_price;
        ImageView item_image;
        ImageView add_cart;
        ImageView wishlist_image;

        public ViewHolder(@NonNull View itemView, final RecyclerOnItemClickListner listner) {
            super(itemView);
            item_name = itemView.findViewById(R.id.wishlist_item_name_txtV_id);
            item_price = itemView.findViewById(R.id.wishlist_item_price_txtV_id);
            item_image = itemView.findViewById(R.id.item_imageV_id);
            add_cart = itemView.findViewById(R.id.wishlist_cart_img_id);
            wishlist_image = itemView.findViewById(R.id.wishlist_selected_img_id);

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
            add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnCartClick(position, add_cart);
                        }
                    }
                }
            });
            wishlist_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnWishListClick(position, wishlist_image);
                        }
                    }
                }
            });

        }
    }
}
