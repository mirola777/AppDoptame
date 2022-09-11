package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.util.DisplayManager;
import com.appdoptame.appdoptame.view.MainActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class FragmentMain extends Fragment {
    // Fragments
    private FragmentFeed          fragmentFeed;
    private FragmentMessages      fragmentMessages;
    private FragmentPostPet       fragmentPostPet;
    private FragmentNotifications fragmentNotifications;
    private FragmentProfile       fragmentProfile;

    // Consonants
    private static final int FEED          = 0;
    private static final int MESSAGES      = 1;
    private static final int POST_PET      = 2;
    private static final int NOTIFICATIONS = 3;
    private static final int PROFILE       = 4;

    // Components
    private ViewPager2           mainViewPager;
    private BottomNavigationView bottomNavigation;
    private MaterialToolbar      toolbar;

    public FragmentMain(){

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mainViewPager != null)
            outState.putInt("PAGE", mainViewPager.getCurrentItem());
        else
            outState.putInt("PAGE", 0);
    }

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
        if(savedInstanceState != null){
            mainViewPager.setCurrentItem(savedInstanceState.getInt("PAGE"), false);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public void setFeedPosition(){
        if(mainViewPager.getCurrentItem() != FEED){
            mainViewPager.setCurrentItem(FEED, true);
        }
    }

    public boolean isOnFeedPosition(){
        if(mainViewPager != null){
            return mainViewPager.getCurrentItem() == FEED;
        }

        return false;
    }

    private void MainViewPagerFunction(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter((MainActivity) requireContext());
        mainViewPager = requireView().findViewById(R.id.main_pager);
        mainViewPager.setUserInputEnabled(false);
        mainViewPager.setOffscreenPageLimit(4);
        mainViewPager.setAdapter(viewPagerAdapter);
        mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case FEED:
                        bottomNavigation.setSelectedItemId(R.id.menu_feed);
                        break;
                    case MESSAGES:
                        bottomNavigation.setSelectedItemId(R.id.menu_messages);
                        break;
                    case POST_PET:
                        bottomNavigation.setSelectedItemId(R.id.menu_post_pet);
                        break;
                    case NOTIFICATIONS:
                        bottomNavigation.setSelectedItemId(R.id.menu_notifications);
                        break;
                    case PROFILE:
                        bottomNavigation.setSelectedItemId(R.id.menu_profile);
                        break;
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomNavigationViewFunction(){
        bottomNavigation = requireView().findViewById(R.id.main_bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_feed:
                    mainViewPager.setCurrentItem(FEED);
                    break;
                case R.id.menu_messages:
                    mainViewPager.setCurrentItem(MESSAGES);
                    break;
                case R.id.menu_post_pet:
                    mainViewPager.setCurrentItem(POST_PET);
                    break;
                case R.id.menu_notifications:
                    mainViewPager.setCurrentItem(NOTIFICATIONS);
                    break;
                case R.id.menu_profile:
                    mainViewPager.setCurrentItem(PROFILE);
                    break;
            }
            return true;
        });
    }

    private void loadComponents(){
        fragmentFeed          = new FragmentFeed();
        fragmentMessages      = new FragmentMessages();
        fragmentPostPet       = new FragmentPostPet();
        fragmentNotifications = new FragmentNotifications();
        fragmentProfile       = new FragmentProfile();

        loadToolbar();
        bottomNavigationViewFunction();
        MainViewPagerFunction();
    }

    private void loadToolbar(){
        toolbar = requireView().findViewById(R.id.main_toolbar);

        // Padding toolbar
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) toolbar.getLayoutParams();
        int height = params.height;
        params.height = DisplayManager.getStatusBarHeight() + params.height;
        toolbar.setPadding(0, params.height - height, 0, 0);
        toolbar.setLayoutParams(params);
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case FEED:
                    return  fragmentFeed;
                case MESSAGES:
                    return  fragmentMessages;
                case POST_PET:
                    return  fragmentPostPet;
                case NOTIFICATIONS:
                    return  fragmentNotifications;
                case PROFILE:
                    return  fragmentProfile;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}