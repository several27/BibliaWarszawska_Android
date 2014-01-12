package pl.several27.Biblia_Warszawska;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
		textView.setTextSize(Float.parseFloat(sharedPreferences.getString("settings_font_sizes", "15")));
		textView.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.content_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		Intent intent = getIntent();
		switch(menuItem.getItemId())
		{
			case R.id.action_back:
				if(MainActivity.chapter > 1)
				{
					MainActivity.chapter--;
				}
				else if(MainActivity.book > 1)
				{
					MainActivity.book--;
					MainActivity.chapter = MainActivity.database.countChapters(MainActivity.book);
				}
				finish();
				startActivity(intent);
				return true;
			case R.id.action_forward:
				if(MainActivity.chapter < MainActivity.database.countChapters(MainActivity.book))
				{
					MainActivity.chapter++;
				}
				else if(MainActivity.book < MainActivity.database.booksList.length)
				{
					MainActivity.book++;
					MainActivity.chapter = 1;
				}
				finish();
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
}