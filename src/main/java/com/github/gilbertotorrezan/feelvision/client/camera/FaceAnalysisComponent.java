package com.github.gilbertotorrezan.feelvision.client.camera;

import gwt.material.design.addins.client.rating.MaterialRating;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.html.UnorderedList;

import com.github.gilbertotorrezan.feelvision.shared.model.FaceAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.model.ImagePoint;
import com.github.gilbertotorrezan.feelvision.shared.model.Likelihood;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 4:32:44 PM
 */
public class FaceAnalysisComponent extends Composite {

	private static FaceAnalysisComponentUiBinder uiBinder = GWT.create(FaceAnalysisComponentUiBinder.class);
	interface FaceAnalysisComponentUiBinder extends UiBinder<Widget, FaceAnalysisComponent> {
	}

	@UiField MaterialImage faceImage;
	@UiField MaterialLabel accuracy;
	@UiField MaterialRating joyLikelihood;
	@UiField MaterialRating sorrowLikelihood;
	@UiField MaterialRating angerLikelihood;
	@UiField MaterialRating surpriseLikelihood;
	@UiField MaterialRating underExposedLikelihood;
	@UiField MaterialRating blurredLikelihood;
	@UiField MaterialRating headwearLikelihood;
	
	public FaceAnalysisComponent() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setFace(MaterialImage originalImage, final FaceAnalysis face){
		if (!face.getBoundingPoly().isEmpty()){
			ImageElement el = originalImage.getElement().cast();
			int minX = el.getClientWidth();
			int minY = el.getClientHeight();
			int maxX = -minX;
			int maxY = -minY;
			
			for (ImagePoint point : face.getBoundingPoly()){
				if (point.getX() < minX){
					minX = point.getX();
				}
				if (point.getY() < minY){
					minY = point.getY();
				}
				if (point.getX() > maxX){
					maxX = point.getX();
				}
				if (point.getY() > maxY){
					maxY = point.getY();
				}
			}
			
			Canvas canvas = Canvas.createIfSupported();
			canvas.setCoordinateSpaceWidth(maxX - minX);
			canvas.setCoordinateSpaceHeight(maxY - minY);
			Context2d context = canvas.getContext2d();
			context.drawImage(el, minX, minY, maxX - minX, maxY - minY, 0, 0, maxX - minX, maxY - minY);
			if (maxX - minX > 200){
				faceImage.setWidth("200px");
			}
			faceImage.setUrl(canvas.toDataUrl("image/png"));
		}
		
		accuracy.setText(NumberFormat.getDecimalFormat().format(face.getDetectionConfidence() * 100) + "%");
		
		joyLikelihood.setValue(getLikelihoodRating(face.getJoyLikelihood()));
		joyLikelihood.setTooltip(joyLikelihood.getTooltip() + " " + getLikelihoodText(face.getJoyLikelihood()));
		
		sorrowLikelihood.setValue(getLikelihoodRating(face.getSorrowLikelihood()));
		sorrowLikelihood.setTooltip(sorrowLikelihood.getTooltip() + " " + getLikelihoodText(face.getSorrowLikelihood()));
		
		angerLikelihood.setValue(getLikelihoodRating(face.getAngerLikelihood()));
		angerLikelihood.setTooltip(angerLikelihood.getTooltip() + " " + getLikelihoodText(face.getAngerLikelihood()));
		
		surpriseLikelihood.setValue(getLikelihoodRating(face.getSurpriseLikelihood()));
		surpriseLikelihood.setTooltip(surpriseLikelihood.getTooltip() + " " + getLikelihoodText(face.getSurpriseLikelihood()));
		
		underExposedLikelihood.setValue(getLikelihoodRating(face.getUnderExposedLikelihood()));
		underExposedLikelihood.setTooltip(underExposedLikelihood.getTooltip() + " " + getLikelihoodText(face.getUnderExposedLikelihood()));
		
		blurredLikelihood.setValue(getLikelihoodRating(face.getBlurredLikelihood()));
		blurredLikelihood.setTooltip(blurredLikelihood.getTooltip() + " " + getLikelihoodText(face.getBlurredLikelihood()));
		
		headwearLikelihood.setValue(getLikelihoodRating(face.getHeadwearLikelihood()));
		headwearLikelihood.setTooltip(headwearLikelihood.getTooltip() + " " + getLikelihoodText(face.getHeadwearLikelihood()));
	}

