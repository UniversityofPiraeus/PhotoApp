/**
 * 
 */
package gr.unipi.photoapp.db.controller;

import java.util.Collection;
import java.util.List;

import gr.unipi.photoapp.db.model.Photo;

/**
 * @author gperreas
 *
 */
public interface IDatabaseController 
{
	Photo getPhoto(String name);
	void insertPhoto(Photo photo);
	void updatePhoto(Photo photo);
	void deletePhoto(String id);
	List<Photo> getAllPhotos();
}
