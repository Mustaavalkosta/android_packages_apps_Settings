package com.android.settings;

import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;


import com.android.settings.R;
import com.android.settings.SettingsFragment;

public class PizzabeanSettings extends SettingsFragment {

    private final static String TAG = PizzabeanSettings.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pizzabean_settings);

    }
}
