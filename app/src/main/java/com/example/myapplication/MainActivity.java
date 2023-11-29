package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.presenter.Navigator;
import com.example.myapplication.presenter.ToolbarCreator;
import com.example.myapplication.view.MainFragment;

public class MainActivity extends AppCompatActivity {
    private final ToolbarCreator toolbarCreator = new ToolbarCreator();
    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigator = new Navigator(getSupportFragmentManager());
        if (savedInstanceState == null) {
            navigator.addFragment(MainFragment.newInstance());
        }
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public ToolbarCreator getToolbarCreator() {
        return toolbarCreator;
    }
}