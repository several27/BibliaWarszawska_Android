package pl.several27.Biblia_Warszawska;

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

public class MainActivity extends Activity
{
    public static final String PREFS_NAME = "BIBLIA_WARSZAWSKA_PREFS_FILE";
	public static Database database;

	public static int book;
	public static int chapter;

	public static boolean asyncTask = false;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		database = new Database(getApplicationContext());

		ListView listView = (ListView) findViewById(R.id.listView);

		ArrayList<String> booksList = new ArrayList<String>();
		for(String book : database.booksList)
		{
			booksList.add(book);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, booksList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				MainActivity.book = i + 1;
				Log.d("book choosen", String.valueOf(MainActivity.book));

				Intent intent = new Intent(MainActivity.this, ChapterActivity.class);
				startActivity(intent);
			}
		});
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
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_history:
				if(!MainActivity.asyncTask)
				{
					new FirstLoadAsyncTask().execute();
					MainActivity.asyncTask = true;
				}
				startActivity(new Intent(MainActivity.this, HistoryActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
					progressDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.sql_execute_dialog_title), getResources().getString(R.string.sql_execute_dialog_message));
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
		}
	}
}
