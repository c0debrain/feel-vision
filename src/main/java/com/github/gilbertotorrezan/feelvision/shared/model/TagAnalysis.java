package com.github.gilbertotorrezan.feelvision.shared.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 4:10:27 PM
 */
public class TagAnalysis implements Serializable {

	private static final long serialVersionUID = 1L;

	private String description;
	private double score;
	private List<ImagePoint> boundingPoly;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<ImagePoint> getBoundingPoly() {
		return boundingPoly;
	}

	public void setBoundingPoly(List<ImagePoint> boundingPoly) {
		this.boundingPoly = boundingPoly;
	}

}
