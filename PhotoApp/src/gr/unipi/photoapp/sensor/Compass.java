/**
 * 
 */
package gr.unipi.photoapp.sensor;

import android.R;

/**
 * @author gperreas
 */
public class Compass 
{
	/**
	 * https://en.wikipedia.org/?title=Azimuth
	 * @param azimuth in radians
	 * @return heading in degrees
	 */
	public static float toDegrees(float azimuth) {	
		return (float)(Math.toDegrees(azimuth)+360)%360;
	}
	
	/**
    *** Returns a String representation of the specified compass azimuth value
    *** @param azimuth in degrees The compass azimuth value to convert to a String representation
    *** @return A String representation of the compass azimuth
    **/
	public static String getAzimuthToString(float azimuth)
    {
        if (!Float.isNaN(azimuth) && (azimuth >= 0.0)) {
            int a = getAzimuthId(azimuth);
            
            //return DIRECTION[(a > 7)? 0 : a];
            switch (a) {
	            case 0: return "North";
	            case 1: return "North East";
	            case 2: return "East";
	            case 3: return "South East";
	            case 4: return "South";
	            case 5: return "South West";
	            case 6: return "West";
	            case 7: return "North West";
            }
            return "";
        } else {
            return "";
        }

    }
   	
   	private static int getAzimuthId(float azimuth)
   	{
   		if (!Double.isNaN(azimuth) && (azimuth >= 0.0)) {
   			return (int)Math.round(azimuth / 45.0) % 8;
   		}

   		return -1;
   	}
	
}
