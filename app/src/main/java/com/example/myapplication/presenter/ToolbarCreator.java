package com.example.myapplication.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolbarCreator  {
    public void setActionBar(@NonNull Toolbar toolbar,
                             @NonNull AppCompatActivity activity) {
        activity.setSupportActionBar(toolbar);
    }

    public void setButtonBack(@Nullable ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
