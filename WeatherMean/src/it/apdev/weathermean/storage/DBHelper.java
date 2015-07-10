/**
 * 
 */
package it.apdev.weathermean.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author TEAM apdev
 * 
 * This class creates the DB if not exists.
 * 
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


	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String q = "CREATE TABLE if not exists " + DBStrings.TBL_NAME + " ( _id INTEGER," + DBStrings.FIELD_CITY + " TEXT," + DBStrings.FIELD_COUNTRY + " TEXT," + DBStrings.FIELD_CURRENT+ " INTEGER," +" PRIMARY KEY ("+DBStrings.FIELD_CITY+", "+ DBStrings.FIELD_COUNTRY+"))";
		db.execSQL(q);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// do nothing
	}

}
