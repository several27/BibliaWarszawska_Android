package pl.several27.Biblia_Warszawska;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Maciej on 06.01.2014.
 */
public class ContentActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		getActionBar().setTitle(MainActivity.database.booksList[MainActivity.book - 1] + ": " + MainActivity.chapter);

		String text = "";
		int count = 1;
		for(String verse: MainActivity.database.getAllVerses(MainActivity.book, MainActivity.chapter))
		{
			text += count + "." + verse + "\n";
			count++;
		}

		TextView textView = (TextView) findViewById(R.id.textView);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		textView.setTextSize(Float.parseFloat(sharedPreferences.getString("settings_fonts", "15")));
		Log.d("settings_font", sharedPreferences.getString("settings_fonts", "NULL"));
		textView.setText(text);
	}
}