package com.android.settings.pizzabean;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class RecentApps extends SettingsPreferenceFragment {

	private static final String TAG = "PizzaBean RecentApps";

    private static final String PREF_RECENTS_MEM_DISPLAY = "recents_mem_display";

    private CheckBoxPreference mRecentsMemDisplay;

    private PreferenceScreen mPrefSet;
    private ContentResolver mCr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.pizzabean_recent_apps);

		mPrefSet = getPreferenceScreen();
		mCr = getContentResolver();

        /** Memory display toggle */
		mRecentsMemDisplay = (CheckBoxPreference) mPrefSet.findPreference(PREF_RECENTS_MEM_DISPLAY);
		mRecentsMemDisplay.setChecked(Settings.System.getInt(mCr,
        		Settings.System.RECENTS_MEM_DISPLAY, 0) == 1);
	}
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
    		Preference preference) {
        if (preference == mRecentsMemDisplay) {
            Settings.System.putInt(mCr, Settings.System.RECENTS_MEM_DISPLAY,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
