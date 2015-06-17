package it.apdev.weathermean.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DBManager
{
	private DBHelper	dbhelper;

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
			if (db.delete(DBStrings.TBL_NAME, DBStrings.FIELD_CITY + "=?"+" AND "+ DBStrings.FIELD_COUNTRY + "=?", new String[] { city, country }) > 0)
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
			crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
		} catch (SQLiteException sqle)
		{
			return null;
		}
		return crs;
	}

	/**
	 * Verify if the record with the given fields already exists within the DB
	 * 
	 * @param cityName
	 * @param countryCode
	 * @return true if the record already exists, false otherwise
	 */
	public boolean isDuplicate(String cityName, String countryCode, Integer isCurrent)
	{
		SQLiteDatabase sqldb = dbhelper.getReadableDatabase();
		Cursor cursor;
		//se si tratta della posizione corrente, e il record è già presente, aggiorna il campo e ritorna true
		if (isCurrent == 1)
		{
			//String Query = "Select * from " + DBStrings.TBL_NAME + " where " + DBStrings.FIELD_CITY + "=" + "\"" + cityName + "\"" + " AND "
			//		+ DBStrings.FIELD_COUNTRY + "=" + "\"" + countryCode + "\"" + " AND " + DBStrings.FIELD_CURRENT + "=" + "\"" + isCurrent + "\"";
			//cursor = sqldb.rawQuery(Query, null);
			ContentValues cv = new ContentValues();
			cv.put(DBStrings.FIELD_CURRENT, 1);
			
			sqldb.update(DBStrings.TBL_NAME, cv, DBStrings.FIELD_CITY +"="+"\""+cityName+"\""+" AND "+ DBStrings.FIELD_COUNTRY+"="+"\""+countryCode+"\"", null);
			
			return true;
			
		} else
		{
			//se invece si tratta di un record qualsiasi, non inserirlo se già presente
			String Query = "Select * from " + DBStrings.TBL_NAME + " where " + DBStrings.FIELD_CITY + "=" + "\"" + cityName + "\"" + " AND "
					+ DBStrings.FIELD_COUNTRY + "=" + "\"" + countryCode + "\"" + " AND " + DBStrings.FIELD_CURRENT + "=" + "\"" + isCurrent + "\"";
			cursor = sqldb.rawQuery(Query, null);
			
			if (cursor.getCount() <= 0)
			{
				cursor.close();
				return false;
			}
			cursor.close();
			return true;
		}

		
	}

	public void updateCurrentField()
	{

		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DBStrings.FIELD_CURRENT, 0);
		db.update(DBStrings.TBL_NAME, cv, DBStrings.FIELD_CURRENT + "=1", null);
	}
}
