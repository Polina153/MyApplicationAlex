package com.example.myapplication.presenter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.view.DetailsFragment;

public class Navigator {
    private final FragmentManager fragmentManager;

    public Navigator(@Nullable FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment) {
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            if (fragment instanceof DetailsFragment) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

    public void popBackStack() {
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }
}
