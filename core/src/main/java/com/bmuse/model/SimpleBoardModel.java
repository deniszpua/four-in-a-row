package com.bmuse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.bmuse.interfaces.BoardModel;
import com.bmuse.interfaces.GameExitHandler;
import com.bmuse.interfaces.GameView;
import com.bmuse.interfaces.GameboardMenu;
import com.bmuse.resources.Context;

public class SimpleBoardModel implements BoardModel {
  private static final Ball MOVES_FIRST = Ball.ORANGE;
  private final int boardWidth;
  private final int boardHeight;
  private final int ballsToWin;
  private GameView view;
  private final Map<Coordinates, Ball> board;
  private Ball turn;
  private GameExitHandler handler;
  private long timer;
  private int timeElapsed = -1;
  

  /**
   * Constructor.
   * @param boardWidth - number of cells in each game board row.
   * @param boardHeight - number of cells in each game board column.
   * @param ballsToWin - minimum number of balls in line or diagonal to finish game
   */
  public SimpleBoardModel(GameExitHandler handler) {
    this.boardWidth = Context.BOARD_WIDTH;
    this.boardHeight = Context.BOARD_HEIGHT;
    this.ballsToWin = Context.BALLS_TO_WIN;
    this.handler = handler;
    board = new HashMap<>(boardHeight * boardWidth);
    turn = MOVES_FIRST;
  }

  @Override
  public void addBall(Coordinates at) {
    assert (!board.containsKey(at));
    assert (at.getX() >= 0 && at.getX() < boardWidth 
        && at.getY() >= 0 && at.getY() < boardHeight);
      
    board.put(at, turn);

    if (!isGameOver()) {
      switchPlayer();
      if (view != null) {
        view.processNextMove(listLegalMoves(), turn);
      }
    } else {
      long timeElapsed = System.currentTimeMillis() - timer;
      this.timeElapsed = (int) timeElapsed;

      if (handler != null) {
    	  handler.onGameFinished(timeElapsed);
    	  view.showNewGameInvitation(turn.toString());
      }
      
    }
      
      

  }

  @Override
  public void addMoveListener(GameView moveListener) {
    view = moveListener;
    
  }

  @Override
  public int getBoardWidth() {
    return boardWidth;
  }

  @Override
  public int getBoardHeight() {
    return boardHeight;
  }

  @Override
  public void startNewGame() {
    view.processNextMove(listLegalMoves(), turn);
    timer = System.currentTimeMillis();
    
  }

  @Override
  public List<Coordinates> listLegalMoves() {
    List<Coordinates> result = new ArrayList<>(boardWidth);
    for (int colunm = 0; colunm < boardWidth; colunm++) {
      for (int row = boardHeight - 1; row >= 0; row--) {
        Coordinates current = new Coordinates(colunm, row);
        if (!board.containsKey(current)) {
          result.add(current);
          break;
        }
      }
    }
    return result;
  }
  
    
  @Override
  public boolean isGameOver() {
  /*
   * Possible winner combinations are:
   *   horizontal row
   *   vertical row
   *   bottom-left to upper-right diagonal (slash)
   *   backslash.
   * 
   */
        
    //no possible moves
    if (board.entrySet().size() >= boardHeight * boardWidth) {
      return true;
    }
    
    List<Coordinates> ballsPlaces = new ArrayList<>(board.keySet());
    
    //remove opponent's balls
    ballsPlaces.removeIf(new Predicate<Coordinates>() {
      @Override
      public boolean test(Coordinates at) {
        return !board.get(at).equals(turn);
      }
    });
    
    for (Coordinates current : ballsPlaces) {
      //test horizontal from left to right
      //check only for columns smaller than 4
      boolean lineBreaked = false;
      if (current.getX() <= boardWidth - ballsToWin) {
        for (int offset = 1; offset < ballsToWin; offset++) {
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
      if (current.getY() >= ballsToWin - 1) {
        lineBreaked = false;
        for (int offset = 1; offset < ballsToWin; offset++) {
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
      if (current.getY() >= ballsToWin - 1
          && current.getX() <= boardWidth - ballsToWin) {
        lineBreaked = false;
        for (int offset = 1; offset < ballsToWin; offset++) {
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
      if (current.getY() <= boardHeight - ballsToWin 
          && current.getX() <= boardWidth - ballsToWin) {
        lineBreaked = false;
        for (int offset = 1; offset < ballsToWin; offset++) {
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
    
  @Override
  public void clearBoard() {

    if (view != null) {
      for (Coordinates at : board.keySet()) {
        view.removeBall(at);;
      }
    }
    board.clear();
    turn = MOVES_FIRST;
  
  }
  
  private void switchPlayer() {
	  turn = Ball.values()[(turn.ordinal() + 1) % Ball.values().length];
	  
  }

	@Override
	public void menuItemSelected(GameboardMenu option) {
		switch (option) {
		case MENU: 
			handler.onMenuPressed();
			break;
		case BEST_TIME:
			handler.onBestTimePressed();
			break;
		}
		
	}

	@Override
	public int getElapsedTime() {
		
		return (timeElapsed == -1) ? (int) (System.currentTimeMillis() - timer)
				: timeElapsed;
	}
  
  
}