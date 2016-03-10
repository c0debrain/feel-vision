package com.github.gilbertotorrezan.feelvision.shared.model;

import java.io.Serializable;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 3:24:39 PM
 */
public class PhotoAnalysisRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dataUrl;
	
	public String getDataUrl() {
		return dataUrl;
	}
	
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

}
