package com.github.gilbertotorrezan.feelvision.shared.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 4:04:54 PM
 */
public class FaceAnalysis implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ImagePoint> boundingPoly;
	private double detectionConfidence;
	private Likelihood joyLikelihood;
	private Likelihood sorrowLikelihood;
	private Likelihood angerLikelihood;
	private Likelihood surpriseLikelihood;
	private Likelihood underExposedLikelihood;
	private Likelihood blurredLikelihood;
	private Likelihood headwearLikelihood;

	public List<ImagePoint> getBoundingPoly() {
		return boundingPoly;
	}

	public void setBoundingPoly(List<ImagePoint> boundingPoly) {
		this.boundingPoly = boundingPoly;
	}

	public double getDetectionConfidence() {
		return detectionConfidence;
	}

	public void setDetectionConfidence(double detectionConfidence) {
		this.detectionConfidence = detectionConfidence;
	}

	public Likelihood getJoyLikelihood() {
		return joyLikelihood;
	}

	public void setJoyLikelihood(Likelihood joyLikelihood) {
		this.joyLikelihood = joyLikelihood;
	}

	public Likelihood getSorrowLikelihood() {
		return sorrowLikelihood;
	}

	public void setSorrowLikelihood(Likelihood sorrowLikelihood) {
		this.sorrowLikelihood = sorrowLikelihood;
	}

	public Likelihood getAngerLikelihood() {
		return angerLikelihood;
	}

	public void setAngerLikelihood(Likelihood angerLikelihood) {
		this.angerLikelihood = angerLikelihood;
	}

	public Likelihood getSurpriseLikelihood() {
		return surpriseLikelihood;
	}

	public void setSurpriseLikelihood(Likelihood surpriseLikelihood) {
		this.surpriseLikelihood = surpriseLikelihood;
	}

	public Likelihood getUnderExposedLikelihood() {
		return underExposedLikelihood;
	}

	public void setUnderExposedLikelihood(Likelihood underExposedLikelihood) {
		this.underExposedLikelihood = underExposedLikelihood;
	}

	public Likelihood getBlurredLikelihood() {
		return blurredLikelihood;
	}

	public void setBlurredLikelihood(Likelihood blurredLikelihood) {
		this.blurredLikelihood = blurredLikelihood;
	}

	public Likelihood getHeadwearLikelihood() {
		return headwearLikelihood;
	}

	public void setHeadwearLikelihood(Likelihood headwearLikelihood) {
		this.headwearLikelihood = headwearLikelihood;
	}

}
