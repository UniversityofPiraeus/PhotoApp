package gr.unipi.photoapp.activities;

import gr.unipi.photoapp.R;
import gr.unipi.photoapp.fragments.Camera2Fragment;
import gr.unipi.photoapp.fragments.CameraFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

@SuppressLint({ "NewApi", "ShowToast", "ClickableViewAccessibility" }) 
public class CameraActivity 
	extends Activity 
{
	private static final String TAG = CameraActivity.class.getSimpleName();  
   
    private Camera2Fragment cameraFragment;  
    
    private RelativeLayout mLayout;
    
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        mLayout = (RelativeLayout) findViewById(R.id.container);		
        
        if (savedInstanceState==null) {
			if(isDeviceSupportCamera())
			{
				
				if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)  
                        && ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP))) {
					getFragmentManager().beginTransaction()
	                	.replace(R.id.container, CameraFragment.newInstance())
	                	.commit();
				} else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					getFragmentManager().beginTransaction()
                    	.replace(R.id.container, Camera2Fragment.newInstance())
                    	.commit();	
				}
			}
		}		

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
     * Checking device has camera hardware or not
     * 
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    
    
    @Override  
    protected void onPause() {  
        Log.i(TAG, "onPause");        
        super.onPause();  

    }  
    
    @Override  
    protected void onResume() {  
        super.onResume();  
        
        Log.e(TAG, "onResume");  
    }


    
}
