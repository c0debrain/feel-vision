package com.github.gilbertotorrezan.feelvision.client;

import org.fusesource.restygwt.client.Defaults;

import com.github.gilbertotorrezan.gwtviews.client.NavigationManager;
import com.github.gilbertotorrezan.gwtviews.client.analytics.UniversalAnalyticsTracker;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 1:00:22 PM
 */
public class FeelVisionEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		//configuring the google analytics tracker
		UniversalAnalyticsTracker.configure("UA-74700985-1");

		//setting the root path for rest calls
		Defaults.setServiceRoot("");
		
		//setting the default date format to be able to accept java.util.Date objects
		Defaults.setDateFormat(null);

		//remove the "Loading" message
		DOM.getElementById("initDisplay").removeFromParent();

		//start the application
		NavigationManager.start(RootPanel.get());
	}

}
