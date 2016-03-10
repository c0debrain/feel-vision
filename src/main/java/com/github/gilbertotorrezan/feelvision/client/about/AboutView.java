package com.github.gilbertotorrezan.feelvision.client.about;

import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialTitle;

import com.github.gilbertotorrezan.feelvision.client.TokenDefaults;
import com.github.gilbertotorrezan.feelvision.client.resources.images.Images;
import com.github.gilbertotorrezan.gwtviews.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 5, 2016 3:28:30 PM
 */
@View(value = TokenDefaults.ABOUT_VIEW, publicAccess = true)
public class AboutView extends Composite {

	private static AboutViewUiBinder uiBinder = GWT.create(AboutViewUiBinder.class);
	interface AboutViewUiBinder extends UiBinder<Widget, AboutView> {
	}
	
	@UiField MaterialTitle projectTitle;
	@UiField MaterialImage developerImage;
	
	private boolean firstLoaded = false;
	
	public AboutView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		developerImage.setResource(Images.INSTANCE.developerImage());
	}
	
	@Override
	protected void onLoad() {
		if (!firstLoaded){
			firstLoaded = true;
			projectTitle.getElement().scrollIntoView();
		}
	}

}
