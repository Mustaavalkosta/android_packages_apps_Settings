package com.android.settings.pizzabean;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import android.util.Log;
import java.util.ArrayList;

import static android.provider.Settings.System.KEYLIGHT_TIMEOUT;
import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

public class HardwareKeys extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "PizzaBean HardwareKeys";

    /** If there is no setting in the provider, use this. */
    private static final int FALLBACK_KEYLIGHT_TIMEOUT_VALUE = 6000;

    private static final String PREF_KEYLIGHT_TIMEOUT = "keylight_timeout";

    private ListPreference mKeylightTimeoutPreference;

    private PreferenceScreen mPrefSet;
    private ContentResolver mCr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.pizzabean_hardware_keys);

		mPrefSet = getPreferenceScreen();
		mCr = getContentResolver();

        /** Button backlight timeout */
        mKeylightTimeoutPreference = (ListPreference) findPreference(PREF_KEYLIGHT_TIMEOUT);
        final long currentTimeout = Settings.System.getLong(mCr, KEYLIGHT_TIMEOUT,
                FALLBACK_KEYLIGHT_TIMEOUT_VALUE);
        mKeylightTimeoutPreference.setValue(String.valueOf(currentTimeout));
        mKeylightTimeoutPreference.setOnPreferenceChangeListener(this);
        disableUnusableTimeouts(mKeylightTimeoutPreference);
        updateTimeoutPreferenceDescription(currentTimeout);
	}

    private void updateTimeoutPreferenceDescription(long currentTimeout) {
        ListPreference preference = mKeylightTimeoutPreference;
        String summary;
        if (currentTimeout < 0) {
            // Unsupported value
            summary = "";
        } else {
            final CharSequence[] entries = preference.getEntries();
            final CharSequence[] values = preference.getEntryValues();
            int best = 0;
            for (int i = 0; i < values.length; i++) {
                long timeout = Long.parseLong(values[i].toString());
                if (currentTimeout >= timeout) {
                    best = i;
                }
            }
            summary = preference.getContext().getString(R.string.pb_keylight_timeout_summary,
                    entries[best]);
        }
        preference.setSummary(summary);
    }

    private void disableUnusableTimeouts(ListPreference keylightTimeoutPreference) {
        long maxTimeout = Settings.System.getLong(mCr, SCREEN_OFF_TIMEOUT, 30000);
        final CharSequence[] entries = keylightTimeoutPreference.getEntries();
        final CharSequence[] values = keylightTimeoutPreference.getEntryValues();
        ArrayList<CharSequence> revisedEntries = new ArrayList<CharSequence>();
        ArrayList<CharSequence> revisedValues = new ArrayList<CharSequence>();
        for (int i = 0; i < values.length; i++) {
            long timeout = Long.parseLong(values[i].toString());
            if (timeout <= maxTimeout) {
                revisedEntries.add(entries[i]);
                revisedValues.add(values[i]);
            }
        }
        if (revisedEntries.size() != entries.length || revisedValues.size() != values.length) {
            keylightTimeoutPreference.setEntries(
                    revisedEntries.toArray(new CharSequence[revisedEntries.size()]));
            keylightTimeoutPreference.setEntryValues(
                    revisedValues.toArray(new CharSequence[revisedValues.size()]));
            final int userPreference = Integer.parseInt(keylightTimeoutPreference.getValue());
            if (userPreference <= maxTimeout) {
                keylightTimeoutPreference.setValue(String.valueOf(userPreference));
            } else {
                // There will be no highlighted selection since nothing in the list matches
                // maxTimeout. The user can still select anything less than maxTimeout.
                // TODO: maybe append maxTimeout to the list and mark selected.
            }
        }
        keylightTimeoutPreference.setEnabled(revisedEntries.size() > 0);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        if (KEY_KEYLIGHT_TIMEOUT.equals(key)) {
            int value = Integer.parseInt((String) objValue);
            try {
                Settings.System.putInt(getContentResolver(), KEYLIGHT_TIMEOUT, value);
                updateTimeoutPreferenceDescription(value);
            } catch (NumberFormatException e) {
                Log.e(TAG, "could not persist keylight timeout setting", e);
            }
            return true;
        }
        return false;
    }
}
