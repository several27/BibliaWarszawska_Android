package pl.several27.Biblia_Warszawska;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maciej on 11.01.2014.
 */
public class SearchResultsActivity extends Activity
{
	public ActionBar actionBar;
	public ListView listView;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		actionBar = getActionBar();
		actionBar.setTitle(R.string.search_results);
		actionBar.setDisplayHomeAsUpEnabled(true);

		listView = (ListView) findViewById(R.id.listView);

		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent)
	{
		String query = intent.getStringExtra(SearchManager.QUERY);

		ArrayList<Database.Verse> results = MainActivity.database.search(query);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for(Database.Verse result : results)
		{
			Map<String, String> datum = new HashMap<String, String>(2);
			datum.put("item", MainActivity.database.booksList[result.getBook() - 1] + " " + result.getChapter() + ":" + result.getVerse());
			datum.put("subitem", result.getContent());
			data.add(datum);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"item", "subitem"},
				                                  new int[] {android.R.id.text1, android.R.id.text2});
		listView.setAdapter(adapter);

		actionBar.setTitle(getString(R.string.search_results) + ": " + results.size());

		MainActivity.database.addHistoryItem(query);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		switch(menuItem.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(menuItem);
	}
}