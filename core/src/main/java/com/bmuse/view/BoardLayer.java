package com.bmuse.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bmuse.core.Coordinates;
import com.bmuse.core.FourInARow;
import com.bmuse.core.GameView;

import playn.core.Canvas;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import pythagoras.f.IDimension;

public class BoardLayer extends GroupLayer implements GameView {
  public static final int STROKE_WIDTH = 2;
  public static final int BLACK_COLOR = 0xFF000000;
  public static final int WHITE_COLOR = 0xFFFFFFFF;
  
  private final FourInARow game;
  private final GridLayer boardGrid;
  private final GroupLayer balls;
  private final Map<Coordinates, ImageLayer> ballViews = 
      new HashMap<>(FourInARow.BOARD_HEIGHT * FourInARow.BOARD_WIDTH);
  private final Tile[] ballTiles = new Tile[FourInARow.Ball.values().length];
  private final List<Coordinates> possibleMoves = new ArrayList<>(FourInARow.BOARD_WIDTH);
  
  /**
   * Constructor
   * @param game - reference to game model instance
   */
  public BoardLayer(FourInARow game, IDimension viewSize) {
    this.game = game;
    boardGrid = new GridLayer(game, viewSize);
    addCenterAt(boardGrid, viewSize.width() / 2, viewSize.height() / 2);

    balls = new GroupLayer();
    addAt(balls, boardGrid.tx(), boardGrid.ty());
    
    //create two balls images from canvas
    float size = boardGrid.cellSize - 2;
    float hsize = size / 2;
    
    Canvas canvas = game.plat.graphics().createCanvas(2 * size, size);
    
    canvas
    .setFillColor(BLACK_COLOR).fillCircle(hsize, hsize, hsize)
    .setStrokeColor(WHITE_COLOR).setStrokeWidth(STROKE_WIDTH).strokeCircle(hsize, hsize, hsize);
    
    canvas
    .setFillColor(WHITE_COLOR).fillCircle(size + hsize, hsize, hsize)
    .setStrokeColor(BLACK_COLOR).setStrokeWidth(STROKE_WIDTH)
    .strokeCircle(size + hsize, hsize, hsize);
    
    //convert image to texture and extract each region (tile)
    Texture texture = canvas.toTexture(Texture.Config.UNMANAGED);
    ballTiles[FourInARow.Ball.BLACK.ordinal()] = texture.tile(0, 0, size, size);
    ballTiles[FourInARow.Ball.WHITE.ordinal()] = texture.tile(size, 0, size, size);
    
    // dispose our pieces texture when this layer is disposed
    onDisposed(texture.disposeSlot());
      
    //subscribe to moves
    game.addMoveListener(this);

  }
  

  @Override
  public void placeBall(Coordinates at, FourInARow.Ball ballColor) {
    ImageLayer ballPic = new ImageLayer(ballTiles[ballColor.ordinal()]);
    ballPic.setOrigin(Layer.Origin.CENTER);
    balls.addAt(ballPic, boardGrid.cellOffset(at.getX()), boardGrid.cellOffset(at.getY()));
    ballViews.put(at, ballPic);
  }


  

  @Override
  public void removeBall(Coordinates at) {
    ImageLayer ball = ballViews.remove(at);
    if (ball != null) {
      ball.close();
    }
  }

@Override
public void showLegalMoves(List<Coordinates> moves, FourInARow.Ball color) {
	possibleMoves.addAll(moves);
	for (Coordinates at : moves) {
		placeBall(at, color);
	}
	
	for (Entry<Coordinates, ImageLayer> entry : ballViews.entrySet()) {
		if (moves.contains(entry.getKey())) {
			entry.getValue().setAlpha(0.3f);
		}
	}
	
}


}
