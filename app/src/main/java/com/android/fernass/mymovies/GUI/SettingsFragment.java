package com.android.fernass.mymovies.GUI;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.android.fernass.mymovies.R;

/**
 * Created by ferna on 02.03.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
