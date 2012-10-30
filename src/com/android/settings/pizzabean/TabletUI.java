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
	
	private final static String TAG = "PizzaBean TabletUI";
	private static final String PREF_FORCE_TABLET_UI = "force_tabletui";
    
	private CheckBoxPreference mTabletui;
	private Preference mLcdDensity;
	private int newDensityValue;
    private ContentResolver mCr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.pizzabean_tabletui);
		
		PreferenceScreen prefSet = getPreferenceScreen();
		mCr = getContentResolver();
		
		/* Force Tablet UI */
		mTabletui = (CheckBoxPreference) prefSet.findPreference(PREF_FORCE_TABLET_UI);
		mTabletui.setChecked(Settings.System.getInt(mCr,
				Settings.System.FORCE_TABLET_UI, 0) == 1);
		
		/* Density Changer */
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
    	
    	if (preference == mTabletui) {
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
