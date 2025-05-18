package com.example.rma_2_eldar_salihovic;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MovieAppPrefs";
    private static final String PREF_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        if (!prefs.contains(PREF_THEME)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PREF_THEME, true);
            editor.apply();
        }


        boolean isLightTheme = prefs.getBoolean(PREF_THEME, true);
        setTheme(isLightTheme ? R.style.Theme_RMA_2_Eldar_Salihovic_Light : R.style.Theme_RMA_2_Eldar_Salihovic);

        super.onCreate(savedInstanceState);
    }
}