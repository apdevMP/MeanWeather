package it.apdev.weathermean.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.nfc.Tag;
import android.util.Log;

public class DBManager
{
	private DBHelper			dbhelper;
	private static final String	TAG	= "DBManager";

	public DBManager(Context ctx)
	{
		dbhelper = new DBHelper(ctx, DBHelper.DBNAME, null, 1);
	}

	public void save(String city, String ccode, Integer isCurrent)
	{
		SQLiteDatabase db = dbhelper.getWritableDatabase();

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

	public Cursor query()
	{
		Cursor crs = null;
		try
		{
			SQLiteDatabase db = dbhelper.getReadableDatabase();
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

		//cerca il record in base al nome e al ccode
		String Query = "Select * from " + DBStrings.TBL_NAME + " where " + DBStrings.FIELD_CITY + "=" + "\"" + city + "\"" + " AND "
				+ DBStrings.FIELD_COUNTRY + "=" + "\"" + ccode + "\"";
		cursor = sqldb.rawQuery(Query, null);

		//se non ci sono record corrispondenti alla ricerca, allora il record corrente non è un duplicato
		if (cursor.getCount() <= 0)
		{
			cursor.close();
			return false;
		} 
		//se invece c'è un record corrispondente alla ricerca, allora controlla il campo current
		else
		{
			//se isCUrrent è 1 allora devo fare un update sul campo isCurrent
			cursor.moveToFirst();
			Log.v(TAG, city+",iscurrent vale="+isCurrent);
			if(isCurrent == 1){
				
				Log.v(TAG, "updating FIELD_CURRENT for "+city);
				ContentValues cv = new ContentValues();
				cv.put(DBStrings.FIELD_CURRENT, 1);

				sqldb.update(DBStrings.TBL_NAME, cv, DBStrings.FIELD_CITY + "=" + "\"" + city + "\"" + " AND " + DBStrings.FIELD_COUNTRY + "=" + "\""
						+ ccode + "\"", null);
			}
			//altrimenti si tratta di un semplice duplicato e non aggiorno nulla
			
			cursor.close();
			return true;
		}

	}

	
	/**
	 * Reset all FIELD_CURRENT to 0 if they are set to 1
	 * in order to clear the current position for the next usage
	 */
	public void updateCurrentField()
	{

		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DBStrings.FIELD_CURRENT, 0);
		db.update(DBStrings.TBL_NAME, cv, DBStrings.FIELD_CURRENT + "=1", null);
	}
}
