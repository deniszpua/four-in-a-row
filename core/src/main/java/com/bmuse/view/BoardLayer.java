package com.bmuse.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmuse.core.Ball;
import com.bmuse.core.BoardModel;
import com.bmuse.core.Coordinates;
import com.bmuse.core.GameView;

import playn.core.Canvas;
import playn.core.Platform;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import playn.scene.Pointer;
import pythagoras.f.IDimension;

public class BoardLayer extends GroupLayer implements GameView {
  public static final int STROKE_WIDTH = 2;
  public static final int BLACK_COLOR = 0xFF000000;
  public static final int WHITE_COLOR = 0xFFFFFFFF;
  
  private final BoardModel game;
  private final GridLayer boardGrid;
  private final GroupLayer balls;
  private final Map<Coordinates, ImageLayer> ballViews = new HashMap<>();
  private final Tile[] ballTiles = new Tile[Ball.values().length];
  
  /**
   * Constructor
   * @param game - reference to game model instance
   */
  public BoardLayer(BoardModel game, IDimension viewSize, Platform plat) {
    this.game = game;
    boardGrid = new GridLayer(game, viewSize);
    addCenterAt(boardGrid, viewSize.width() / 2, viewSize.height() / 2);

    balls = new GroupLayer();
    addAt(balls, boardGrid.tx(), boardGrid.ty());
    
    //create two balls images from canvas
    float size = boardGrid.cellSize - 2;
    float hsize = size / 2;
    
    Canvas canvas = plat.graphics().createCanvas(2 * size, size);
    
    canvas
    .setFillColor(BLACK_COLOR).fillCircle(hsize, hsize, hsize)
    .setStrokeColor(WHITE_COLOR).setStrokeWidth(STROKE_WIDTH).strokeCircle(hsize, hsize, hsize);
    
    canvas
    .setFillColor(WHITE_COLOR).fillCircle(size + hsize, hsize, hsize)
    .setStrokeColor(BLACK_COLOR).setStrokeWidth(STROKE_WIDTH)
    .strokeCircle(size + hsize, hsize, hsize);
    
    //convert image to texture and extract each region (tile)
    Texture texture = canvas.toTexture(Texture.Config.UNMANAGED);
    ballTiles[Ball.BLACK.ordinal()] = texture.tile(0, 0, size, size);
    ballTiles[Ball.WHITE.ordinal()] = texture.tile(size, 0, size, size);
    
    // dispose our pieces texture when this layer is disposed
    onDisposed(texture.disposeSlot());
      
    //subscribe to moves
    game.addMoveListener(this);

  }
  

  private ImageLayer placeBall(Coordinates at, Ball ballColor) {
    ImageLayer ballPic = new ImageLayer(ballTiles[ballColor.ordinal()]);
    ballPic.setOrigin(Layer.Origin.CENTER);
    balls.addAt(ballPic, boardGrid.cellOffset(at.getX()), boardGrid.cellOffset(at.getY()));
    ballViews.put(at, ballPic);
    return ballPic;
  }


  

  @Override
  public void removeBall(Coordinates at) {
    ImageLayer ball = ballViews.remove(at);
    if (ball != null) {
      ball.close();
    }
  }

@Override
public void showLegalMoves(List<Coordinates> moves, Ball color) {
	
	final List<ImageLayer> possibleMovesPics = new ArrayList<>();
	for (final Coordinates coord : moves) {
		final ImageLayer ballPic = placeBall(coord, color);
		ballPic.setAlpha(0.3f);
		//add listener (touch/click on possible move picture confirms move)
		ballPic.events().connect(new Pointer.Listener() {

			@Override
			public void onStart(Pointer.Interaction iact) {
				for (ImageLayer image : possibleMovesPics) {
					if (!image.equals(ballPic)) {
						image.close();
					}
					else {
						image.setAlpha(1.0f);
						game.addBall(coord);
					}
				}
				
			}
			
			}
		);
		possibleMovesPics.add(ballPic);
		}
	}
	
	
}
