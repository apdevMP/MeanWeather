package it.apdev.weathermean.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * @author TEAM apdev
 * 
 * This class contains all the methods that have to manage the DB.
 * Through them, it is possible to insert/delete new records, and to query the system.
 *
 */
public class DBManager
{
	private DBHelper					dbhelper;
	private static final String	TAG	= 	"DBManager";

	/**
	 * Constructor
	 * 
	 * @param ctx the context
	 */
	public DBManager(Context ctx)
	{
		dbhelper = new DBHelper(ctx, DBHelper.DBNAME, null, 1);
	}

	
	/**
	 * Save a new record within the database
	 * 
	 * @param city
	 * @param ccode
	 * @param isCurrent
	 */
	public void save(String city, String ccode, Integer isCurrent)
	{
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		//check if values are not null
		if (city != null && ccode != null && isCurrent != null)
		{
			ContentValues cv = new ContentValues();
			cv.put(DBStrings.FIELD_CITY, city);
			cv.put(DBStrings.FIELD_COUNTRY, ccode);
			cv.put(DBStrings.FIELD_CURRENT, isCurrent);
			try
			{
				db.insert(DBStrings.TBL_NAME, null, cv);
			} catch (SQLiteException sqle)
			{
				sqle.printStackTrace();
			}
		}
	}

	
	/**
	 * Delete a record from the database, based on the city name and the country code
	 * 
	 * @param city
	 * @param country
	 * @return			true if the deletion as been successful
	 */
	public boolean delete(String city, String country)
	{
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		try
		{
			if (db.delete(DBStrings.TBL_NAME, DBStrings.FIELD_CITY + "=?" + " AND " + DBStrings.FIELD_COUNTRY + "=?", new String[] { city, country }) > 0)
				return true;
			return false;
		} catch (SQLiteException sqle)
		{
			return false;
		}

	}

	/**
	 * Query the database for the list of all records.
	 * 
	 * @return a Cursor object with the records in alphabetical order
	 */
	public Cursor getCityList()
	{
		Cursor crs = null;
		try
		{
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			
			//require alphabetical order
			crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, DBStrings.FIELD_CITY, null);
		} catch (SQLiteException sqle)
		{
			return null;
		}
		return crs;
	}

	/**
	 * Verify if the record with the given fields already exists within the DB
	 * 
	 * @param city
	 * @param ccode
	 * @param isCurrent
	 * @return true if the record already exists, false otherwise
	 */
	public boolean isAlreadyPresent(String city, String ccode, Integer isCurrent)
	{

		SQLiteDatabase sqldb = dbhelper.getReadableDatabase();
		Cursor cursor;

		//search for the record based on country name and country code
		String Query = "Select * from " + DBStrings.TBL_NAME + " where " + DBStrings.FIELD_CITY + "=" + "\"" + city + "\"" + " AND "
				+ DBStrings.FIELD_COUNTRY + "=" + "\"" + ccode + "\"";
		cursor = sqldb.rawQuery(Query, null);

		/*
		 * if there are no records with the name and the code required, 
		 * the record is not a duplicate, so return false
		 */
		if (cursor.getCount() <= 0)
		{
			cursor.close();
			return false;
		}
		/*
		 * if there's at least one record with the same name and country code
		 * check the FIELD_CURRENT:
		 * 
		 * if it is 0, update the record and set the field to 1,
		 * because the record we search for is now the current location
		 * 
		 * else it is a duplicate, so return true
		 * 
		 */
		else
		{
			cursor.moveToFirst();
			
			if (isCurrent == 1)
			{

				Log.v(TAG, "updating FIELD_CURRENT for " + city);
				ContentValues cv = new ContentValues();
				cv.put(DBStrings.FIELD_CURRENT, 1);

				sqldb.update(DBStrings.TBL_NAME, cv, DBStrings.FIELD_CITY + "=" + "\"" + city + "\"" + " AND " + DBStrings.FIELD_COUNTRY + "=" + "\""
						+ ccode + "\"", null);
			}
			cursor.close();
			return true;
		}

	}

	/**
	 * Reset all FIELD_CURRENT to 0 if they are set to 1 in order to clear the
	 * current position for the next usage
	 */
	public void updateCurrentField()
	{

		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DBStrings.FIELD_CURRENT, 0);
		db.update(DBStrings.TBL_NAME, cv, DBStrings.FIELD_CURRENT + "=1", null);
	}
}
