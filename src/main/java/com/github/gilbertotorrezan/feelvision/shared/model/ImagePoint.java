package com.github.gilbertotorrezan.feelvision.shared.model;

import java.io.Serializable;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 4:06:11 PM
 */
public class ImagePoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
