package com.bmuse.core;

public class Coordinates implements Comparable<Coordinates> {
  private final int xPos;
  private final int yPos;

  public Coordinates(int xPos, int yPos) {
    assert(xPos >= 0 && yPos >= 0);
    this.xPos = xPos;
    this.yPos = yPos;
  }

  @Override
  public int hashCode() {
    return getX() ^ getY();
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
   * @param at - Coordinate to compare with
   */
  @Override
  public int compareTo(Coordinates at) {
    return Integer.valueOf(this.getX()).compareTo(at.getX()) != 0 
        ? Integer.valueOf(this.getX()).compareTo(at.getX())
            : Integer.valueOf(this.getY()).compareTo(at.getY());
          
  }

  public int getX() {
    return xPos;
  }

  public int getY() {
    return yPos;
  }

@Override
public String toString() {
	return "{xPos=" + xPos + ", yPos=" + yPos + "}";
}

  

}
