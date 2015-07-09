package com.bmuse.core;

import java.util.List;

public interface BoardModel {
  
  /**
   * 
   * Add player's move at given position
   */
  public void addBall(Coordinates at);
  
  /**Add reference to game view.
   * 
   */
  public void addMoveListener(GameView moveListener);
   
  public void startNewGame();
   
  public int getBoardWidth();
   
  public int getBoardHeight();
   
  /**
   * Start new game.
   * Restore initial board state (with no balls).
   * White always moves first.
   */
  public void clearBoard();
  
  public boolean isGameOver();
  
  /**
   * Returns list of all positions, where next ball can be placed
   * @return - list of Coordinates.
   */
  public List<Coordinates> listLegalMoves();
  
}
