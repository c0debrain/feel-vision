package com.github.gilbertotorrezan.feelvision.client.camera;

import gwt.material.design.addins.client.camera.MaterialCameraCapture;
import gwt.material.design.addins.client.camera.events.CameraCaptureEvent;
import gwt.material.design.addins.client.camera.events.CameraCaptureEvent.CaptureStatus;
import gwt.material.design.addins.client.camera.events.CameraCaptureHandler;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialChip;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTitle;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.github.gilbertotorrezan.feelvision.client.TokenDefaults;
import com.github.gilbertotorrezan.feelvision.shared.model.FaceAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.model.PhotoAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.model.PhotoAnalysisRequest;
import com.github.gilbertotorrezan.feelvision.shared.model.TagAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.ws.PhotoService;
import com.github.gilbertotorrezan.gwtviews.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 1:17:18 PM
 */
@View(value = TokenDefaults.CAMERA_VIEW, defaultView = true, publicAccess = true)
public class CameraView extends Composite {

	private static CameraViewUiBinder uiBinder = GWT.create(CameraViewUiBinder.class);
	interface CameraViewUiBinder extends UiBinder<Widget, CameraView> {
	}

	@UiField MaterialTitle viewTitle;
	@UiField MaterialCameraCapture video;
	@UiField MaterialImage image;
	
	@UiField MaterialButton capture;
	@UiField MaterialButton back;
	
	@UiField MaterialPanel results;
	
	public CameraView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		image.setDisplay(Display.NONE);
		back.setDisplay(Display.NONE);
		capture.setDisplay(Display.NONE);
		
		if (!MaterialCameraCapture.isSupported()){
			video.setDisplay(Display.NONE);
			
			MaterialLabel label = new MaterialLabel(
					"<i class=\"material-icons\">mood_bad</i><br/>"
					+ "Too bad! Your browser can't access the camera. Please consider using another browser to access this application.<br/>"
					+ "<a href=\"https://whatbrowser.org/\" target=\"_blank\">Click here for help.</a>");
			label.setPaddingTop(20);
			label.setTextAlign(TextAlign.CENTER);
			results.add(label);
		}
		else {
			MaterialLabel label = new MaterialLabel(
					"<i class=\"material-icons\">photo_camera</i><br/>"
					+ "Please grant the permission to access your camera.");
			label.setPaddingTop(20);
			label.setTextAlign(TextAlign.CENTER);
			results.add(label);
			
			video.addCameraCaptureHandler(new CameraCaptureHandler() {
				private boolean firstLoaded = false;
				@Override
				public void onCameraCaptureChange(CameraCaptureEvent event) {
					if (event.getCaptureStatus() == CaptureStatus.STARTED){
						if (!firstLoaded){
							firstLoaded = true;
							results.clear();
							capture.getElement().getStyle().clearDisplay();
						}
					}
					else if (event.getCaptureStatus() == CaptureStatus.ERRORED){
						capture.setDisplay(Display.NONE);
						video.setDisplay(Display.NONE);
						results.clear();
						
						MaterialLabel label = new MaterialLabel(
								"<i class=\"material-icons\">mood_bad</i><br/>"
								+ "Too bad! There was an error accessing your camera:<br/>"+ event.getErrorMessage());
						label.setPaddingTop(20);
						label.setTextAlign(TextAlign.CENTER);
						results.add(label);
					}
				}
			});			
		}
	}
	
	@UiHandler("back")
	void onBack(ClickEvent evt){
		image.setDisplay(Display.NONE);
		video.getElement().getStyle().clearDisplay();
		
		back.setDisplay(Display.NONE);
		capture.getElement().getStyle().clearDisplay();
		
		results.clear();
		viewTitle.setTitle("Take a picture");
		viewTitle.setDescription("We will try to detect your feelings...");
	}
	
	@UiHandler("capture")
	void onCapture(ClickEvent evt){
		String url = video.captureToDataURL("image/jpeg");
		image.setUrl(url);
		video.setDisplay(Display.NONE);
		image.getElement().getStyle().clearDisplay();
		
		capture.setDisplay(Display.NONE);
		
		results.clear();
		MaterialLoader.showLoading(true);
		
		PhotoAnalysisRequest request = new PhotoAnalysisRequest();
		int idx = url.indexOf("base64,"); 
		//get only the base64 encoded image, without the header
		request.setDataUrl(url.substring(idx + "base64,".length()));
		
		PhotoService service = GWT.create(PhotoService.class);
		service.analyze(request, new MethodCallback<PhotoAnalysis>() {
			@Override
			public void onSuccess(Method method, PhotoAnalysis response) {
				MaterialLoader.showLoading(false);
				
				viewTitle.setTitle("Analysis complete");
				viewTitle.setDescription("Scroll down to see the results.");
				
				back.getElement().getStyle().clearDisplay();
				
				if (!response.getTags().isEmpty()){
					MaterialPanel tags = new MaterialPanel();
					tags.setMarginTop(20);
					for (TagAnalysis tag : response.getTags()){
						MaterialChip chip = new MaterialChip();
						chip.setText(tag.getDescription());
						chip.setBackgroundColor("green darken-3");
						chip.setTextColor("white");
						chip.setMargin(4);
						tags.add(chip);
					}
					results.add(tags);
				}
				
				List<FaceAnalysis> faces = response.getFaces();
				if (faces.isEmpty()){
					MaterialLabel label = new MaterialLabel(
							"<i class=\"material-icons\">mood_bad</i><br/>"
							+ "Too bad! Couldn't find any faces on the image.");
					label.setPaddingTop(20);
					label.setTextAlign(TextAlign.CENTER);
					results.add(label);
				}
				else {
					for (FaceAnalysis faceAnalysis : faces) {
						FaceAnalysisComponent comp = new FaceAnalysisComponent();
						comp.setFace(image, faceAnalysis);
						results.add(comp);
					}
				}
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				GWT.log("Error on accessing server: " + exception, exception);
				MaterialLoader.showLoading(false);
				results.clear();
				MaterialLabel label = new MaterialLabel("An error occured when accessing the server. Please try again.<br/>" + exception);
				label.setPaddingTop(20);
				label.setTextAlign(TextAlign.CENTER);
				results.add(label);
				back.getElement().getStyle().clearDisplay();
			}
		});
	}

}
