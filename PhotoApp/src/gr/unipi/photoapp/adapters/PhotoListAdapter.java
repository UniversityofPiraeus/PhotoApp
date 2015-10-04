/**
 * 
 */
package gr.unipi.photoapp.adapters;


import java.util.List;

import gr.unipi.photoapp.R;
import gr.unipi.photoapp.db.controller.DatabaseController;
import gr.unipi.photoapp.db.model.Photo;
import gr.unipi.photoapp.directory.PhotoDirectory;
import gr.unipi.photoapp.touch.SwipeGestureListener;
import gr.unipi.photoapp.touch.SwipeGestureListener.SimpleGestureListener;
import gr.unipi.photoapp.util.StringUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author gperreas
 *
 */
@SuppressLint({ "DefaultLocale", "ClickableViewAccessibility" }) 
public class PhotoListAdapter 
	extends ArrayAdapter<Photo>
	implements OnTouchListener, SimpleGestureListener
{
	
	private final Context context;
	private List<Photo> photos;
	private GestureDetector mGestureDetector;
	private DatabaseController controller;
	
	static class ViewHolder{
		public TextView imageInfo;
	    public ImageView image;
	}
	
	/**
	 * @param context
	 * @param resource
	 * @param photos
	 */
	public PhotoListAdapter(Context context, DatabaseController controller, List<Photo> photos) {
		super(context, R.layout.photo_row_layout, photos);
		this.context = context;
		this.photos = photos;
		this.controller = controller;
		this.mGestureDetector =
				new GestureDetector(context, new SwipeGestureListener((Activity)context,this));

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rView = convertView;

		if(rView==null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			rView = inflater.inflate(R.layout.photo_row_layout, null);
			rView.setOnTouchListener(this);
			ViewHolder vHolder = new ViewHolder();

			vHolder.image = (ImageView) rView.findViewById(R.id.image_preview);
			vHolder.imageInfo = (TextView) rView.findViewById(R.id.image_extra_info);
	
			rView.setTag(vHolder);
		}
		
		ViewHolder vHolder = (ViewHolder) rView.getTag();
		
		Photo p = photos.get(position);
		
		//Get thubnail of image
		Bitmap bitmap = null;
		bitmap = PhotoDirectory.getInstance().getImageThumbnailBitmap(p.getName());
		if(bitmap==null) {
			controller.deletePhoto(p.getId());
			photos.remove(position);
		}
		
		//Uri uri = PhotoDirectory.getInstance().getImageUri(p.getName());
		vHolder.image.setImageBitmap(bitmap);
		vHolder.imageInfo.setText(StringUtil.getImageExtraInfo(p));
		
		return rView;
	}
	
	@Override
	public int getCount() {
		return photos.size();
	}
 
	@Override
	public Photo getItem(int position) {
		return photos.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onSwipe(int direction) {
		String str = "";

		switch (direction) {
			case SwipeGestureListener.SWIPE_RIGHT:
				str = "Swipe Right";
				break;
	
			case SwipeGestureListener.SWIPE_LEFT:
				str = "Swipe Left";
				break;
				
			case SwipeGestureListener.SWIPE_DOWN:
				str = "Swipe Down";
				break;
				
			case SwipeGestureListener.SWIPE_UP:
				str = "Swipe Up";
				break;
		}
		
	    Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDoubleTap() {
		Toast.makeText(context, "Double Tap", Toast.LENGTH_LONG).show();
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return this.mGestureDetector.onTouchEvent(event);
	}


	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}



}
