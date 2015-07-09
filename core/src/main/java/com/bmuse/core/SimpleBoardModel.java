package com.bmuse.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SimpleBoardModel implements BoardModel {
	private final int boardWidth;
	private final int boardHeight;
	private final int ballsToWin;
	private GameView view;
	private final Map<Coordinates, Ball> board;
	private Ball turn;


	public SimpleBoardModel(int boardWidth, int boardHeight, int ballsToWin) {
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		this.ballsToWin = ballsToWin;
		board = new HashMap<>(boardHeight * boardWidth);
		//white always moves first
		turn = Ball.WHITE;
	}

	@Override
	public void addBall(Coordinates at) {
		assert(!board.containsKey(at));
	    assert(at.getX() >=0 && at.getX() < boardWidth 
	    		&& at.getY() >= 0 && at.getY() < boardHeight);
	    
	    board.put(at, turn);

	    if (!isGameOver()){
	      switchPlayer();
	      view.showLegalMoves(listLegalMoves(), turn);
	    }
	    else {
	      //TODO save score and exit to main menu
	      System.out.println("Winner is " + turn);
	    }
	    
	    

	}

	@Override
	public void addMoveListener(GameView moveListener) {
		view = moveListener;
		
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public int getBoardHeight() {
		return boardHeight;
	}

	@Override
	public void startNewGame() {
		view.showLegalMoves(listLegalMoves(), turn);
		
	}

	private List<Coordinates> listLegalMoves() {
		List<Coordinates> result = new ArrayList<>(boardWidth);
	    for (int colunm = 0; colunm < boardWidth; colunm++) {
	      for (int row = boardHeight - 1; row >=0; row--) {
	    	  Coordinates current = new Coordinates(colunm, row);
	    	  if(!board.containsKey(current)) {
	    		  result.add(current);
	    		  break;
	    	  }
	      }
		}
		  return result;
	}
	
	  private void switchPlayer() {
		    turn = Ball.values()[(turn.ordinal() + 1) % Ball.values().length];

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
		    if (board.entrySet().size() >= boardHeight * boardWidth) {
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
		    
//		    final int BALLS_TO_WIN = 4;
		    
		    for (Coordinates current : ballsPlaces) {
		      //test horizontal from left to right
		      //check only for columns smaller than 4
		      boolean lineBreaked = false;
		      if (current.getX() <= boardWidth - ballsToWin){
		        for (int offset = 1; offset < ballsToWin; offset++){
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
		        for (int offset = 1; offset < ballsToWin; offset++){
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
		        for (int offset=1; offset < ballsToWin; offset++) {
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
	  
	  public void clearBoard() {
		    for (Coordinates at : board.keySet()) {
		      view.removeBall(at);;
		    }
		    board.clear();
		    turn = Ball.WHITE;
		    
		  }
}
