package com.elbndarmarket.networking;

import com.elbndarmarket.model.AddOrderProductModel;
import com.elbndarmarket.model.AddToCartResponseModel;
import com.elbndarmarket.model.CartProductsModel;
import com.elbndarmarket.model.CategoryDataModel;
import com.elbndarmarket.model.ChangePasswordModel;
import com.elbndarmarket.model.CheckOutResponseModel;
import com.elbndarmarket.model.DelOrAddWishlistModel;
import com.elbndarmarket.model.DeleteCartModel;
import com.elbndarmarket.model.DeleteNotificatiomModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.model.NotificationsModel;
import com.elbndarmarket.model.ProductDetailsModel;
import com.elbndarmarket.model.OrderDetailsModel;
import com.elbndarmarket.model.RegisterationModel;
import com.elbndarmarket.model.SliderModel;
import com.elbndarmarket.model.UpdateAddressResponse;
import com.elbndarmarket.model.UserLoginModel;
import com.elbndarmarket.model.UserOrderModel;
import com.elbndarmarket.model.WishListModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Ma7MouD on 9/18/2018.
 */

public interface ApiServiceInterface {

    // ---------------- LogIn -------------------------------
    @POST("api/login")
    Call<UserLoginModel> userLogin(@Query("username") String username,
                                   @Query("password") String password
    );


    // ---------------- Register -------------------------------
    @Multipart
    @POST("api/register")
    Call<RegisterationModel> Register(@Part("username") RequestBody username,
                                      @Query("email") String email,
                                      @Query("Mobile") String Mobile,
                                      @Query("password") String password,
                                      @Query("password_confirmation") String pass_confirmation,
                                      @Part MultipartBody.Part user_image
    );


    // ---------------- LogIn -------------------------------
    @POST("api/login")
    Call<UserLoginModel> forgetPassword(@Query("username") String username);


    // ---------------- USER Information -------------------------------
    @GET("api/userinfo")
    Call<UserLoginModel> getUserInfo(@Query("user_id") int user_id,
                                     @Query("api_token") String api_token
    );


    // ---------------- Change Password -------------------------------
    @POST("api/changepassword")
    Call<ChangePasswordModel> changePassword(@Query("user_id") int user_id,
                                             @Query("current_password") String current_password,
                                             @Query("password") String password,
                                             @Query("password_confirmation") String pass_confirmation,
                                             @Query("api_token") String api_token
    );


    // ---------------- Change Password -------------------------------
    @Multipart
    @POST("api/updateprofile")
    Call<UserLoginModel> updateProfile(@Part("username") RequestBody username,
                                       @Query("email") String email,
                                       @Query("Mobile") String Mobile,
                                       @Query("api_token") String api_token,
                                       @Query("user_id") int user_id,
                                       @Part MultipartBody.Part user_image
    );


    // ---------------- Slider Images -------------------------------
    @GET("api/getslider")
    Call<SliderModel> getSlider();


    // ---------------- Main Category -------------------------------
    @GET("api/maincategory")
    Call<CategoryDataModel> getMainCategory(@Query("page") int page,
                                            @Query("limit") int limit
    );


    // ---------------- All Category -------------------------------
    @GET("api/allcategory")
    Call<CategoryDataModel> getAllCategory(@Query("page") int page,
                                           @Query("limit") int limit
    );


    // ---------------- All Category -------------------------------
    @GET("api/subcategory")
    Call<CategoryDataModel> getSubCategory(@Query("page") int page,
                                           @Query("limit") int limit,
                                           @Query("cat_id") int cat_id

    );


    // ---------------- All Category -------------------------------
    @GET("api/productbycatid")
    Call<FeaturedProductsModel> getCategoryProducts(@Query("page") int page,
                                                    @Query("limit") int limit,
                                                    @Query("cat_id") int cat_id

    );


    // ---------------- All Category -------------------------------
    @GET("api/allproducts")
    Call<FeaturedProductsModel> getAllProducts(@Query("page") int page,
                                               @Query("limit") int limit
    );

    // ---------------- All Category -------------------------------
    @GET("api/productbysubcatid")
    Call<FeaturedProductsModel> subCategoryProducts(@Query("page") int page,
                                                    @Query("limit") int limit,
                                                    @Query("sub_cat_id") int sub_cat_id
    );


    // ---------------- Get Featured Products -------------------------------
    @GET("api/getfeaturedproduct")
    Call<FeaturedProductsModel> getFeaturedProducts(@Query("page") int page,
                                                    @Query("limit") int limit,
                                                    @Query("user_id") int user_id
    );


    // ---------------- Product Details -------------------------------
    @GET("api/productdetails")
    Call<ProductDetailsModel> productDetails(@Query("product_id") int product_id,
                                             @Query("user_id") int user_id);


    // ---------------- Add Order Product ------------------------------
    @POST("api/addorder")
    Call<AddOrderProductModel> addOrder(@Query("Order_User_id") int Order_User_id,
                                        @Query("TotalCost") int TotalCost,
                                        @Query("ItemsNo") int ItemsNo,
                                        @Query("PayMethod") String PayMethod,
                                        @Query("TotalShippingCost") int TotalShippingCost,
                                        @Query("api_token") String api_token
    );


