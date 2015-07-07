package com.bmuse.core;

import java.util.HashMap;
import java.util.Map;

import com.bmuse.view.GameView;

import playn.core.Platform;
import playn.core.Surface;
import playn.scene.Layer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;

public class FourInARow extends SceneGame {
  
  public static enum Ball {WHITE, BLACK}
  
  public static final int BOARD_WIDTH = 7;
  public static final int BOARD_HEIGHT = 6;
  
  private Ball turn;
  private Map<Coordinates, Ball> board;
  private MoveListener moveListener;

  /**
   * Game model constructor
   * @param plat - platform dependent parameters
   */
  public FourInARow(Platform plat) {
    // update our "simulation" 33ms (30 times per second)
    super(plat, 33); 
    init();

    // Getting window size
    final IDimension viewSize = plat.graphics().viewSize;
    
    // Fill background with gray
    rootLayer.add(new Layer(){
      @Override
      protected void paintImpl(Surface surf) {
        surf.setFillColor(0xFFCCCCCC)
          .fillRect(0, 0, viewSize.width(), viewSize.height());
      }
    });
    
    // add game board
    rootLayer.add(new GameView(this, viewSize));
    
    startGame();
  }
  
  private void startGame() {
    addBallAt(new Coordinates(0, 0));
    switchPlayer();
    addBallAt(new Coordinates(3, 4));
  
  }
  
  private void switchPlayer() {
    turn = Ball.values()[(turn.ordinal() + 1) % Ball.values().length];

  }

  private void init() {
    turn = Ball.WHITE;
    board = new HashMap<>(BOARD_HEIGHT * BOARD_WIDTH);
    
  }
  
  private void addBallAt(Coordinates at) {
    board.put(at, turn);
    moveListener.ballAddedAt(at, turn);
    
  }
  
  /**
   * Restore initial board state (with no balls).
   */
  public void clearBoard() {
    for (Coordinates at : board.keySet()) {
      moveListener.ballRemoved(at);;
    }
    board.clear();
    
  }

  public void addMoveListener(MoveListener moveListener) {
    this.moveListener = moveListener;
  }
  
  
}
