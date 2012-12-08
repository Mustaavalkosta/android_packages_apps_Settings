package com.android.settings.pizzabean;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class StatusBar extends SettingsPreferenceFragment {

	private static final String TAG = "PizzaBean Statusbar";

	private static final String PREF_SIXBAR_ENABLED = "sixbar_enabled";

    private CheckBoxPreference mUseSixbar;

    private PreferenceScreen mPrefSet;
    private ContentResolver mCr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.pizzabean_statusbar);

		mPrefSet = getPreferenceScreen();
		mCr = getContentResolver();

        /** Sixbar signal icons toggle */
		mUseSixbar = (CheckBoxPreference) mPrefSet.findPreference(PREF_SIXBAR_ENABLED);
		mUseSixbar.setChecked(Settings.System.getInt(mCr,
        		Settings.System.STATUSBAR_6BAR_SIGNAL, 0) == 1);
	}

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
    		Preference preference) {
    	if (preference == mUseSixbar) {
            Settings.System.putInt(mCr, Settings.System.STATUSBAR_6BAR_SIGNAL,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        } 
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