	private int getLikelihoodRating(Likelihood likelihood){
		switch (likelihood) {
		case UNKNOWN:
		default: return 0;
		
		case VERY_UNLIKELY: return 1;
		case UNLIKELY: return 2;
		case POSSIBLE: return 3;
		case LIKELY: return 4;
		case VERY_LIKELY: return 5;
		}
	}
	
	private String getLikelihoodColor(Likelihood likelihood){
		switch (likelihood) {
		case UNKNOWN:
		default: return "grey";
		
		case VERY_UNLIKELY: return "red";
		case UNLIKELY: return "orange";
		case POSSIBLE: return "purple";
		case LIKELY: return "blue";
		case VERY_LIKELY: return "green";
		}
	}
	
	private String getLikelihoodText(Likelihood likelihood){
		switch (likelihood) {
		case UNKNOWN:
		default: return "Unknown";
		
		case VERY_UNLIKELY: return "Very unlikely";
		case UNLIKELY: return "Unlikely";
		case POSSIBLE: return "Possible";
		case LIKELY: return "Likely";
		case VERY_LIKELY: return "Very likely";
		}
	}
	
	@UiHandler("info")
	void onInfoClicked(ClickEvent evt){
		final MaterialModal modal = new MaterialModal();
		
		MaterialModalContent content = new MaterialModalContent();
		modal.add(content);
		
		MaterialLabel lbl = new MaterialLabel("These are the possible values for the metrics:");
		content.add(lbl);
		
		UnorderedList list = new UnorderedList();
		content.add(list);
		
		lbl = new MaterialLabel(getLikelihoodText(Likelihood.UNKNOWN));
		lbl.setTextColor(getLikelihoodColor(Likelihood.UNKNOWN));
		lbl.setPaddingTop(10);
		list.add(lbl);
		lbl = new MaterialLabel(getLikelihoodText(Likelihood.VERY_UNLIKELY));
		lbl.setTextColor(getLikelihoodColor(Likelihood.VERY_UNLIKELY));
		lbl.setPaddingTop(10);
		list.add(lbl);
		lbl = new MaterialLabel(getLikelihoodText(Likelihood.UNLIKELY));
		lbl.setTextColor(getLikelihoodColor(Likelihood.UNLIKELY));
		lbl.setPaddingTop(10);
		list.add(lbl);
		lbl = new MaterialLabel(getLikelihoodText(Likelihood.POSSIBLE));
		lbl.setTextColor(getLikelihoodColor(Likelihood.POSSIBLE));
		lbl.setPaddingTop(10);
		list.add(lbl);
		lbl = new MaterialLabel(getLikelihoodText(Likelihood.LIKELY));
		lbl.setTextColor(getLikelihoodColor(Likelihood.LIKELY));
		lbl.setPaddingTop(10);
		list.add(lbl);
		lbl = new MaterialLabel(getLikelihoodText(Likelihood.VERY_LIKELY));
		lbl.setTextColor(getLikelihoodColor(Likelihood.VERY_LIKELY));
		lbl.setPaddingTop(10);
		list.add(lbl);
		
		MaterialModalFooter footer = new MaterialModalFooter();
		modal.add(footer);
		
		MaterialButton close = new MaterialButton(ButtonType.FLAT);
		close.setText("Close");
		close.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.closeModal(true);
			}
		});
		footer.add(close);
		
		modal.addCloseHandler(new CloseHandler<MaterialModal>() {
			@Override
			public void onClose(CloseEvent<MaterialModal> event) {
				modal.removeFromParent();
			}
		});
		RootPanel.get().add(modal);
		modal.openModal();
	}

}
