/**
 * 
 */
package gr.unipi.photoapp.db.controller;

import gr.unipi.photoapp.db.model.Photo.PhotoEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper used to create/upgrade 
 * the SQLite database and get an instance of database
 * http://developer.android.com/training/basics/data-storage/databases.html
 * @author gperreas
 * 
 */
public class DatabaseHelper
	extends SQLiteOpenHelper
{
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PhotoApp.db";
    
    
    public static final String DOUBLE_TYPE = " REAL";
    public static final String FLOAT_TYPE = " FLOAT";

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String EQUAL = "=";
    
    public static final String SELECT_ALL = "*";
    
    public static final String DESC = " DESC";
    public static final String ASC = " ASC";
    
    private static final String SQL_CREATE_TABLES =
        "CREATE TABLE " + PhotoEntry.TABLE_NAME + " (" +
        PhotoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        PhotoEntry.COLUMN_TIMESTAMP + TEXT_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_AZIMUTH + FLOAT_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_GYROSCOPE_X + FLOAT_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_GYROSCOPE_Y + FLOAT_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_GYROSCOPE_Z + FLOAT_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
        PhotoEntry.COLUMN_LONGITUDE + DOUBLE_TYPE  +
        " )";

    private static final String SQL_DELETE_TABLES =
        "DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME;

	/**
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SQL_CREATE_TABLES);
		
		Log.i(TAG, "DATABASE " + DATABASE_NAME + " CREATED");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
		db.execSQL(SQL_DELETE_TABLES);
        onCreate(db);
        
        Log.i(TAG, "DATABASE " + DATABASE_NAME + " UPGRADED");
	}

	
}
