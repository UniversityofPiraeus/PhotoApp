/**
 * 
 */
package gr.unipi.photoapp.activities;

import gr.unipi.photoapp.R;
import gr.unipi.photoapp.adapters.PhotoListAdapter;
import gr.unipi.photoapp.db.controller.DatabaseController;
import gr.unipi.photoapp.db.model.Photo;
import gr.unipi.photoapp.directory.PhotoDirectory;
import gr.unipi.photoapp.util.StringUtil;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author gperreas
 *
 */
public class CustomImageActivity 
	extends Activity
{

    private TextView textView;
    private ImageView imageView;
     
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_image);
 
        Photo photo = (Photo) getIntent().getExtras().getSerializable(Photo.KEY_INTENT);
        
        textView = (TextView) findViewById(R.id.image_extra_info);
        imageView = (ImageView) findViewById(R.id.image_preview);
        
        imageView.setImageURI(PhotoDirectory.getInstance().getImageUri(photo.getName()));
        textView.setText(StringUtil.getImageExtraInfo(photo));
	}

	
    

}
