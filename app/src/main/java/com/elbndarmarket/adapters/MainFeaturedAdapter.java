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
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.interfaces.RecyclerOnItemClickListner;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.utils.PreferencesHelper;
import com.elbndarmarket.utils.Urls;

import java.util.List;

public class MainFeaturedAdapter extends RecyclerView.Adapter<MainFeaturedAdapter.ViewHolder> {

    Context mcontext;
    List<FeaturedProductsModel.ProductData> list;
    private RecyclerOnItemClickListner itemClickListner;
    private final String TAG = this.getClass().getSimpleName();

    public MainFeaturedAdapter(Context mcontext, List<FeaturedProductsModel.ProductData> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerOnItemClickListner listener) {
        itemClickListner = listener;
    }

    @NonNull
    @Override
    public MainFeaturedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.main_featured_row_item, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFeaturedAdapter.ViewHolder viewHolder, int position) {
        FeaturedProductsModel.ProductData productsModel = list.get(position);
        if (PreferencesHelper.getSomeStringValue(mcontext).equals("ar")) {
            viewHolder.item_title.setText(productsModel.getProduct_name_ar());
            viewHolder.item_price.setText(productsModel.getPrice());
            viewHolder.item_price.append("$");
        } else {
            viewHolder.item_title.setText(productsModel.getProduct_name_en());
            viewHolder.item_price.setText(productsModel.getPrice());
            viewHolder.item_price.append("$");
        }
        // Load Product Image
        Glide.with(mcontext)
                .load(Urls.BASE_URL + list.get(position).getPhoto())
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

        if (productsModel.getWishlists() == 1)
            viewHolder.wishlist_image.setImageResource(R.drawable.wishlist_select);
        else
            viewHolder.wishlist_image.setImageResource(R.drawable.wishlist_not_select);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cart_image, wishlist_image, item_image;
        TextView item_title, item_price;

        public ViewHolder(@NonNull View itemView, final RecyclerOnItemClickListner listner) {
            super(itemView);
            cart_image = itemView.findViewById(R.id.main_cart_img_id);
            wishlist_image = itemView.findViewById(R.id.main_wishlist_img_id);
            item_image = itemView.findViewById(R.id.main_item_imageV_id);
            item_title = itemView.findViewById(R.id.main_item_title_txtV_id);
            item_price = itemView.findViewById(R.id.main_item_price_txtV_id);

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

            cart_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnCartClick(position, cart_image);
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
