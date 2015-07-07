package com.bmuse.core;

public class Coordinates implements Comparable<Coordinates> {
	private final int x, y;

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		return getX()^getY();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		else if (! (other instanceof Coordinates)) {
			return false;
		}
		else {
			Coordinates otherCoord = (Coordinates) other;
			return (this.getX() == otherCoord.getX()) && (this.getY() == otherCoord.getY());
		}
	}
	
	

	/**
	 * Point is meant to be smaller if it has smaller x coordinate,
	 * or smaller y, if x is the same.
	 * @param o - Coordinate to compare with
	 * @return
	 */
	@Override
	public int compareTo(Coordinates o) {
		return Integer.valueOf(this.getX()).compareTo(o.getX()) != 0 
				? Integer.valueOf(this.getX()).compareTo(o.getX())
						: Integer.valueOf(this.getY()).compareTo(o.getY());
					
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	

}
