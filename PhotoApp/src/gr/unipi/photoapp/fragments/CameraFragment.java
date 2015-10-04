/**
 * 
 */
package gr.unipi.photoapp.fragments;

import java.io.File;
import java.lang.ref.Reference;
import java.util.Date;
import java.util.List;

import gr.unipi.photoapp.R;
import gr.unipi.photoapp.activities.PhotoListActivity;
import gr.unipi.photoapp.camera.CameraConstraints;
import gr.unipi.photoapp.db.controller.DatabaseController;
import gr.unipi.photoapp.db.model.Photo;
import gr.unipi.photoapp.directory.PhotoDirectory;
import gr.unipi.photoapp.sensor.Compass;
import gr.unipi.photoapp.sensor.Gyroscope;
import gr.unipi.photoapp.services.gps.GPSService;
import gr.unipi.photoapp.services.gps.GeoLocation;
import gr.unipi.photoapp.views.AutoFitTextureView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PreviewCallback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author gperreas
 *
 */
@SuppressLint("NewApi") 
public class CameraFragment 
	extends Fragment 
	implements View.OnClickListener, OnTouchListener, SensorEventListener 
{
	private static final String TAG = CameraFragment.class.getSimpleName();

    private AutoFitTextureView mTextureView;
    /**
     * This is the output file for our picture.
     */
    private File mFile;
    
    public static final String MEDIA_TYPE_IMAGE = ".jpg";
    
    private DatabaseController dbController;
    private Gyroscope gyroScope = new Gyroscope();
    private GPSService gpsService;
    private SensorManager mSensorManager;
    
    /**
     * This is the sensors (mAccelerometer,mMagnetometer) to calculate the heading (Compass).
     */
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    /**
     *  A {@link Sensor} of Gyroscope
     */
    private Sensor mGyroscope;
    
    public static enum LayoutMode {
        FitToParent, // Scale to the size that no side is larger than the parent
        NoBlank // Scale to the size that no side is smaller than the parent
    };
    
    public interface PreviewReadyCallback {
        public void onPreviewReady();
    }
    
  
	public CameraFragment() {
	}

	public static CameraFragment newInstance() {
    	CameraFragment fragment = new CameraFragment();
        fragment.setRetainInstance(true);
        
        return fragment;
    }
	
	/**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
  
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_old, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.image_info_btn).setOnClickListener(this);
        view.setOnTouchListener(this);
        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbController = new DatabaseController(getActivity().getBaseContext());
        
        initSensors();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTextureView.isAvailable()) {
            //openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
        
        registerSensorManagerListener();
 
    }
    

    @Override
    public void onPause() {
        unregisterSensorManagerListener();
        super.onPause();
    }

	@Override
	public void onStop() {	
        unregisterSensorManagerListener();	
		super.onStop();
	}
	
   

	
    /**
     * A {@link Handler} for showing {@link Toast}s.
     */
    private Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = getActivity();
            if (activity != null) {
                Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    
    @Override
   	public boolean onTouch(View v, MotionEvent event) {

   		switch (event.getAction()) {
   		    case MotionEvent.ACTION_DOWN:
   			    takePicture();
   		        Log.i("onTouch", "ACTION_DOWN");
   		        break;

   	    }
   	    return true;
   	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
	        case R.id.image_info_btn: {
	        	
	        	Intent intent = new Intent(getActivity(), PhotoListActivity.class);
	    		startActivity(intent);
	
	            break;
	        }
	    }
	}
	

	private  String fileName;
	private Date imageTimestamp;
	/**
     * Initiate a still image capture.
     */
    private void takePicture() {
    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        	imageTimestamp = new Date();
        	fileName = PhotoDirectory.IMAGE_FILE_PREFIX+imageTimestamp.getTime()+MEDIA_TYPE_IMAGE;
        	
        	Uri uriSavedImage=Uri.fromFile(new File(PhotoDirectory.getInstance().getImageDirectory() +
        			PhotoDirectory.FOLDER_DELIMITER + fileName));
        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        	startActivityForResult(
        			takePictureIntent, CameraConstraints.REQUEST_IMAGE_CAPTURE);
        }
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CameraConstraints.REQUEST_IMAGE_CAPTURE 
				&& resultCode == CameraConstraints.RESULT_OK) {
			
            gpsService = new GPSService(getActivity());
            GeoLocation location = new GeoLocation();
            if(gpsService.canGetLocation()) {
            	location = gpsService.getGeoLocation();    	
            }
                        
            Photo p = new Photo(); 
            p.setLocation(location);
			p.setAzimuth(azimuth);
			p.setGyroscope(gyroScope);
			p.setName(fileName);
			p.setTimestamp(imageTimestamp);
			
			dbController.insertPhoto(p);
	    }
	}

	private void initSensors()
	{
		mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
	       
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	}
	
	private void registerSensorManagerListener()
	{
		 mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	     mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
	     mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_UI);
	}
	
	private void unregisterSensorManagerListener()
	{
        mSensorManager.unregisterListener(this);	
	}

	//Compass
    private float[] mGravity;
	private float[] mGeomagnetic;
	private float azimuth = 0;
	
	//Gyroscope
	// Create a constant to convert nanoseconds to seconds.
	private static final float NS2S = 1.0f / 1000000000.0f;
	private final float[] deltaRotationVector = new float[4];
	private float timestamp;

	
	/**
	 * {@link Reference <a>http://www.mathworks.com/matlabcentral/fileexchange/40876-android-sensor-support-from-matlab--r2013a--r2013b-/content/sensorgroup/Examples/html/CapturingAzimuthRollPitchExample.html</a>}
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;
	    
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
	    	mGeomagnetic = event.values;
	    
	    if(mGravity != null && mGeomagnetic != null) {
	    	float R[] = new float[9];
	    	float I[] = new float[9];
	    	boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
	    	if(success) {
	    		float orientation[] = new float[3];
	    		SensorManager.getOrientation(R, orientation);
	    		azimuth = Compass.toDegrees(orientation[0]); // orientation contains: azimuth, pitch and roll
	    	}
	    }
	    
	    //TODO ADD GYROSCOPE
	    if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)		    	
		    gyroScope.onSensorChanged(event);
	}

	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}


}
