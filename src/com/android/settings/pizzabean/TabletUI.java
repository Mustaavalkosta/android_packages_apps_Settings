package com.android.settings.pizzabean;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class TabletUI extends SettingsPreferenceFragment {

	private static final String TAG = "PizzaBean TabletUI";

    private static final String KEY_FORCE_TABLET_UI = "force_tabletui";
	private static final String KEY_DUAL_PANE = "dual_pane";

	private CheckBoxPreference mDualPane;
	private CheckBoxPreference mTabletui;
	private Preference mLcdDensity;
	private int newDensityValue;

    private PreferenceScreen mPrefSet; 
    private ContentResolver mCr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.pizzabean_tabletui);

		mPrefSet = getPreferenceScreen();
		mCr = getContentResolver();

		/** Dual pane toggle */
		mDualPane = (CheckBoxPreference) mPrefSet.findPreference(KEY_DUAL_PANE);
		mDualPane.setChecked(Settings.System.getInt(mCr,
				Settings.System.DUAL_PANE_SETTINGS, 0) == 1);

		/** Force tablet UI toggle */
		mTabletui = (CheckBoxPreference) mPrefSet.findPreference(KEY_FORCE_TABLET_UI);
		mTabletui.setChecked(Settings.System.getInt(mCr,
				Settings.System.FORCE_TABLET_UI, 0) == 1);

		/** Density changer */
		mLcdDensity = findPreference("lcd_density_setup");
		String currentProperty = SystemProperties.get("ro.sf.lcd_density");
		try {
			newDensityValue = Integer.parseInt(currentProperty);
		} catch (Exception e) {
			getPreferenceScreen().removePreference(mLcdDensity);
		}
		mLcdDensity.setSummary(getResources().getString(R.string.current_lcd_density) + currentProperty);
	}

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
    		Preference preference) {
    	if (preference == mDualPane) {
    		Settings.System.putInt(mCr, Settings.System.DUAL_PANE_SETTINGS,
    				((CheckBoxPreference) preference).isChecked() ? 1 : 0);
    		return true;    		
    	} else if (preference == mTabletui) {
    		Settings.System.putInt(mCr, Settings.System.FORCE_TABLET_UI,
    				((CheckBoxPreference) preference).isChecked() ? 1 : 0);
    		return true;
    	} else if (preference == mLcdDensity) {
    		((PreferenceActivity) getActivity()).startPreferenceFragment(new DensityChanger(), true);
    		return true;
    	}
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
