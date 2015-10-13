/**
 * 
 */
package gr.unipi.photoapp.directory;

import gr.unipi.photoapp.db.controller.DatabaseController;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * @author gperreas
 * Singleton Image Directory
 */
public class PhotoDirectory 
{
	private static final String TAG = PhotoSaver.class.getSimpleName();
	
	private static PhotoDirectory instance;
	
	private static final String FOLDER_NAME = "PHOTOAPP_DCIM";
	private static final String THUMBNAIL_FOLDER_NAME = "thumbnails";
	public static final String IMAGE_FILE_PREFIX = "IMAGE_";
	public static final String FOLDER_DELIMITER = "/";
	
	private static final int THUMB_WIDTH = 50;	
	private static final int THUMB_HEIGHT = 50;
	
	
	private File imageDirectory;
	private File imageThumbsDirectory;
	
	public PhotoDirectory() {
		initFolders();
	}
	
	public synchronized static PhotoDirectory getInstance() {
	      if(instance == null) {
	         instance = new PhotoDirectory();
	      }
	      return instance;
	}
	
	private void initFolders() {

	    imageDirectory = getNewFile(Environment.getExternalStorageDirectory(), FOLDER_NAME);
	    createFolder(imageDirectory);
	    
	    String thumbnailsFolderName = FOLDER_NAME + FOLDER_DELIMITER + THUMBNAIL_FOLDER_NAME;
	    
	    imageThumbsDirectory = getNewFile(Environment.getExternalStorageDirectory(), thumbnailsFolderName);
	    createFolder(imageThumbsDirectory);
	}
	
	private boolean createFolder(File dir)
	{
		boolean exist = true;

		if (!dir.exists()) {
	        if (!dir.mkdirs()) {
	            Log.e(TAG, "Problem while creating folder");
	            exist = false;
	        }
	        else {
	            Log.i(TAG, "Folder created");
	        }	        
		}
		
		return exist;
	}
	
	private File getNewFile(File dir, String name)
	{
		return new File(dir, name);
	}
	
	public File getImageDirectory() {		
		return imageDirectory;
	}
	
	public File getImageThubnailsDirectory() {		
		return imageThumbsDirectory;
	}

	public Uri getImageUri(String name) {
		
		return Uri.fromFile(
				new File(
						Environment.getExternalStorageDirectory(), 
						FOLDER_NAME + FOLDER_DELIMITER + name));
	}
	
	
	public void deleteImage(String name) {
		String path = FOLDER_DELIMITER+FOLDER_NAME+FOLDER_DELIMITER+name;	
		File file = getNewFile(Environment.getExternalStorageDirectory(), path);
		file.delete();
	}
	

	public Bitmap getImageThumbnailBitmap(String name) {

		String path = getImageUri(name).getPath();
		Bitmap bitmap = null;

		bitmap = ThumbnailUtils.extractThumbnail(
				BitmapFactory.decodeFile(path),
				THUMB_WIDTH, THUMB_HEIGHT);

		return bitmap;
	}
	
}
