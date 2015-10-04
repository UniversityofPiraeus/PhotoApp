/**
 * 
 */
package gr.unipi.photoapp.db.model;

import gr.unipi.photoapp.sensor.Gyroscope;
import gr.unipi.photoapp.services.gps.GeoLocation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * @author gperreas
 *
 */
public class Photo
	implements Serializable
{

	private static final long serialVersionUID = -1333124540647749402L;

	public static final String KEY_INTENT = "photo";
	
	private String id;
	
	private Date timestamp;
	
	private String name;

	private Float azimuth;
	
	private Gyroscope gyroscope;
	
	private GeoLocation location = new GeoLocation();
	
	public Photo(){}
	
	public Photo(String id, Date timestamp, String name, Float azimuth, 
			Float gyroscope_x, Float gyroscope_y, Float gyroscope_z,
			Double latitude, Double longitude){
		this.id = id;
		this.timestamp = timestamp;
		this.name = name;
		this.azimuth = azimuth;
		this.gyroscope = new Gyroscope(gyroscope_x,gyroscope_y,gyroscope_z);
		this.location = new GeoLocation(latitude, longitude);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the azimuth
	 */
	public Float getAzimuth() {
		return azimuth;
	}

	/**
	 * @param azimuth the heading to set
	 */
	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}
	
	/**
	 * @return the gyroscope
	 */
	public Gyroscope getGyroscope() {
		return gyroscope;
	}

	/**
	 * @param gyroscope the gyroscope to set
	 */
	public void setGyroscope(Gyroscope gyroscope) {
		this.gyroscope = gyroscope;
	}

	/**
	 * @return the location
	 */
	public GeoLocation getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(GeoLocation location) {
		this.location = location;
	}

	/* Inner class that defines the table contents */
    public static abstract class PhotoEntry 
    	implements BaseColumns
    {
        public static final String TABLE_NAME = "Photo";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AZIMUTH = "azimuth";
        public static final String COLUMN_GYROSCOPE_X = "gs_x";
        public static final String COLUMN_GYROSCOPE_Y = "gs_y";
        public static final String COLUMN_GYROSCOPE_Z = "gs_z";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        //public static final String COLUMN_LOCATION = "location";
        private static String[] projection = {
        	_ID,
        	COLUMN_TIMESTAMP,
        	COLUMN_NAME,
        	COLUMN_AZIMUTH,
        	COLUMN_GYROSCOPE_X,
        	COLUMN_GYROSCOPE_Y,
        	COLUMN_GYROSCOPE_Z,
        	COLUMN_LATITUDE,
        	COLUMN_LONGITUDE
        };
        
        public static String[] getDefaultProjection(){
        	return projection;
        }
    }

    private transient ContentValues values = new ContentValues();
    
    public ContentValues getContentValues()
    {	
    	values.put(PhotoEntry._ID, this.id);
    	values.put(PhotoEntry.COLUMN_TIMESTAMP, this.timestamp.getTime());
    	values.put(PhotoEntry.COLUMN_NAME, this.name);
    	values.put(PhotoEntry.COLUMN_AZIMUTH, this.azimuth);
    	values.put(PhotoEntry.COLUMN_GYROSCOPE_X, this.gyroscope.getX());
    	values.put(PhotoEntry.COLUMN_GYROSCOPE_Y, this.gyroscope.getY());
    	values.put(PhotoEntry.COLUMN_GYROSCOPE_Z, this.gyroscope.getZ());
    	values.put(PhotoEntry.COLUMN_LATITUDE, this.location.getLatitude());
    	values.put(PhotoEntry.COLUMN_LONGITUDE, this.location.getLongitude());
    	
    	return this.values;
    }

    
	@Override
	public String toString() {
		return "Photo [id=" + id + ", timestamp=" + timestamp + ", name="
				+ name + ", azimuth=" + azimuth + ", gyroscope=" + gyroscope
				+ ", location=" + location + "]";
	}

}
