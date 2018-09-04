package id.iip.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Preference connectionPref = findPreference(key);
        // Set summary to be the user-description for the selected value
        connectionPref.setSummary(sharedPreferences.getString(key, ""));

        if (connectionPref instanceof CheckBoxPreference) {
            boolean isChecked = ((CheckBoxPreference) connectionPref).isChecked();
            switch (key) {
                case "author":
                    sharedPreferences.edit().putBoolean(getString(R.string.author_key), isChecked).commit();
                    break;
                case "name":
                    sharedPreferences.edit().putBoolean(getString(R.string.name_key), isChecked).commit();
                    break;
                case "title":
                    sharedPreferences.edit().putBoolean(getString(R.string.title_key), isChecked).commit();
                    break;
                case "publication_date":
                    sharedPreferences.edit().putBoolean(getString(R.string.publication_date_key), isChecked).commit();
                    break;
            }
        } else if (connectionPref instanceof EditTextPreference){
            String query = ((EditTextPreference)connectionPref).getText().toString();
            sharedPreferences.edit().putString(getString(R.string.query_key), query).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
