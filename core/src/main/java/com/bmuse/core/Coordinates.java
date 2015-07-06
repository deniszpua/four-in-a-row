package com.bmuse.core;

public class Coordinates implements Comparable<Coordinates> {
	private final int x, y;

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		return x^y;
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
			return (this.x == otherCoord.x) && (this.y == otherCoord.y);
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
		return Integer.valueOf(this.x).compareTo(o.x) != 0 
				? Integer.valueOf(this.x).compareTo(o.x)
						: Integer.valueOf(this.y).compareTo(o.y);
					
	}

	

}
