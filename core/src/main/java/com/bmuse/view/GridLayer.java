package com.bmuse.view;

import com.bmuse.core.BoardModel;
import com.bmuse.core.FourInARow;

import playn.core.Surface;
import playn.scene.Layer;
import pythagoras.f.IDimension;

public class GridLayer extends Layer {
  public static final float LINE_WIDTH = 2;
  public static final float MARGIN = 5;
  
  private final BoardModel game;

  public float cellSize;
  
  /**
   * Constructs layer which display cells grid
   * @param game2 - game model object;
   * @param viewSize - size of view;
   */
  public GridLayer(BoardModel game2, IDimension viewSize) {
    this.game = game2;
    //compute cell size to fit in screen
    cellSize = Math.min((viewSize.height() - 2 * MARGIN) / game.getBoardHeight(), 
        (viewSize.width() - 2 * MARGIN) / game.getBoardWidth());
  }
  
  /**
   * Returns offset of the center of the cell
   * @param cellCount - row or column of the cell
   * @return offset in pixels in x or y
   */
  public float cellOffset(int cellCount) {
    //cell offset from center is 
    return cellCount * cellSize + cellSize / 2 + 1;
  }
  
  @Override
  public float width() {
    return cellSize * game.getBoardWidth() + LINE_WIDTH;
  }

  @Override
  public float height() {
    return cellSize * game.getBoardHeight() + LINE_WIDTH;
  }




  @Override
  protected void paintImpl(Surface surf) {
    //black with full alpha background
    surf.setFillColor(0xFF000000);
    
    float top = 0f, bottom = height(), left = 0f, right = width();
    
    //draw vertical lines
    for (int xx = 0; xx <= game.getBoardWidth(); xx++) {
      float xpos = xx * cellSize + 1;
      surf.drawLine(xpos, top, xpos, bottom, LINE_WIDTH);
    }
    
    //same for horizontal
    for (int yy = 0; yy <= game.getBoardHeight(); yy++) {
      float ypos = yy * cellSize + 1;
      surf.drawLine(left, ypos, right, ypos, LINE_WIDTH);
    }

  }

}
