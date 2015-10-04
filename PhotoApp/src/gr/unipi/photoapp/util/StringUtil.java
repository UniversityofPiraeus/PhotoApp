/**
 * 
 */
package gr.unipi.photoapp.util;

import gr.unipi.photoapp.db.model.Photo;
import gr.unipi.photoapp.sensor.Compass;

/**
 * @author gperreas
 *
 */
public class StringUtil {

	public static String getImageExtraInfo(Photo photo) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Name: "+ photo.getName());
		sb.append("\nDate: "+ photo.getTimestamp());
		sb.append("\nGeolocation: "+ photo.getLocation().toString());
		sb.append("\nAzimuth: "+ photo.getAzimuth() +" or "+
				Compass.getAzimuthToString(photo.getAzimuth()));
		sb.append("\nGyroscope: "+ photo.getGyroscope().toString());
		
		return sb.toString();
	}
}
