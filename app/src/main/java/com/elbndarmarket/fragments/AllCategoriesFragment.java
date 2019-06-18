package com.elbndarmarket.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.activities.ProductDetails;
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.adapters.FeaturedProductsAdapter;
import com.elbndarmarket.interfaces.RecyclerOnItemClickListner;
import com.elbndarmarket.model.Category;
import com.elbndarmarket.model.CategoryDataModel;
import com.elbndarmarket.model.DelOrAddWishlistModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.elbndarmarket.utils.DealingWithCartItem;
import com.elbndarmarket.utils.DealingWithWishListItem;
import com.elbndarmarket.utils.EndlessRecyclerViewScrollListener;
import com.elbndarmarket.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllCategoriesFragment extends Fragment {

    FeaturedProductsAdapter products_adapter;
    ProgressBar progressBar;
    TabLayout categories_tablayout;
    TabLayout subCategories_tabLayout;
    RecyclerView recyclerView;
    TextView no_available_prods_txtV;

    private final String TAG = this.getClass().getSimpleName();
    private NetworkAvailable networkAvailable;

    private ArrayList<Category> categorylList;
    private ArrayList<Category> sub_CategorylList;
    private ArrayList<FeaturedProductsModel.ProductData> productslList;
    private int all_products_page = 1;
    private int last_cat_position = 0;
    private String type = "all";

    private int subCategory_page = 1;
    private int category_products_page = 1;

    DealingWithWishListItem dealingWithWishListItem;
    DealingWithCartItem dealingWithCartItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_categories, container, false);
        dealingWithWishListItem = new DealingWithWishListItem(getActivity());
        dealingWithCartItem = new DealingWithCartItem(getActivity());

        progressBar = view.findViewById(R.id.all_categories_progress_id);
        no_available_prods_txtV = view.findViewById(R.id.no_available_products_txtV_id);
        recyclerView = view.findViewById(R.id.products_recycler_id);
        categories_tablayout = view.findViewById(R.id.categories_tabLayout_id);
        subCategories_tabLayout = view.findViewById(R.id.subCategories_tabLayout_id);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        networkAvailable = new NetworkAvailable(getActivity());
        categorylList = new ArrayList<>();
        productslList = new ArrayList<>();
        buildRecyclerView();
        if (networkAvailable.isNetworkAvailable())
            getAllCategoriesList(1);
        else
            Toast.makeText(getActivity(), getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        categories_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressBar.setVisibility(View.VISIBLE);
                last_cat_position = tab.getPosition();
                productslList.clear();
                if (tab.getPosition() == 0) {
                    all_products_page = 1;
                    subCategories_tabLayout.setVisibility(View.GONE);
                    getAllProducts(all_products_page);
                } else {
//                    subCategories_tabLayout.setVisibility(View.VISIBLE);
                    checkForSubCategory(subCategory_page, categorylList.get(tab.getPosition()).getCategory_id());
                }
//                else if (type.equals("subCategory")) {
//                    getSubCategoryProducts(all_products_page, sub_CategorylList.get(tab.getPosition()).getCategory_id());
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        subCategories_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressBar.setVisibility(View.VISIBLE);
                productslList.clear();
                Log.i("sub_pos:", tab.getPosition() + "");
                getSubCategoryProducts(sub_CategorylList.get(tab.getPosition()).getCategory_id());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getSubCategoryProducts(int subCat_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.subCategoryProducts(subCategory_page, 20, subCat_id);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_prods_txtV.setVisibility(View.GONE);
//                    products_adapter.notifyItemRangeInserted(products_adapter.getItemCount(), productslList.size() - 1);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_available_prods_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_available_prods_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkForSubCategory(int page, final int category_id) {
        sub_CategorylList = new ArrayList<>();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getSubCategory(page, 20, category_id);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    sub_CategorylList.addAll(response.body().getCategories());
                    subCategories_tabLayout.removeAllTabs();
                    subCategories_tabLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < sub_CategorylList.size(); i++) {
                        subCategories_tabLayout.addTab(
                                subCategories_tabLayout.newTab()
                                        .setText(sub_CategorylList.get(i).getCategory_name_en()));
                    }
                } else {
                    getCategoryProducts(category_id);
                    subCategories_tabLayout.removeAllTabs();
                    subCategories_tabLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCategoryProducts(int category_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getCategoryProducts(category_products_page, 20, category_id);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_prods_txtV.setVisibility(View.GONE);
//                    products_adapter.notifyItemRangeInserted(products_adapter.getItemCount(), productslList.size() - 1);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_available_prods_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_available_prods_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAllProducts(int all_products_page) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getAllProducts(all_products_page, 20);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
//                    buildRecyclerView(productslList);
                    Log.i("cat_list", categorylList.size() + "");
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_prods_txtV.setVisibility(View.GONE);

//                    products_adapter.notifyItemRangeInserted(products_adapter.getItemCount(), productslList.size() - 1);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_available_prods_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_available_prods_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void getAllCategoriesList(int page) {
        categorylList = new ArrayList<>();
        categorylList.add(new Category(0, "All", "الكل", "All", ""));
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getAllCategory(page, 20);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    categorylList.addAll(response.body().getCategories());
                    for (int i = 0; i < categorylList.size(); i++) {
                        if (PreferencesHelper.getSomeStringValue(getActivity()).equals("en")) {
                            categories_tablayout.addTab(
                                    categories_tablayout.newTab().setText(categorylList.get(i).getCategory_name_en()));
                        } else {
                            categories_tablayout.addTab(
                                    categories_tablayout.newTab().setText(categorylList.get(i).getCategory_name_ar()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void buildRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        products_adapter = new FeaturedProductsAdapter(getActivity(), productslList);
        recyclerView.setAdapter(products_adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                getAllProducts(all_products_page);

                if (last_cat_position == 0) {
                    all_products_page++;
                    getAllProducts(all_products_page);
                }
            }
        });

        products_adapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("prod_id", productslList.get(position).getProduct_id());
                if (PreferencesHelper.getSomeStringValue(getActivity()).equals("ar")) {
                    intent.putExtra("prod_name", productslList.get(position).getProduct_name_ar());
                } else {
                    intent.putExtra("prod_name", productslList.get(position).getProduct_name_en());
                }
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                if (productslList.get(position).getCart() == 0) {
                    dealingWithCartItem.addTocart(getActivity(), productslList.get(position), progressBar, cart_image, productslList.get(position).getProduct_id(), MainActivity.userData.getId(), 1, MainActivity.userData.getToken());
                } else {
                    dealingWithCartItem.removeFromCart(getActivity(), productslList.get(position), progressBar, cart_image, productslList.get(position).getProduct_id(), dealingWithCartItem.Cart_id, MainActivity.userData.getToken());
                }
            }

            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
                if (productslList.get(position).getWishlists() == 0)
                    dealingWithWishListItem.addWishListItem(productslList.get(position), progressBar, wishlist_image);
                else
                    removeWishListItem(productslList.get(position).getProduct_id(), position);
            }
        });

    }

    private void removeWishListItem(int productId, final int position) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainActivity.userData.getId(), MainActivity.userData.getToken(), productId);
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    productslList.get(position).setWishlists(0);
                    products_adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}