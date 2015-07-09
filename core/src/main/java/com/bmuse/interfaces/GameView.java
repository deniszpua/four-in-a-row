package com.bmuse.interfaces;

import java.util.List;

import com.bmuse.core.Ball;
import com.bmuse.core.Coordinates;

public interface GameView {
	
  
  /**
   * 
   * Remove ball image from view
   * @param at - position, where ball, that should be removed is placed.
   */
  void removeBall(Coordinates at);
  
  /**
   * 
   * Show all possible moves for player (should be different from past moves).
   * @param moves - list of positions, where next move is legal.
   * @param color - color of ball, that possible to place (depends on move's turn).
   */
  void processNextMove(List<Coordinates>  moves, Ball color);

}
