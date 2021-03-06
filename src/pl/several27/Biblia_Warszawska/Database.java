package pl.several27.Biblia_Warszawska;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Database extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Bible";
	private Context context;

	public Database(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		try
		{
			InputStream inputStream = this.context.getAssets().open("bible.sql");
			execSqlFile(database, inputStream);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		database.execSQL("DROP TABLE IF EXISTS bible");
		this.onCreate(database);
	}

	private static final String TABLE_BIBLE = "bible";

	private static final String KEY_ID = "id";
	private static final String KEY_BOOK = "book";
	private static final String KEY_CHAPTER = "chapter";
	private static final String KEY_VERSE = "verse";
	private static final String KEY_CONTENT = "content";

	private static final String[] COLUMNS = {KEY_ID, KEY_BOOK, KEY_CHAPTER, KEY_VERSE, KEY_CONTENT};

	public String[] booksList = {"Ks. Rodzaju", "Ks. Wyjścia", "Ks. Kapłańska", "Ks. Liczb", "Ks. Powt. Prawa", "Ks. Jozuego", "Ks. Sędziów", "Ks. Rut", "1 Ks. Samuela", "2 Ks. Samuela", "1 Ks. Królewska", "2 Ks. Królewska", "1 Ks. Kronik", "2 Ks. Kronik", "Ks. Ezdrasza", "Ks. Nehemiasza", "Ks. Estery", "Ks. Hioba", "Ks. Psalmów", "Ks. Przysłów", "Ks. Koheleta", "Pieśń nad pieśniami", "Ks. Izajasza", "Ks. Jeremiasza", "Lamentacje Jeremiasza", "Ks. Ezechiela", "Ks. Daniela", "Ks. Ozeasza", "Ks. Joela", "Ks. Amosa", "Ks. Abdiasza", "Ks. Jonasza", "Ks. Micheasza", "Ks. Nahuma", "Ks. Habakuka", "Ks. Sofoniasza", "Ks. Aggeusza", "Ks. Zachariasza", "Ks. Malachiasza", "Ew. Mateusza", "Ew. Marka", "Ew. Łukasza", "Ew. Jana", "Dzieje Apost.", "List do Rzymian", "1 list do Koryntian", "2 list do Koryntian", "List do Galatów", "List do Efezjan", "List do Filipian", "List do Kolosan", "1 List do Tesaloniczan", "2 List do Tesaloniczan", "1 List do Tymoteusza", "2 List do Tymoteusza", "List do Tytusa", "List do Filemona", "List do Hebrajczyków", "List Jakuba", "1 List Piotra", "2 List Piotra", "1 List Jana", "2 List Jana", "3 List Jana", "List Judy", "Apokalipsa (Objawienie)"};

	public String getVerse(int id)
	{
		return "Not implemented yet ... ";
	}

	public ArrayList<String> getAllVerses(int book, int chapter)
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT id, book, chapter, verse, content FROM bible WHERE book = ? AND chapter = ?", new String[] {String.valueOf(book), String.valueOf(chapter)});

		ArrayList<String> verses = new ArrayList<String>();
		if(cursor.moveToFirst())
		{
			do
			{
				verses.add(cursor.getString(4));
			} while(cursor.moveToNext());
		}

		Log.d("getAllVerses()", verses.toString());
		return verses;
	}

	public int countVerses(int book, int chapter)
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM bible WHERE book = ? AND chapter = ?", new String[] {String.valueOf(book), String.valueOf(chapter)});

		cursor.moveToFirst();
		int verses = cursor.getInt(0);
		cursor.close();

		Log.d("countVerses()", String.valueOf(verses));
		return verses;
	}

	public int countChapters(int book)
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT COUNT(DISTINCT chapter) FROM bible WHERE book = ?", new String[] {String.valueOf(book)});

		cursor.moveToFirst();
		int chapters = cursor.getInt(0);
		cursor.close();

		Log.d("countChapters()", String.valueOf(chapters));

		return chapters;
	}

	public ArrayList<Verse> search(String query)
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT id, book, chapter, verse, content FROM bible WHERE content LIKE ?", new String[] {"%" + query + "%"});

		ArrayList<Verse> results = new ArrayList<Verse>();
		if(cursor.moveToFirst())
		{
			do
			{
				results.add(new Verse(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));
			} while(cursor.moveToNext());
		}

		return results;
	}

	public static class Verse
	{
		public int book;
		public int chapter;
		public int verse;
		public String content;

		public Verse(int book, int chapter, int verse, String content)
		{
			this.book = book;
			this.chapter = chapter;
			this.verse = verse;
			this.content = content;
		}

		public int getBook()
		{
			return book;
		}

		public int getChapter()
		{
			return chapter;
		}

		public int getVerse()
		{
			return verse;
		}

		public String getContent()
		{
			return content;
		}
	}

    public static class HistoryItem
    {
        public int id;
        public boolean search;
        public String searchString;
        public Verse verse;
	    public String date;

        public HistoryItem(String searchString, int id, String date)
        {
            this.searchString = searchString;
            this.search = true;
            this.id = id;
	        this.date = date;
        }

        public HistoryItem(Verse verse, int id, String date)
        {
            this.verse = verse;
            this.search = false;
            this.id = id;
	        this.date = date;
        }

        public int getBook()
        {
            return verse.getBook();
        }

        public int getChapter()
        {
            return verse.getChapter();
        }

        public int getVerse()
        {
            return verse.getVerse();
        }

	    public String getDate()
	    {
		    return this.date;
	    }

        public boolean isSearch()
        {
            return search;
        }

	    public String getSearchString()
	    {
		    return this.searchString;
	    }
    }

	public HistoryItem getLastHistoryString()
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT id, search, string, book, chapter, verse, date FROM history WHERE ID = (SELECT MAX(id) FROM history) AND search = 1", new String[]{});

		HistoryItem historyItem = new HistoryItem("", 0, "");
		if(cursor.moveToFirst())
		{
			do
			{
				historyItem = new HistoryItem(cursor.getString(2), cursor.getInt(0), cursor.getString(6));
			} while(cursor.moveToNext());
		}

		return historyItem;
	}

	public HistoryItem getLastHistoryVerse()
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT id, search, string, book, chapter, verse, date FROM history WHERE ID = (SELECT MAX(id) FROM history) AND search = 0", new String[]{});

		HistoryItem historyItem = new HistoryItem(new Verse(0, 0, 0, ""), 0, "");
		if(cursor.moveToFirst())
		{
			do
			{
				historyItem = new HistoryItem(new Verse(cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), ""), cursor.getInt(0), cursor.getString(6));
			} while(cursor.moveToNext());
		}

		return historyItem;
	}

	public HistoryItem addHistoryItem(String searchString)
	{
		if(!getLastHistoryString().getSearchString().equals(searchString))
		{
			SQLiteDatabase database = this.getReadableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("search", 1);
			contentValues.put("string", searchString);
			contentValues.put("date", (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")).format(new Date()));
			int id = (int) Math.round(database.insert("history", null, contentValues));

			return new HistoryItem(searchString, id, (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")).format(new Date()));
		}
		else
		{
			return new HistoryItem(searchString, 0, "");
		}
	}

	public HistoryItem addHistoryItem(Verse verse)
	{
		HistoryItem historyItem = getLastHistoryVerse();
		if(historyItem.verse.getChapter() != verse.getChapter() || historyItem.verse.getBook() != verse.getBook())
		{
			SQLiteDatabase database = this.getReadableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("search", 0);
			contentValues.put("book", verse.getBook());
			contentValues.put("chapter", verse.getChapter());
			contentValues.put("verse", verse.getVerse());
			contentValues.put("date", (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")).format(new Date()));
			int id = (int) Math.round(database.insert("history", null, contentValues));

			return new HistoryItem(new Verse(verse.getBook(), verse.getChapter(), verse.getVerse(), ""), id, (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")).format(new Date()));
		}
		else
		{
			return new HistoryItem(verse, 0, "");
		}
	}

	public ArrayList<HistoryItem> getHistory()
	{
		SQLiteDatabase database = this.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT id, search, string, book, chapter, verse, date FROM history ORDER BY id DESC", new String[]{});

		ArrayList<HistoryItem> history = new ArrayList<HistoryItem>();
		if(cursor.moveToFirst())
		{
			do
			{
				if(cursor.getInt(1) == 1)
					history.add(new HistoryItem(cursor.getString(2), cursor.getInt(0), cursor.getString(6)));
				else
					history.add(new HistoryItem(new Verse(cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), ""), cursor.getInt(0), cursor.getString(6)));

			} while(cursor.moveToNext());
		}

		return history;
	}

	private void execSqlFile(SQLiteDatabase database, InputStream inputStream)
			throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		while(bufferedReader.ready())
		{
			database.execSQL(bufferedReader.readLine());
		}
		bufferedReader.close();
	}

}
