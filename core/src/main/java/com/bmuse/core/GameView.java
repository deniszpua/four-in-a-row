package com.bmuse.core;

import java.util.List;

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
  void showLegalMoves(List<Coordinates>  moves, FourInARow.Ball color);

}
