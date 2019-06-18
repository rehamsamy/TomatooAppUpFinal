package com.elbndarmarket.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.CategoriesPagerAdapter;
import com.elbndarmarket.model.FeaturedProductsModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    CategoriesPagerAdapter categoriesPagerAdapter;

    private ArrayList<FeaturedProductsModel> allCategoriesList;
    private ArrayList<FeaturedProductsModel> fruitCategoriesList;
    private ArrayList<FeaturedProductsModel> bakeryCategoriesList;
    private ArrayList<FeaturedProductsModel> forthCategoriesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_categories, container, false);
        tabLayout = view.findViewById(R.id.categories_tabLayout_id);
        viewPager = view.findViewById(R.id.categories_viewPager_id);

        createAllList();

        categoriesPagerAdapter = new CategoriesPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), allCategoriesList, fruitCategoriesList, bakeryCategoriesList, forthCategoriesList);
        viewPager.setAdapter(categoriesPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    public void createAllList() {
//        allCategoriesList = new ArrayList<>();
//        allCategoriesList.add(new FeaturedProductsModel("Apple Juice", R.drawable.home_apple_juice, "9", 1));
//        allCategoriesList.add(new FeaturedProductsModel("Apple Juice", R.drawable.home_category_bakery, "9", 0));
//        allCategoriesList.add(new FeaturedProductsModel("Apple", R.drawable.home_apple_juice, "5", 0));
//        allCategoriesList.add(new FeaturedProductsModel("Apple Juice", R.drawable.home_category_bakery, "7", 1));
//        allCategoriesList.add(new FeaturedProductsModel("Apple", R.drawable.home_apple_juice, "8", 1));
    }

}
