package com.example.foodbank;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private Switch switchButton_switchMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        // Navigation Menu
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categories, R.id.nav_products, R.id.nav_add_product, R.id.nav_favorites, R.id.nav_healthy, R.id.nav_avoid, R.id.nav_share
        )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Hide keyboard
        hideKeyboard(drawer);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Switch mode
        switchMode();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void switchMode() {
        switchButton_switchMode = findViewById(R.id.switchButton_switchMode);
        setToggle();
        switchButton_switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchButton_switchMode.isChecked()) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                }
            }
        });
    }

    public void setToggle() {

        switchButton_switchMode = findViewById(R.id.switchButton_switchMode);

        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                switchButton_switchMode.setChecked(false);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                switchButton_switchMode.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                switchButton_switchMode.setChecked(false);
                break;
        }
    }
}