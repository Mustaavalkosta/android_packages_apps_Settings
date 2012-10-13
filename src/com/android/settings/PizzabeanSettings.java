package com.android.settings;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.SettingsFragment;

public class PizzabeanSettings extends SettingsFragment {

    private final static String TAG = PizzabeanSettings.class.getSimpleName();
    private static final String STATUSBAR_SIXBAR_SIGNAL = "pref_statusbar_sixbar_signal";

    private ContentResolver mCr;
    private PreferenceScreen mPrefSet;

    private CheckBoxPreference mUseSixbaricons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pizzabean_settings);

        mUseSixbaricons = (CheckBoxPreference) findPreference("statusbar_sixbar");
    }

    @Override
    public void onResume() {
        super.onResume();
        
        mUseSixbaricons.setChecked(Settings.System.getInt(getContentResolver(),
        		Settings.System.STATUSBAR_6BAR_SIGNAL, 0) != 0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
    		Preference preference) {
        if (preference == mUseSixbaricons) {
            Settings.System.putInt(getContentResolver(),
            		Settings.System.STATUSBAR_6BAR_SIGNAL,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
