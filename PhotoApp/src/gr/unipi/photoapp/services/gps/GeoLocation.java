/**
 * 
 */
package gr.unipi.photoapp.services.gps;

import java.io.Serializable;

import android.provider.BaseColumns;

/**
 * @author gperreas
 *
 */
public class GeoLocation
	implements Serializable
{
	private static final long serialVersionUID = 6953910933417407514L;
	
	public  static final double MAX_LATITUDE            = 90.0;
    public  static final double MIN_LATITUDE            = -90.0;
	
    public  static final double MAX_LONGITUDE           = 180.0;
    public  static final double MIN_LONGITUDE           = -180.0;
    
	private double latitude = 0.0;	
	private double longitude = 0.0;

	public GeoLocation() {}

	public GeoLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return this.longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
     *** Returns true if the latitude/longitude contained by the GeoLocation is valid
     *** @return True if the latitude/longitude contained by the GeoLocation is valid
     **/
    public boolean isValid()
    {
        return GeoLocation.isValid(this.getLatitude(), this.getLongitude());
    }
	
	/**
     *** Returns true if the specified latitude/longitude are valid, false otherwise
     *** @param lat  The latitude
     *** @param lon  The longitude
     *** @return True if the specified latitude/longitude are valid, false otherwise
     **/
    public static boolean isValid(double lat, double lon)
    {
        double latAbs = Math.abs(lat);
        double lonAbs = Math.abs(lon);
        if (latAbs >= MAX_LATITUDE) {
            // invalid latitude
            return false;
        } else
        if (lonAbs >= MAX_LONGITUDE) {
            // invalid longitude
            return false;
        } else
        if ((latAbs <= 0.0001) && (lonAbs <= 0.0001)) {
            // small square off the coast of Africa (Ghana)
            return false;
        } else {
            return true;
        }
    }


	@Override
	public String toString() {
		return "latitude=" + latitude + "\n longitude=" + longitude;
	}

    
}
