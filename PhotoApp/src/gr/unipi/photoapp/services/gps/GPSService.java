/**
 * 
 */
package gr.unipi.photoapp.services.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * @author gperreas
 *
 */
public class GPSService 
	extends Service
	implements LocationListener
{
	
	private static final String TAG = GPSService.class.getSimpleName();

	
	private final Context context;

	boolean isGpsEnabled;
	boolean isNetworkEnabled;
	boolean canGetLocation;
	
	Location location;
	GeoLocation geoLocation = new GeoLocation();
	
	// The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
	
	/**
	 * @param context
	 */
	public GPSService(Context context) {
		this.context = context;
		getLocation();
	}

	
	private Location getLocation() {
		try {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGpsEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGpsEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e(TAG, "get Location via Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                        	geoLocation.setLatitude(location.getLatitude());
                        	geoLocation.setLongitude(location.getLongitude());
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGpsEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.e(TAG, "get Location via GPS (Enabled)");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                            	geoLocation.setLatitude(location.getLatitude());
                            	geoLocation.setLongitude(location.getLongitude());
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }


	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}


	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	/** 
     *  @return geoLocation
     **/
    public GeoLocation getGeoLocation() {
		return this.geoLocation;	
    }
    	
    /** 
     *  Function to check if best network provider
     *  @return boolean
     **/
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

   
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     **/
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSService.this);
        }       
    }
}
