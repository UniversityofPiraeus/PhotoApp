/**
 * 
 */
package gr.unipi.photoapp.camera2;

/**
 * @author gperreas
 *
 */
public class Camera2Constraints 
{

    /**
     * Camera state: Showing camera preview.
     */
	public static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
	public static final int STATE_WAITING_LOCK = 1;
    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
	public static final int STATE_WAITING_PRECAPTURE = 2;
    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
	public static final int STATE_WAITING_NON_PRECAPTURE = 3;
    /**
     * Camera state: Picture was taken.
     */
	public static final int STATE_PICTURE_TAKEN = 4;
}
