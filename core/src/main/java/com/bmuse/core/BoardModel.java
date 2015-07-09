package com.bmuse.core;

public interface BoardModel {
	
	/**
	 * 
	 * Add player's move at given position
	 */
	public void addBall(Coordinates at);
	
	/**
	 * 
	 * Add game view as listener;
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
	
}
