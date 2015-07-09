package com.bmuse.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import playn.java.LWJGLPlatform;


public class TestSimpleBoardModel {
	private static final BoardModel game = new SimpleBoardModel(7, 6, 4);
	

	@Test
	public void TestIsGameOver() {
		
		//players place ball by turn white first
		//horizontal lowest row
		int[] xPos = {0, 0, 1, 1, 2, 2, 3, 3};
		int[] yPos = {6, 5, 6, 5, 6, 5, 6, 5};
		populateBoard(game, xPos, yPos);
		assertTrue(game.isGameOver());
		
		//vertical leftmost column
		xPos = new int[] {6, 6, 5, 5, 4, 4, 3, 3};
		yPos = new int[] {0, 1, 0, 1, 0, 1, 0, 1};
		populateBoard(game, xPos, yPos);
		assertTrue(game.isGameOver());
		
		//slash from left bottom
		xPos = new int[] {5, 0, 4, 1, 3, 2, 2, 4};
		yPos = new int[] {6, 6, 6, 5, 6, 4, 6, 3};
		populateBoard(game, xPos, yPos);
		assertTrue(game.isGameOver());
		
		//backslash from left upper
		xPos = new int[] {0, 0, 1, 1, 2, 2, 2, 3};
		yPos = new int[] {6, 0, 6, 1, 6, 2, 5, 3};
		populateBoard(game, xPos, yPos);
		assertTrue(game.isGameOver());
		
		//horizontal line in lowest row broken by black ball
		xPos = new int[] {0, 0, 1, 1, 2, 2, 3, 3, 4, 4};
		yPos = new int[] {6, 5, 6, 5, 6, 5, 5, 6, 6, 5};
		populateBoard(game, xPos, yPos);
		assertFalse(game.isGameOver());
		
		//broken vertical line in leftmost column
		xPos = new int[] {0, 1, 0, 1, 0, 1, 1, 0, 0, 1};
		yPos = new int[] {6, 6, 5, 5, 4, 4, 3, 3, 2, 2};
		populateBoard(game, xPos, yPos);
		assertFalse(game.isGameOver());
		
		//broken slash line
		xPos = new int[] {0, 1, 1, 2, 2, 1, 3, 2, 3, 3, 3, 0};
		yPos = new int[] {6, 6, 5, 6, 5, 4, 6, 4, 5, 4, 3, 5};
		populateBoard(game, xPos, yPos);
		assertFalse(game.isGameOver());
		
		//broken backslash line
		xPos = new int[] {0, 0, 0, 0, 1, 1, 2, 1, 2, 1};
		yPos = new int[] {6, 5, 4, 3, 6, 5, 6, 4, 5, 3};
		populateBoard(game, xPos, yPos);
		assertFalse(game.isGameOver());
		
		

		
	}

	private static void populateBoard(BoardModel board, int[] ballXPos, int[] ballYPos) {
		board.clearBoard();
		for (int i = 0; i < ballXPos.length / 2; i++){
			board.addBall(new Coordinates(ballXPos[i * 2], ballYPos[i * 2]));
			board.addBall(new Coordinates(ballXPos[i * 2 + 1], ballYPos[i * 2 + 1]));
		}
	}
	
	@Test
	public void testPossibleMoves() {

		int[] xPos = {0, 1, 2, 4};
		int[] yPos = {5, 5, 5, 5};
		populateBoard(game, xPos, yPos);
		List<Coordinates> got = game.listLegalMoves();
		List<Coordinates> expected = Arrays.asList(
				new Coordinates(0, 4),
				new Coordinates(1, 4),
				new Coordinates(2, 4),
				new Coordinates(3, 5),
				new Coordinates(4, 4),
				new Coordinates(5, 5),
				new Coordinates(6, 5)
				);
		
		assertTrue(got.equals(expected));
		
		
	}

}
