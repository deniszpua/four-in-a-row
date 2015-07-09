package com.bmuse.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.bmuse.view.BoardLayer;

import playn.core.Platform;
import playn.core.Surface;
import playn.scene.Layer;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;

public class FourInARow extends SceneGame {
  
  public static enum Ball {WHITE, BLACK}
  
  public static final int BOARD_WIDTH = 7;
  public static final int BOARD_HEIGHT = 6;
  
  private Ball turn;
  private Map<Coordinates, Ball> board;
  private GameView moveListener;
  public final Pointer pointer;

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
    
    // Wire up pointer and mouse events
    pointer = new Pointer(plat, rootLayer, false);
    plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, false));
    
    // Fill background with gray
    rootLayer.add(new Layer(){
      @Override
      protected void paintImpl(Surface surf) {
        surf.setFillColor(0xFFCCCCCC)
          .fillRect(0, 0, viewSize.width(), viewSize.height());
      }
    });
    
    // add game board
    rootLayer.add(new BoardLayer(this, viewSize));
    
    startGame();
  }
  
  private void startGame() {
	  moveListener.showLegalMoves(possibleMoves(this), turn);
    //add test ball to test graphics
//    addBallAt(new Coordinates(0, 0));
//    addBallAt(new Coordinates(0, 5));
//    addBallAt(new Coordinates(3, 5));
  
  }
  
  private void switchPlayer() {
    turn = Ball.values()[(turn.ordinal() + 1) % Ball.values().length];

  }

  private void init() {
    turn = Ball.WHITE;
    board = new HashMap<>(BOARD_HEIGHT * BOARD_WIDTH);
    
  }
  
  public void addBallAt(Coordinates at) {
    
	assert(!board.containsKey(at));
    assert(at.getX() >=0 && at.getX() < BOARD_WIDTH 
    		&& at.getY() >= 0 && at.getY() < BOARD_HEIGHT);
    
    board.put(at, turn);

    if (!isGameOver()){
      switchPlayer();
      moveListener.showLegalMoves(possibleMoves(this), turn);
    }
    else {
      //TODO save score and exit to main menu
      System.out.println("Winner is " + turn);
    }
    
  }
  
  /**
   * Restore initial board state (with no balls).
   * White always moves first.
   */
  public void clearBoard() {
    for (Coordinates at : board.keySet()) {
      moveListener.removeBall(at);;
    }
    board.clear();
    turn = Ball.WHITE;
    
  }

  public void addMoveListener(GameView moveListener) {
    this.moveListener = moveListener;
  }
  
  public boolean isGameOver(){
    /*
     * Possible winner combinations are:
     *   horizontal row
     *   vertical row
     *   bottom-left to upper-right diagonal (slash)
     *   backslash.
     * 
     */
    
    //no possible moves
    if (board.entrySet().size() >= BOARD_HEIGHT * BOARD_WIDTH) {
      return true;
    }
    
    List<Coordinates> ballsPlaces = new ArrayList<>(board.keySet());
    
    //remove opponent's balls
    ballsPlaces.removeIf(new Predicate<Coordinates>() {
      @Override
      public boolean test(Coordinates t) {
        return !board.get(t).equals(turn);
      }
    });
    
    final int BALLS_TO_WIN = 4;
    
    for (Coordinates current : ballsPlaces) {
      //test horizontal from left to right
      //check only for columns smaller than 4
      boolean lineBreaked = false;
      if (current.getX() <= BOARD_WIDTH - BALLS_TO_WIN){
        for (int offset = 1; offset < BALLS_TO_WIN; offset++){
          if (!turn.equals(board.get(
              new Coordinates(current.getX() + offset, current.getY())
          )))  {
            lineBreaked = true;
            break;
          }
        }
        if (!lineBreaked) {
          return true;
        }
        
      }
      
      //test for vertical line
      //check only for rows down from second
      if (current.getY() >= BALLS_TO_WIN - 1) {
        lineBreaked = false;
        for (int offset = 1; offset < BALLS_TO_WIN; offset++){
          if (!turn.equals(board.get(
              new Coordinates(current.getX(), current.getY() - offset)
            ))) {
            lineBreaked = true;
            break;
          }
        }
        if (!lineBreaked) {
          return true;
        }
      }
      
      //test for slash diagonal
      //check only balls in rows down from two 
      //and columns smaller than 4
      if (current.getY() >= BALLS_TO_WIN - 1
          && current.getX() <= BOARD_WIDTH - BALLS_TO_WIN) {
        lineBreaked = false;
        for (int offset=1; offset < BALLS_TO_WIN; offset++) {
          if (!turn.equals(board.get(
              new Coordinates(current.getX() + offset, current.getY() - offset)
          ))) {
            lineBreaked = true;
            break;
          }
        }
        if (!lineBreaked) {
          return true;
        }
      }
      
      //test for backslash diagonal
      //check only balls in rows upper than 4
      //and columns smaller than 4
      if (current.getY() <= BOARD_HEIGHT - BALLS_TO_WIN 
          && current.getX() <= BOARD_WIDTH - BALLS_TO_WIN) {
        lineBreaked = false;
        for (int offset = 1; offset < BALLS_TO_WIN; offset++) {
          if (!turn.equals(board.get(
              new Coordinates(current.getX() + offset, current.getY() + offset)
          ))) {
            lineBreaked = true;
            break;
          }
        }
        if (!lineBreaked) {
          return true;
        }
        
      }
      
      //check other balls
      }
    
    
    return false;
    
  }
  
  /**
   * Lists all coordinates where placing new ball is possible. 
   * @param game - game to analyze.
   */
  public static List<Coordinates> possibleMoves(FourInARow game) {
    List<Coordinates> result = new ArrayList<>(BOARD_WIDTH);
    for (int colunm = 0; colunm < BOARD_WIDTH; colunm++) {
      for (int row = BOARD_HEIGHT - 1; row >=0; row--) {
    	  Coordinates current = new Coordinates(colunm, row);
    	  if(!game.board.containsKey(current)) {
    		  result.add(current);
    		  break;
    	  }
      }
	}
	  return result;
  }
  
}