    // ------------------------For WishList ----------------
    // ---------------- Get WishList Data ------------------------------
    @GET("api/getwishlist")
    Call<WishListModel> getWishListData(@Query("user_id") int user_id,
                                        @Query("api_token") String api_token,
                                        @Query("page") int page,
                                        @Query("limit") int limit
    );

    // ---------------- Add To WishList ------------------------------
    @POST("api/addwishlist")
    Call<DelOrAddWishlistModel> addToWishList(@Query("user_id") int user_id,
                                              @Query("Product_id") int Product_id,
                                              @Query("api_token") String api_token
    );

    // ---------------- Delete From WishList ------------------------------
    @POST("api/deletewishlistproduct")
    Call<DelOrAddWishlistModel> deleteFromWishList(@Query("user_id") int user_id,
                                                   @Query("api_token") String api_token,
                                                   @Query("product_id") int product_id
    );


    // ----------------------- User Orders -------------------
    // ---------------- Get User Orders ------------------------------
    @GET("api/getuserorders")
    Call<UserOrderModel> getUserOrders(@Query("user_id") int user_id,
                                       @Query("api_token") String api_token
    );


    // ---------------- Get Order Details ------------------------------
    @GET("api/getuserorderdetails")
    Call<OrderDetailsModel> getOrderDetails(@Query("user_id") int user_id,
                                            @Query("api_token") String api_token,
                                            @Query("Order_id") int Order_id
    );


//    // ---------------- Add Order Product ------------------------------
//    @POST("api/getuserorderdetails")
//    Call<OrderDetailsModel> addOrderProduct(@Query("Order_id") int Order_id,
//                                            @Query("Product_id") int Product_id,
//                                            @Query("Quantity") int Quantity,
//                                            @Query("UnitCost") int UnitCost,
//                                            @Query("TotalCost") int TotalCost,
//                                            @Query("api_token") int api_token
//    );


    // ------------------------Dealing With Cart --------------------------------
    // ---------------- Add TO Cart ------------------------------
    @POST("api/addcart")
    Call<AddToCartResponseModel> addItemToCart(@Query("product_id") int product_id,
                                               @Query("user_id") int user_id,
                                               @Query("qty") int qty,
                                               @Query("api_token") String api_token
    );

    // ---------------- Delete Cart Item ------------------------------
    @POST("api/deleteproductcart")
    Call<AddToCartResponseModel> delateCartItem(@Query("product_id") int product_id,
                                                @Query("cart_id") int cart_id,
                                                @Query("api_token") String api_token
    );

    // ---------------- Update Cart Count Item ------------------------------
    @POST("api/updateproductcartqty")
    Call<AddToCartResponseModel> updateCartCountItem(@Query("product_id") int product_id,
                                                     @Query("cart_id") int cart_id,
                                                     @Query("qty") int qty,
                                                     @Query("api_token") String api_token
    );

    // ---------------- Update Cart Count Item ------------------------------
    @GET("api/getcartproduct")
    Call<CartProductsModel> getCartProducts(@Query("user_id") int user_id,
                                            @Query("api_token") String api_token
    );


    // ---------------- Mke Check Out ------------------------------
    @POST("api/addorder")
    Call<CheckOutResponseModel> makeCheckOut(@Query("Order_User_id") int Order_User_id,
                                             @Query("TotalCost") double TotalCost,
                                             @Query("ItemsNo") int ItemsNo,
                                             @Query("PayMethod") String PayMethod,
                                             @Query("TotalShippingCost") double TotalShippingCost,
                                             @Query("api_token") String api_token,
                                             @Query("products") String products,
                                             @Query("cart_id") int Cart_id
    );


    // ---------------- get Notifications ------------------------------
    @GET("api/getnotfication")
    Call<NotificationsModel> getUserNotifications(@Query("user_id") int user_id,
                                                  @Query("api_token") String api_token
    );


    // ---------------- Delete Notification ------------------------------
    @POST("api/deletenotfication")
    Call<DeleteNotificatiomModel> deleteNotification(@Query("user_id") int user_id,
                                                     @Query("notfication_id") String notfication_id,
                                                     @Query("api_token") String api_token
    );


    // ----------------- User Location ------------------------------
    @GET("api/updateuseraddress")
    Call<UpdateAddressResponse> userAddress(@Query("Country") String Country,
                                            @Query("City") String City,
                                            @Query("Address") String Address,
                                            @Query("building_no") String building_no,
                                            @Query("floor_no") String floor_no,
                                            @Query("apartment_no") String apartment_no,
                                            @Query("villa_no") String villa_no,
                                            @Query("other") String other,
                                            @Query("lng") double lng,
                                            @Query("lat") double lat,
                                            @Query("user_id") int user_id,
                                            @Query("api_token") String api_token
    );


    // ---------------- Delete Notification ------------------------------
    @POST("api/ios/deletecart")
    Call<DeleteCartModel> deleteCartProducts(@Query("cart_id") int cart_id,
                                             @Query("api_token") String api_token
    );


}