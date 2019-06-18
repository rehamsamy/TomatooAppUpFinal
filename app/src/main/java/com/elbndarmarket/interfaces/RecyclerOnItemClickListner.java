package com.elbndarmarket.interfaces;

import android.widget.ImageView;

public interface RecyclerOnItemClickListner {

    void OnItemClick(int position);

    void OnCartClick(int position, ImageView cart_image);

    void OnWishListClick(int position, ImageView wishlist_image);
}
