package com.github.gilbertotorrezan.feelvision.client;

import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialSideNav;
import gwt.material.design.client.ui.html.ListItem;

import com.github.gilbertotorrezan.gwtviews.client.HasViews;
import com.github.gilbertotorrezan.gwtviews.client.URLToken;
import com.github.gilbertotorrezan.gwtviews.client.ViewContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 1:16:23 PM
 */
@ViewContainer
public class FeelVisionContainer extends Composite implements HasViews {

	private static FeelVisionContainerUiBinder uiBinder = GWT.create(FeelVisionContainerUiBinder.class);
	interface FeelVisionContainerUiBinder extends UiBinder<Widget, FeelVisionContainer> {
	}
	
	@UiField MaterialContainer center;
	@UiField MaterialSideNav sideBar;

	public FeelVisionContainer() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler({"cameraLink", "aboutLink"})
	void onLinkClicked(ClickEvent evt){
		int width = Window.getClientWidth();
		if (width <= 600){
			sideBar.hide();
		}
	}
	
	@Override
	public void showView(URLToken url, Widget view) {
		center.clear();
		center.add(view);
		
		int count = sideBar.getWidgetCount();
		for (int i = 0; i< count; i++){
			Widget w = sideBar.getWidget(i);
			if (w instanceof ListItem){
				w.removeStyleName("active");
				
				Widget item = ((ListItem)w).getWidget(0);
				if (item instanceof MaterialLink){
					if (((MaterialLink) item).getTargetHistoryToken().equals(url.getId())){
						w.addStyleName("active");
					}					
				}
			}
		}
	}

}
