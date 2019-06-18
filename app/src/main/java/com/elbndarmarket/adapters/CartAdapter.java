package com.elbndarmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.elbndarmarket.R;
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.activities.ShoppingCart;
import com.elbndarmarket.interfaces.CartItemClickListner;
import com.elbndarmarket.model.CartProductsModel;
import com.elbndarmarket.utils.PreferencesHelper;
import com.elbndarmarket.utils.Urls;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    public static List<CartProductsModel.Product> list;
    private CartItemClickListner cartItemClickListner;

    public CartAdapter(Context context, List<CartProductsModel.Product> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnCartItemClickListener(CartItemClickListner listener) {
        cartItemClickListner = listener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_row_item, viewGroup, false);
        return new ViewHolder(view, cartItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder viewHolder, int position) {
        CartProductsModel.Product itemModel = list.get(position);
        if (PreferencesHelper.getSomeStringValue(context).equals("ar")) {
            viewHolder.category.setText(itemModel.getCategory_name_ar());
            viewHolder.name.setText(itemModel.getProduct_name_ar());
            viewHolder.price.setText(itemModel.getPrice() + "");
            viewHolder.price.append(" $");
            viewHolder.count_txtV.setText(String.valueOf(itemModel.getQuentity()));
        } else {
            viewHolder.category.setText(itemModel.getCategory_name_en());
            viewHolder.name.setText(itemModel.getProduct_name_en());
            viewHolder.price.setText(itemModel.getPrice() + "");
            viewHolder.price.append(" $");
            viewHolder.count_txtV.setText(String.valueOf(itemModel.getQuentity()));
        }
        Glide.with(context)
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
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(CartProductsModel.Product item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image;
        TextView category, name, price, count_txtV;
        ImageView plus_img, minus_img;
        public LinearLayout view_back_ground;
        public ConstraintLayout view_foreground;

        public ViewHolder(@NonNull View itemView, final CartItemClickListner listner) {
            super(itemView);
            item_image = itemView.findViewById(R.id.cart_item_img_id);
            category = itemView.findViewById(R.id.cart_item_categpry_txtV_id);
            name = itemView.findViewById(R.id.cart_item_name_txtV_id);
            price = itemView.findViewById(R.id.cart_item_price_txtV_id);
            plus_img = itemView.findViewById(R.id.plus_img_id);
            minus_img = itemView.findViewById(R.id.minus_img_id);
            count_txtV = itemView.findViewById(R.id.item_number_txtV_id);
            view_back_ground = itemView.findViewById(R.id.view_back_ground);
            view_foreground = itemView.findViewById(R.id.layout_item_data);

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

            plus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CartProductsModel.Product cartItemModel = list.get(position);
                        int item_count = Integer.parseInt(count_txtV.getText().toString());
                        Log.i(" count", item_count + "");
                        item_count++;
                        Log.i(" count", item_count + "");
                        count_txtV.setText(String.valueOf(item_count));
                        cartItemModel.setQuentity(item_count);
                        ShoppingCart.total += cartItemModel.getPrice();
                        ShoppingCart.total_cart_val += cartItemModel.getPrice();
                        ShoppingCart.items_price.setText(String.valueOf(ShoppingCart.total));
                        ShoppingCart.items_price.append("$");
                        ShoppingCart.total_cart_price_txtV.setText(String.valueOf(ShoppingCart.total_cart_val));
                        ShoppingCart.total_cart_price_txtV.append("$");
                    }
                }
            });

            minus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && Integer.parseInt(count_txtV.getText().toString()) > 1) {
                        CartProductsModel.Product cartItemModel = list.get(position);
                        int item_count = Integer.parseInt(count_txtV.getText().toString());
                        item_count--;
                        count_txtV.setText(String.valueOf(item_count));
                        cartItemModel.setQuentity(item_count);
                        ShoppingCart.total -= cartItemModel.getPrice();
                        ShoppingCart.total_cart_val -= cartItemModel.getPrice();
                        ShoppingCart.items_price.setText(String.valueOf(ShoppingCart.total));
                        ShoppingCart.items_price.append("$");
                        ShoppingCart.total_cart_price_txtV.setText(String.valueOf(ShoppingCart.total_cart_val));
                        ShoppingCart.total_cart_price_txtV.append("$");
                    }
                }
            });
        }
    }
}
