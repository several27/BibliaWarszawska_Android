package pl.several27.Biblia_Warszawska;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maciej on 20/02/14.
 */
public class HistoryActivity extends Activity {
    public ActionBar actionBar;
    public ListView listView;
	public ArrayList<Database.HistoryItem> history;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        actionBar = getActionBar();
        actionBar.setTitle(R.string.history);
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);

	    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	    history = MainActivity.database.getHistory();
	    for(Database.HistoryItem historyItem : history)
	    {
		    Map<String, String> datum = new HashMap<String, String>(2);
			if(historyItem.isSearch())
			{
				datum.put("item", historyItem.getSearchString());
				datum.put("subitem", historyItem.getDate());
			}
		    else
			{
				Log.d("showing", Integer.toString(historyItem.getBook()));
				datum.put("item", MainActivity.database.booksList[historyItem.getBook() - 1] + ": " + Integer.toString(historyItem.getChapter()));
				datum.put("subitem", historyItem.getDate());
			}
		    data.add(datum);
	    }
	    SimpleAdapter adapter = new SimpleAdapter(this, data,
	                                              android.R.layout.simple_list_item_2,
	                                              new String[] {"item", "subitem"},
	                                              new int[] {android.R.id.text1, android.R.id.text2});
	    listView.setAdapter(adapter);

	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
	    {
		    @Override
		    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
		    {
				Database.HistoryItem historyItem = history.get(i);
			    if(historyItem.isSearch())
			    {
				    Intent intent = new Intent(HistoryActivity.this, SearchResultsActivity.class);
				    intent.putExtra(SearchManager.QUERY, historyItem.getSearchString());
				    startActivity(intent);
			    }
			    else
			    {
					MainActivity.book = historyItem.getBook();
				    MainActivity.chapter = historyItem.getChapter();

				    Intent intent = new Intent(HistoryActivity.this, ContentActivity.class);
				    startActivity(intent);
			    }
		    }
	    });
    }
}