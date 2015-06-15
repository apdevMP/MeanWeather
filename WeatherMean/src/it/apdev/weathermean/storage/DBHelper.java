/**
 * 
 */
package it.apdev.weathermean.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Vanessa
 *
 */
public class DBHelper extends SQLiteOpenHelper
{

	public static final String	DBNAME			= "CITIES_DB";


	public DBHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub

		String q = "CREATE TABLE " + DBStrings.TBL_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," + DBStrings.FIELD_CITY + " TEXT UNIQUE," + DBStrings.FIELD_COUNTRY + " TEXT," + DBStrings.FIELD_CURRENT+ " INTEGER)";
		db.execSQL(q);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}
