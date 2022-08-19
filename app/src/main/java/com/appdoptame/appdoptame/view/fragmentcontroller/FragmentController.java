package com.appdoptame.appdoptame.view.fragmentcontroller;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Fade;
import androidx.transition.Transition;

import com.appdoptame.appdoptame.view.MainActivity;

import com.appdoptame.appdoptame.R;
import com.google.android.material.transition.MaterialSharedAxis;

public class FragmentController {
    private static MainActivity activityInstance;
    private static FragmentManager fragmentManager;

    private FragmentController(Context context){
        activityInstance = (MainActivity) context;
        fragmentManager  = activityInstance.getSupportFragmentManager();

        SetFragmentLogin.set();
        //SetFragmentMain.set();
    }

    public static void init(Context context){
        new FragmentController(context);
    }

    public static void reload(Context context){
        activityInstance = (MainActivity) context;
        fragmentManager  = activityInstance.getSupportFragmentManager();
    }

    public static void addFragmentFade(Fragment fragment){
        Fragment topFragment = getTopFragment();

        Fade fade = new Fade();
        if(topFragment != null){
            topFragment.setExitTransition(fade);
            topFragment.setReenterTransition(fade);
        }

        fragment.setEnterTransition(fade);
        fragment.setReturnTransition(fade);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public static void addFragmentUp(Fragment fragment){
        Fragment topFragment = getTopFragment();

        Transition upTransition = new MaterialSharedAxis(MaterialSharedAxis.X, true).setDuration(500);
        Transition downTransition = new MaterialSharedAxis(MaterialSharedAxis.X, false).setDuration(500);
        if(topFragment != null){
            topFragment.setExitTransition(upTransition);
            topFragment.setReenterTransition(downTransition);
        }
        fragment.setEnterTransition(upTransition);
        fragment.setReturnTransition(downTransition);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private static Fragment getTopFragment(){
        if(fragmentManager.getFragments().size() != 0){
            return fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1);
        }

        return null;
    }

    public static void onBackPressed(){
        int count = fragmentManager.getBackStackEntryCount();
        if (count <= 1) {
            activityInstance.moveTaskToBack(true);
        } else {
            fragmentManager.popBackStackImmediate();
        }
    }
}
