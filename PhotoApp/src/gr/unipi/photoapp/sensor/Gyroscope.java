/**
 * 
 */
package gr.unipi.photoapp.sensor;

import java.io.Serializable;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * @author gperreas
 * http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-gyro
 */
public class Gyroscope
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private static final String TAG = Gyroscope.class.getSimpleName();
	
	// Create a constant to convert nanoseconds to seconds.
	private static final float NS2S = 1.0f / 1000000000.0f;
	private final float[] deltaRotationVector = new float[4];	
	protected static final float EPSILON = 0.000000001f;
	private float timestamp;
	
	private float x = 0;
	private float y = 0;
	private float z = 0;
	
	public Gyroscope() {
		
	}
	
	public Gyroscope(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void onSensorChanged(SensorEvent event) {
		// This timestep's delta rotation to be multiplied by the current rotation
		// after computing it from the gyro sample data.
		if (timestamp != 0) {
			final float dT = (event.timestamp - timestamp) * NS2S;
		    // Axis of the rotation sample, not normalized yet.
		    float axisX = event.values[0];
		    float axisY = event.values[1];
		    float axisZ = event.values[2];

		    // Calculate the angular speed of the sample
		    float omegaMagnitude = (float) Math.sqrt(Math.pow(axisX, 2) + Math.pow(axisY, 2) + Math.pow(axisZ, 2));
		    
		    // Normalize the rotation vector if it's big enough to get the axis
		    // (that is, EPSILON should represent your maximum allowable margin of error)
		    if (omegaMagnitude > EPSILON) {
		    	axisX /= omegaMagnitude;
		        axisY /= omegaMagnitude;
		        axisZ /= omegaMagnitude;
		    }

		    // Integrate around this axis with the angular speed by the timestep
		    // in order to get a delta rotation from this sample over the timestep
		    // We will convert this axis-angle representation of the delta rotation
		    // into a quaternion before turning it into the rotation matrix.
		    float thetaOverTwo = omegaMagnitude * dT / 2.0f;
		    float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
		    float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
		    deltaRotationVector[0] = sinThetaOverTwo * axisX;
		    deltaRotationVector[1] = sinThetaOverTwo * axisY;
		    deltaRotationVector[2] = sinThetaOverTwo * axisZ;
		    deltaRotationVector[3] = cosThetaOverTwo;
		    
		}

		timestamp = event.timestamp;
		float[] deltaRotationMatrix = new float[9];
		SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
		    // User code should concatenate the delta rotation we computed with the current rotation
		    // in order to get the updated rotation.
		    // rotationCurrent = rotationCurrent * deltaRotationMatrix;
		
		this.setX(deltaRotationVector[0]);
		this.setY(deltaRotationVector[1]);
		this.setZ(deltaRotationVector[2]);
		
		//Log.d(TAG, " X: " + this.getX() + " Y: " + this.getY() + " Z: " + this.getZ() + " deltaRotationVector[3]: " +  deltaRotationVector[3]);

	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}


	@Override
	public String toString() {
		return "Gyroscope x=" + x + ", y=" + y + ", z=" + z;
	}

}
