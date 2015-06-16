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

	public boolean delete(long id)
	{
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		try
		{
			if (db.delete(DBStrings.TBL_NAME, DBStrings.FIELD_ID + "=?", new String[] { Long.toString(id) }) > 0)
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

	public boolean isDuplicate(String cityName, String countryCode)
	{
		SQLiteDatabase sqldb = dbhelper.getReadableDatabase();
		String Query = "Select * from " + DBStrings.TBL_NAME + " where " + DBStrings.FIELD_CITY + "=" +"\""+ cityName+"\"" + " AND " + DBStrings.FIELD_COUNTRY + "=" +"\""+countryCode+"\"";
		Cursor cursor = sqldb.rawQuery(Query, null);
		
		//Cursor cursor = db.query("sku_table", columns, "owner=? and price=?", new String[] { owner, price }, null, null, null);
		if (cursor.getCount() <= 0)
		{
			cursor.close();
			return false;
		}
		cursor.close();
		return true;
	}
}
