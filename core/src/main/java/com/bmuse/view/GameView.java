package com.bmuse.view;

import java.util.HashMap;
import java.util.Map;

import com.bmuse.core.Coordinates;
import com.bmuse.core.FourInARow;
import com.bmuse.core.FourInARow.Ball;
import com.bmuse.core.MoveListener;

import playn.core.Canvas;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import pythagoras.f.IDimension;

public class GameView extends GroupLayer implements MoveListener {
  public static final int STROKE_WIDTH = 2;
  public static final int BLACK_COLOR = 0xFF000000;
  public static final int WHITE_COLOR = 0xFFFFFFFF;
  
  private final FourInARow game;
  private final BoardView boardView;
  private final GroupLayer balls;
  private final Map<Coordinates, ImageLayer> ballViews = 
      new HashMap<>(FourInARow.BOARD_HEIGHT * FourInARow.BOARD_WIDTH);
  private final Tile[] ballTiles = new Tile[FourInARow.Ball.values().length];
  
  /**
   * Constructor
   * @param game - reference to game model instance
   */
  public GameView(FourInARow game, IDimension viewSize) {
    this.game = game;
    boardView = new BoardView(game, viewSize);
    addCenterAt(boardView, viewSize.width() / 2, viewSize.height() / 2);

    balls = new GroupLayer();
    addAt(balls, boardView.tx(), boardView.ty());
    
    //create two balls images from canvas
    float size = boardView.cellSize - 2;
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
  
  /**
   * Put ball of specified color in specified cell
   */
  public void putBallAt(Coordinates at, FourInARow.Ball ballColor) {
    ImageLayer ballView = new ImageLayer(ballTiles[ballColor.ordinal()]);
    ballView.setOrigin(Layer.Origin.CENTER);
    balls.addAt(ballView, boardView.cellOffset(at.getX()), boardView.cellOffset(at.getY()));
    ballViews.put(at, ballView);
  }

  @Override
  public void ballAddedAt(Coordinates at, Ball color) {
    putBallAt(at, color);
    
    
  }
  
  /**
   * Remove ball at given position.
   */
  public void removeBall(Coordinates at) {
    ImageLayer ball = ballViews.remove(at);
    if (ball != null) {
      ball.close();
    }
  }

  @Override
  public void ballRemoved(Coordinates at) {
    removeBall(at);
    
  }
  
  

}
