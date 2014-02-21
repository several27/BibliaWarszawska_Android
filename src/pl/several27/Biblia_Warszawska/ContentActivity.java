package pl.several27.Biblia_Warszawska;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Maciej on 06.01.2014.
 */
public class ContentActivity extends Activity
{
    public ScrollView scrollView;
    public int line = 0;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(MainActivity.database.booksList[MainActivity.book - 1] + ": " + MainActivity.chapter);
		actionBar.setDisplayHomeAsUpEnabled(true);

		String text = "";
		int count = 1;
		for(String verse: MainActivity.database.getAllVerses(MainActivity.book, MainActivity.chapter))
		{
			text += count + "." + verse + "\n";
			count++;
		}

		TextView textView = (TextView) findViewById(R.id.textView);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		textView.setTextSize(Float.parseFloat(sharedPreferences.getString("settings_font_size", "15")));
		textView.setText(text);

		MainActivity.database.addHistoryItem(new Database.Verse(MainActivity.book, MainActivity.chapter, 0, ""));

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public Runnable onScrollStoppedListener;
            public int initialPosition;

            public void onScrollStopped() {
                line = scrollView.getScrollY();
                Log.d("onTouch", String.valueOf(line));
            }

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                initialPosition = scrollView.getScrollY();
                onScrollStoppedListener = new Runnable() {
                    @Override
                    public void run() {
                        int newPosition = scrollView.getScrollY();
                        if (initialPosition - newPosition == 0) {
                            onScrollStopped();
                        } else {
                            initialPosition = scrollView.getScrollY();
                            scrollView.postDelayed(onScrollStoppedListener, 100);
                        }
                    }
                };
                scrollView.postDelayed(onScrollStoppedListener, 100);
                return false;
            }
        });
	}

    @Override
    protected void onPause()
    {
        super.onPause();
        
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("line", line);
        editor.commit();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        line = settings.getInt("line", 0);
        Log.d("event", String.valueOf(line));
        scrollView.postDelayed(new Runnable(){
            @Override
            public void run() {
                scrollView.scrollTo(0, line);
            }
        }, 30);
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