package com.github.gilbertotorrezan.feelvision.shared.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 3:14:04 PM
 */
public class PhotoAnalysis implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<FaceAnalysis> faces;
	private List<TagAnalysis> tags;

	public List<FaceAnalysis> getFaces() {
		return faces;
	}

	public void setFaces(List<FaceAnalysis> faces) {
		this.faces = faces;
	}

	public List<TagAnalysis> getTags() {
		return tags;
	}
	
	public void setTags(List<TagAnalysis> tags) {
		this.tags = tags;
	}

}
