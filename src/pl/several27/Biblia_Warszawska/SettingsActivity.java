package pl.several27.Biblia_Warszawska;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Maciej on 06.01.2014.
 */
public class SettingsActivity extends PreferenceActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		getActionBar().setTitle(R.string.action_settings);
	}
}