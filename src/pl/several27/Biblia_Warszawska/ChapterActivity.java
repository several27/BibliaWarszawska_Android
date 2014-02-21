package pl.several27.Biblia_Warszawska;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Maciej on 06.01.2014.
 */
public class ChapterActivity extends Activity
{
	ListView listView;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(MainActivity.database.booksList[MainActivity.book - 1]);
		actionBar.setDisplayHomeAsUpEnabled(true);


		listView = (ListView) findViewById(R.id.listView);

		if(!MainActivity.asyncTask)
		{
			new FirstLoadAsyncTask().execute();
			MainActivity.asyncTask = true;
		}
		else
		{
			setChapters(MainActivity.database.countChapters(MainActivity.book));
		}

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

	public void setChapters(int chapters)
	{
		ArrayList<String> chaptersList = new ArrayList<String>();
		for(int chapter = 1; chapter <= chapters; chapter++)
		{
			chaptersList.add(String.valueOf(chapter));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChapterActivity.this, android.R.layout.simple_list_item_1, chaptersList);
		listView.setAdapter(adapter);
	}

	public class FirstLoadAsyncTask extends AsyncTask<Integer, Void, Integer>
	{
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					progressDialog = ProgressDialog.show(ChapterActivity.this, getResources().getString(R.string.sql_execute_dialog_title), getResources().getString(R.string.sql_execute_dialog_message));
				}
			});
		}

		@Override
		protected Integer doInBackground(Integer... params)
		{
			int chapters = MainActivity.database.countChapters(MainActivity.book);
			return chapters;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			progressDialog.dismiss();
			setChapters(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_activity_actions, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.action_settings:
				startActivity(new Intent(ChapterActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_history:
				startActivity(new Intent(ChapterActivity.this, HistoryActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}