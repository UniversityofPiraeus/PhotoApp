/**
 * 
 */
package gr.unipi.photoapp.directory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.util.Log;

/**
 * @author gperreas
 *
 * Saves a JPEG {@link Image} into the specified {@link File}.
 */
@SuppressLint("NewApi") 
public class PhotoSaver
	implements Runnable
{
	private static final String TAG = PhotoSaver.class.getSimpleName();
	
	private static final int THUMB_WIDTH = 50;	
	private static final int THUMB_HEIGHT = 50;
	
	
	/**
     * The JPEG image
     */
    private final Image mImage;
    /**
     * The file we save the image into.
     */
    private final File mFile;
    
    private boolean isThumbNail = false;
    
    public PhotoSaver(Image image, File file) {
        mImage = image;
        mFile = file;
    }
    
    public PhotoSaver(Image image, File file, boolean toThumbnail) {
        mImage = image;
        mFile = file;
        isThumbNail = toThumbnail;
    }


    @Override
    public void run() {
    	
    	ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(mFile);
            output.write(bytes);
            
            Log.i(TAG, mFile.getAbsolutePath());
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
