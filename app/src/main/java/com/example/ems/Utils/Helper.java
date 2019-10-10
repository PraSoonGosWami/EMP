package com.example.ems.Utils;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;

/**
 * Created by Prasoon Goswami
 * on 07-10-19
 * This is a public java class which has static fragment replacement methods
 */

public class Helper {

    public static final int LONG = 0;
    public static final int SHORT = -1;

    public static void swapFragments(Fragment fragment, int container, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(container, fragment);
        fragmentTransaction.commit();
    }
    public static void swapFragmentsWithBackStack(Fragment fragment, int container, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(container, fragment);
        fragmentTransaction.addToBackStack("home").commit();
    }
    public static void swapFragmentsWithBackStackAndBundle(Fragment fragment, Bundle bundle, int container, FragmentManager fragmentManager) {
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(container, fragment);
        fragmentTransaction.addToBackStack("home").commit();
    }

    //view animator
    public static void animateError(View view, int duration){
        YoYo.with(Techniques.Shake)
                .duration(duration)
                .playOn(view);
    }

    //snackbar
    public static void showSnackbar(String msg, Activity activity, int duration) {
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), msg, duration);
        snackbar.show();
    }

}
