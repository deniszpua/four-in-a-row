package com.bmuse.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmuse.interfaces.BoardModel;
import com.bmuse.interfaces.GameView;
import com.bmuse.model.Ball;
import com.bmuse.model.Coordinates;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.Platform;
import playn.core.TextBlock;
import playn.core.TextFormat;
import playn.core.TextWrap;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import playn.scene.Pointer;
import pythagoras.f.IDimension;

public class BoardLayer extends GroupLayer implements GameView {
  public static final int STROKE_WIDTH = 2;
  public static final int FIRST_PLAYER_COLOR = 0xFFFF6600;//orange
  public static final int SECOND_PLAYER_COLOR = 0xFF0066FF;//light blue
  public static final int LABEL_TEXT_COLOR = 0xFFCCCCCC; //light grey
  
  private final BoardModel game;
  private final IDimension viewSize;
  private final Platform plat;
  private final GridLayer boardGrid;
  private final GroupLayer balls;
  private final Map<Coordinates, ImageLayer> ballViews = new HashMap<>();
  private final Tile[] ballTiles = new Tile[Ball.values().length];
  private ImageLayer turnLabel;
  private Map<Ball, ImageLayer> turnLabelsCache = new HashMap<>(Ball.values().length);
  
  /**
   * Constructor
   * @param game - reference to game model instance
   */
  public BoardLayer(BoardModel game, IDimension viewSize, Platform plat) {
    this.game = game;
    this.viewSize = viewSize;
    this.plat = plat;
    boardGrid = new GridLayer(game, viewSize);
    addCenterAt(boardGrid, viewSize.width() / 2, viewSize.height() / 2);

    balls = new GroupLayer();
    addAt(balls, boardGrid.tx(), boardGrid.ty());
    
    //create two balls images from canvas
    float size = (boardGrid.cellSize - 2) * 0.8f;
    float hsize = size / 2;
    
    Canvas canvas = plat.graphics().createCanvas(2 * size, size);
    
    canvas
    .setFillColor(SECOND_PLAYER_COLOR).fillCircle(hsize, hsize, hsize);
    
    canvas
    .setFillColor(FIRST_PLAYER_COLOR).fillCircle(size + hsize, hsize, hsize);
    
    //convert image to texture and extract each region (tile)
    Texture texture = canvas.toTexture(Texture.Config.UNMANAGED);
    ballTiles[Ball.BLUE.ordinal()] = texture.tile(0, 0, size, size);
    ballTiles[Ball.ORANGE.ordinal()] = texture.tile(size, 0, size, size);
    
    
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
public void processNextMove(List<Coordinates> moves, Ball color) {
  // Create textLabel with current player color;
  changeTurnLabel(color);
	
	
  // Show possible move positions
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

  private void changeTurnLabel(Ball color) {
	  if (turnLabel != null) {
		  turnLabel.setVisible(false);
	  }
	  if (turnLabelsCache.containsKey(color)) {
		  turnLabel = turnLabelsCache.get(color);
		  turnLabel.setVisible(true);
		  
	  } else {
		  TextBlock label = new TextBlock(plat.graphics().layoutText(color.toString(),
				  new TextFormat(new Font("Sans", 18)),
				  new TextWrap(viewSize.width()/2)));
		  Canvas canvas = plat.graphics().createCanvas(label.bounds.width() + 2 * STROKE_WIDTH,
				  label.bounds.height() + 2 * STROKE_WIDTH);
		  canvas.setFillColor(LABEL_TEXT_COLOR);
		  label.fill(canvas, TextBlock.Align.CENTER, 0, 0);
		  turnLabel = new ImageLayer(canvas.toTexture());
		  turnLabelsCache.put(color, turnLabel);
		  this.addFloorAt(turnLabel, (viewSize.width() - turnLabel.width())/2, //center horizontally
				  ((viewSize.height() - boardGrid.height())/2 - turnLabel.height())/2); //and vertically
	  }
	  
  }
  
  
  
}
