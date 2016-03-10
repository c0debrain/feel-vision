package com.github.gilbertotorrezan.feelvision.client.resources.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 5, 2016 4:15:15 PM
 */
public interface Images extends ClientBundle {
	
	public static final Images INSTANCE = GWT.create(Images.class);
	
	@Source("developer.png")
	ImageResource developerImage();

}
