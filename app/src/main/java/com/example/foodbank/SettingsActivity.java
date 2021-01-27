package com.example.foodbank;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.foodbank.db.SettingsRoomDatabase;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }

    }

    @Override
    protected void onResume() {
        switchMode();
        setToggle();
        super.onResume();
    }

    public void switchMode() {
        SwitchMaterial switchButton_switchMode = findViewById(R.id.switchButton_switchMode);

        // Update mode setting
        List<Settings> settings = getSettings();

        switchButton_switchMode.setOnClickListener(v -> {
            if (switchButton_switchMode.isChecked()) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);
                settings.get(0).setDarkMode(true);
                update(settings.get(0));
            } else {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                settings.get(0).setDarkMode(false);
                update(settings.get(0));
            }
        });
    }

    public void setToggle() {
        SwitchMaterial switchButton_switchMode = findViewById(R.id.switchButton_switchMode);
        TextView textView_modeStatus = findViewById(R.id.textView_modeStatus);

        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                switchButton_switchMode.setChecked(true);
                textView_modeStatus.setText("Enabled");
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                textView_modeStatus.setText("Disabled");
                break;
            default:
                switchButton_switchMode.setChecked(false);
                textView_modeStatus.setText("");
                break;
        }
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Settings> getSettings() {
        return SettingsRoomDatabase.getDatabase(this).settingsDao().getSettings();
    }

    void update(Settings settings) {
        SettingsRoomDatabase.getDatabase(this).settingsDao().update(settings);
    }
}