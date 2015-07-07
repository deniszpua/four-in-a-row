package com.bmuse.core;

public interface MoveListener {
  
  void ballAddedAt(Coordinates at, FourInARow.Ball color);
  
  void ballRemoved(Coordinates at);

}
