package com.android.settings.pizzabean;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class UserInterface extends SettingsPreferenceFragment {
	
	private final static String TAG = "PizzaBean UserInterface";
	private static final String PREF_FORCE_TABLET_UI = "force_tabletui";
    
	private CheckBoxPreference mTabletui;
    private ContentResolver mCr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.pizzabean_userinterface);
		
		PreferenceScreen prefSet = getPreferenceScreen();
		mCr = getContentResolver();
		
		/* Force Tablet UI */
		mTabletui = (CheckBoxPreference) prefSet.findPreference(PREF_FORCE_TABLET_UI);
		mTabletui.setChecked(Settings.System.getInt(mCr,
				Settings.System.FORCE_TABLET_UI, 0) == 1);
	}
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
    		Preference preference) {
    	
    	if (preference == mTabletui) {
    		Settings.System.putInt(mCr, Settings.System.FORCE_TABLET_UI,
    				((CheckBoxPreference) preference).isChecked() ? 1 : 0);
    		return true;
    	}
        
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
