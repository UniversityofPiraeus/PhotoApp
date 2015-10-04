package gr.unipi.photoapp.activities;

import java.util.List;

import gr.unipi.photoapp.R;
import gr.unipi.photoapp.adapters.PhotoListAdapter;
import gr.unipi.photoapp.db.controller.DatabaseController;
import gr.unipi.photoapp.db.model.Photo;
import gr.unipi.photoapp.directory.PhotoDirectory;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

@SuppressLint("ClickableViewAccessibility")
public class PhotoListActivity
	extends Activity 
	implements OnItemClickListener, OnItemLongClickListener, OnTouchListener 
{
	private static final String TAG = PhotoListActivity.class.getSimpleName();
	
	private GestureDetector mGestureDetector;
	private DatabaseController dbController;		
	private	PhotoListAdapter adapter;
	private List<Photo> results;
	private boolean isAlertDialogOpen = false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
       
        ListView list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
        dbController = new DatabaseController(this);
        results = dbController.getAllPhotos();
        if(results!=null && results.size()>0)
        {
        	adapter = new PhotoListAdapter(this, dbController, results);
        	
        	//adapter.addAll(results);
        	list.setAdapter(adapter);
        }
        /*
         *   AlertDialog.Builder adb=new AlertDialog.Builder(MyActivity.this);
        adb.setTitle("Delete?");
        adb.setMessage("Are you sure you want to delete " + position);
        final int positionToRemove = position;
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyDataObject.remove(positionToRemove);
                adapter.notifyDataSetChanged();
            }});
        adb.show();
        }
         */
	}

//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//
//		super.onListItemClick(l, v, position, id);
//		
//        
//	}
	
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return mGestureDetector.onTouchEvent(event);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if(isAlertDialogOpen)
			return;
		
		Photo photo = results.get(position);
		
		Log.i(TAG, "Click : \n  Position :"+position+"  \n  ListItem : " +photo.toString());
	      
	    //Pass photo serializable object to intent
	             
	    Intent intent = new Intent(this, CustomImageActivity.class);
//	    Bundle bundle = new Bundle();  
//	    bundle.putSerializable(Photo.KEY_INTENT, photo);
	    intent.putExtra(Photo.KEY_INTENT, photo); 
	       
	    startActivity(intent);
	}

	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		removeItemFromList(position);   
		
		return false;
	}

	/**
	 * @param position
	 */
	private void removeItemFromList(int position) {
		
		final int deletePosition = position;
	       
	    AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    alert.setTitle("Delete");
	    alert.setMessage("Do you want delete this item?");
	    alert.setPositiveButton("Ok", new OnClickListener() {

	    	@Override
	    	public void onClick(DialogInterface dialog, int which) {
	    		
	    		dbController.deletePhoto(results.get(deletePosition).getId());
				PhotoDirectory.getInstance().deleteImage(results.get(deletePosition).getName());
				results.remove(deletePosition);
				adapter.notifyDataSetChanged();
				
				isAlertDialogOpen = false;
			}
	    });
	     
	    alert.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				isAlertDialogOpen = false;
			}
		});
	    
	    alert.show();
	    
	    isAlertDialogOpen = true;
	}
}
