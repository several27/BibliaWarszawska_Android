package pl.several27.Biblia_Warszawska;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Maciej on 06.01.2014.
 */
public class ChapterActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getActionBar().setTitle(MainActivity.database.booksList[MainActivity.book - 1]);

		ListView listView = (ListView) findViewById(R.id.listView);

		ArrayList<String> chaptersList = new ArrayList<String>();
		int chapters = MainActivity.database.countChapters(MainActivity.book);
		for(int chapter = 1; chapter <= chapters; chapter++)
		{
			chaptersList.add(String.valueOf(chapter));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChapterActivity.this, android.R.layout.simple_list_item_1, chaptersList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				MainActivity.chapter = i + 1;
				Log.d("chapter choosen", String.valueOf(MainActivity.chapter));

				Intent intent = new Intent(ChapterActivity.this, ContentActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.action_settings:
				Intent intent = new Intent(ChapterActivity.this, SettingsActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}