package com.elbndarmarket.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.elbndarmarket.R;
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.activities.ProductDetails;
import com.elbndarmarket.adapters.MainCategoriesAdapter;
import com.elbndarmarket.adapters.MainFeaturedAdapter;
import com.elbndarmarket.interfaces.RecyclerOnItemClickListner;
import com.elbndarmarket.model.Category;
import com.elbndarmarket.model.CategoryDataModel;
import com.elbndarmarket.model.DelOrAddWishlistModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.model.SliderModel;
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


public class MainFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    NetworkAvailable networkAvailable;
    RecyclerView categories_recyclerV, featuredProducts_recyclerV;
    TextView seeAll_txtV;

    // Categories
    private ProgressBar categories_progress, featured_progress;
    private List<Category> categoriesList;
    MainCategoriesAdapter categoriesAdapter;
    SliderLayout sliderLayout;

    //Featured Products
    List<FeaturedProductsModel.ProductData> featuredProductsList;
    MainFeaturedAdapter mainFeaturedAdapter;
    ArrayList<String> layouts = new ArrayList<>();

    private int category_current_page = 1;
    private int featured_page = 1;
    private int featured_limit = 10;


    private List<SliderModel.Slider> images_list;
    private DealingWithCartItem dealingWithCartItem;
    private DealingWithWishListItem dealingWithWishListItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkAvailable = new NetworkAvailable(getActivity());
        if (!networkAvailable.isNetworkAvailable())
            Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, null);
        seeAll_txtV = view.findViewById(R.id.see_all_txtV);
        categories_progress = view.findViewById(R.id.mainCategories_progress_id);
        featured_progress = view.findViewById(R.id.mainFeatured_progress_id);
        sliderLayout = view.findViewById(R.id.banner_slider);
        featuredProducts_recyclerV = view.findViewById(R.id.featured_products_recyclerV_id);

        categories_progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        featured_progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        categoriesList = new ArrayList<>();
        featuredProductsList = new ArrayList<>();
        dealingWithCartItem = new DealingWithCartItem(getActivity());
        dealingWithWishListItem = new DealingWithWishListItem(getActivity());

        buildCategoriesRecycler(view);
        buildFeaturesProdsRecycler();

        if (networkAvailable.isNetworkAvailable()) {
            getCategoriesList(category_current_page);
            createFeaturedProdList(featured_page);
            getSliderImages();
        }

        seeAll_txtV.setOnClickListener(this);

        return view;
    }

    private void getSliderImages() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<SliderModel> call = serviceInterface.getSlider();
        call.enqueue(new Callback<SliderModel>() {
            @Override
            public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                SliderModel sliderModel = response.body();
                if (sliderModel.getStatus()) {
                    images_list = sliderModel.getSlider();
                    for (int i = 0; i < images_list.size(); i++) {

                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        // initialize a SliderLayout
                        textSliderView.image(images_list.get(i).getSlider_image())
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(MainFragment.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        sliderLayout.addSlider(textSliderView);

                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(4000);
                    sliderLayout.addOnPageChangeListener(MainFragment.this);
                }
            }

            @Override
            public void onFailure(Call<SliderModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new AllCategoriesFragment();
        displaySelectedFragment(fragment);
    }

    private void getCategoriesList(int category_current_page) {
        categories_progress.setVisibility(View.VISIBLE);
        // Bind Data For Categories
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getMainCategory(category_current_page, 10);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    categoriesList.addAll(response.body().getCategories());
                    categoriesAdapter.notifyDataSetChanged();
                }
                categories_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
                categories_progress.setVisibility(View.GONE);
            }
        });

    }

    private void buildCategoriesRecycler(View view) {
        categories_recyclerV = view.findViewById(R.id.main_categories_recyclerV_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // For Categories RecyclerView
        categories_recyclerV.setLayoutManager(linearLayoutManager);
        categories_recyclerV.setHasFixedSize(true);
        // Categories
        categoriesAdapter = new MainCategoriesAdapter(getActivity(), categoriesList);
        categories_recyclerV.setAdapter(categoriesAdapter);
        categories_recyclerV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                category_current_page++;
                getCategoriesList(category_current_page);
            }
        });
    }

    private void createFeaturedProdList(int featured_page) {
        featured_progress.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getFeaturedProducts(featured_page, featured_limit, MainActivity.userData.getId());
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                FeaturedProductsModel featuredProductsModel = response.body();
                if (response.body().getStatus()) {
                    featuredProductsList.addAll(response.body().getProducts());
//                    mainFeaturedAdapter.notifyItemRangeInserted(mainFeaturedAdapter.getItemCount(), featuredProductsList.size() - 1);
                    mainFeaturedAdapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(getActivity(), featuredProductsModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
                featured_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                featured_progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void buildFeaturesProdsRecycler() {
        // For Featured  RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        featuredProducts_recyclerV.setLayoutManager(linearLayoutManager);
        featuredProducts_recyclerV.setHasFixedSize(true);
        // Set Adapter ...
        mainFeaturedAdapter = new MainFeaturedAdapter(getActivity(), featuredProductsList);
        featuredProducts_recyclerV.setAdapter(mainFeaturedAdapter);

        featuredProducts_recyclerV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                featured_page++;
                createFeaturedProdList(featured_page);
            }
        });

        mainFeaturedAdapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("prod_id", featuredProductsList.get(position).getProduct_id());
                if (PreferencesHelper.getSomeStringValue(getActivity()).equals("ar")) {
                    intent.putExtra("prod_name", featuredProductsList.get(position).getProduct_name_ar());
                    intent.putExtra("prod_category", featuredProductsList.get(position).getCategory_name_ar());
                } else {
                    intent.putExtra("prod_name", featuredProductsList.get(position).getProduct_name_en());
                    intent.putExtra("prod_category", featuredProductsList.get(position).getCategory_name_en());
                }
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                Log.i("cart: ", featuredProductsList.get(position).getCart() + "");
                if (featuredProductsList.get(position).getCart() == 0) {
                    dealingWithCartItem.addTocart(getActivity(), featuredProductsList.get(position), featured_progress, cart_image, featuredProductsList.get(position).getProduct_id(), MainActivity.userData.getId(), 1, MainActivity.userData.getToken());
//                    featuredProductsList.get(position).setCart(1);
                } else {
                    dealingWithCartItem.removeFromCart(getActivity(), featuredProductsList.get(position), featured_progress, cart_image, featuredProductsList.get(position).getProduct_id(), dealingWithCartItem.Cart_id, MainActivity.userData.getToken());
//                    featuredProductsList.get(position).setCart(0);
                }
            }

            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
                if (featuredProductsList.get(position).getWishlists() == 0) {
                    dealingWithWishListItem.addWishListItem(featuredProductsList.get(position), featured_progress, wishlist_image);
//                    featuredProductsList.get(position).setWishlists(1);
                } else {
                    removeWishListItem(featuredProductsList.get(position), position);
//                    featuredProductsList.get(position).setWishlists(0);
                }
            }
        });
    }

    private void removeWishListItem(final FeaturedProductsModel.ProductData item_model, final int position) {
        featured_progress.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainActivity.userData.getId(), MainActivity.userData.getToken(), item_model.getProduct_id());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    item_model.setWishlists(0);
                    mainFeaturedAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                featured_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
                featured_progress.setVisibility(View.GONE);
            }
        });
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_id, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
