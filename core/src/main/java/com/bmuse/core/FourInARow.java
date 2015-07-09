package com.bmuse.core;

import java.util.Map;

import com.bmuse.view.BoardLayer;

import playn.core.Platform;
import playn.core.Surface;
import playn.scene.Layer;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;

public class FourInARow extends SceneGame {
  
  private static final int BOARD_WIDTH = 7;
  private static final int BOARD_HEIGHT = 6;
  private static final int BALLS_TO_WIN = 4;
  
  public final Pointer pointer;
  private final BoardModel boardModel;

  public FourInARow(Platform plat) {
    // update our "simulation" 33ms (30 times per second)
    super(plat, 33); 

    // Getting window size
    final IDimension viewSize = plat.graphics().viewSize;
    
    // instantiate game model
    boardModel = new SimpleBoardModel(BOARD_WIDTH, BOARD_HEIGHT, BALLS_TO_WIN);
    
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
    rootLayer.add(new BoardLayer(boardModel, viewSize, plat));
    
    boardModel.startNewGame();
    
  }
  
  
}
