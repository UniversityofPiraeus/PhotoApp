/**
 * 
 */
package gr.unipi.photoapp.db.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import gr.unipi.photoapp.db.model.Photo;
import gr.unipi.photoapp.db.model.Photo.PhotoEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Database Controller which it used from app to read/write to SQLite
 * @author gperreas
 * 
 */
public class DatabaseController
	implements IDatabaseController
{
	private static final String TAG = DatabaseController.class.getSimpleName();
	
	// Gets the data repository in write mode
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	/**
	 * 
	 */
	public DatabaseController(Context app_context) {
		 this.dbHelper = new DatabaseHelper(app_context);
	}

	//TODO getPhoto
	@Override
	public Photo getPhoto(String name) {

		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = db.query(
				PhotoEntry.TABLE_NAME, 
				PhotoEntry.getDefaultProjection(), 
				null,
				null,null,
				null,null,null);
		
		return null;
	}

	
	@Override
	public void insertPhoto(Photo photo) {

		if(photo==null)
			return;
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();	
		ContentValues values = photo.getContentValues();
			
		db.insert(PhotoEntry.TABLE_NAME, null, values);
		
		db.close();
		
		Log.i(TAG, "photo saved " + photo.getTimestamp());
	}

	
	@Override
	public void updatePhoto(Photo photo) {

		
	}
	
	@Override
	public void deletePhoto(String id) {
		
		if(id==null || id.equals(""))
			return;
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();	
			
		db.delete(PhotoEntry.TABLE_NAME, PhotoEntry._ID+DatabaseHelper.EQUAL+id, null);
		
		db.close();
		
		Log.i(TAG, "photo deleted with id " + id);
	}
	
	@Override
	public List<Photo> getAllPhotos() {

		List<Photo> results = null;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor c = db.query(
				PhotoEntry.TABLE_NAME, 
				PhotoEntry.getDefaultProjection(), 
				null,
				null,null,
				null,PhotoEntry.COLUMN_TIMESTAMP+DatabaseHelper.DESC,null);

		if(c.moveToFirst()) 
		{		
			results = new ArrayList<Photo>();
			
			while(c.moveToNext()){
            	
				Photo photo = new Photo(
	            			c.getString(c.getColumnIndex(PhotoEntry._ID)),
	            			new Date(c.getLong(c.getColumnIndex(PhotoEntry.COLUMN_TIMESTAMP))),
	            			c.getString(c.getColumnIndex(PhotoEntry.COLUMN_NAME)),
	            			c.getFloat(c.getColumnIndex(PhotoEntry.COLUMN_AZIMUTH)),
	            			c.getFloat(c.getColumnIndex(PhotoEntry.COLUMN_GYROSCOPE_X)),
	            			c.getFloat(c.getColumnIndex(PhotoEntry.COLUMN_GYROSCOPE_Y)),
	            			c.getFloat(c.getColumnIndex(PhotoEntry.COLUMN_GYROSCOPE_Z)),
	            			c.getDouble(c.getColumnIndex(PhotoEntry.COLUMN_LATITUDE)),
	            			c.getDouble(c.getColumnIndex(PhotoEntry.COLUMN_LONGITUDE))
						);
                 
				results.add(photo);
            }
        }
		
        c.close();
        db.close();
		
		return results;
	}
	
	
}
