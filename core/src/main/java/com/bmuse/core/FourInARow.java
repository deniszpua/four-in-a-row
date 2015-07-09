package com.bmuse.core;

import static com.bmuse.resources.Context.BALLS_TO_WIN;
import static com.bmuse.resources.Context.BG_COLOR;
import static com.bmuse.resources.Context.BOARD_HEIGHT;
import static com.bmuse.resources.Context.BOARD_WIDTH;

import com.bmuse.interfaces.BoardModel;
import com.bmuse.interfaces.MainMenuOptions;
import com.bmuse.interfaces.MenuListener;
import com.bmuse.model.SimpleBoardModel;
import com.bmuse.view.BoardLayer;
import com.bmuse.view.MainMenu;

import playn.core.Platform;
import playn.core.Surface;
import playn.scene.Layer;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;

public class FourInARow extends SceneGame {
  
  
  private final BoardModel boardModel;
  private final BoardLayer boardLayer;
  private final com.bmuse.view.MainMenu mainMenuView;
  
  public final Pointer pointer;

  public FourInARow(Platform plat) {
    // update our "simulation" 33ms (30 times per second)
    super(plat, 33); 
    
    // Getting window size
    final IDimension viewSize = plat.graphics().viewSize;
    
    // instantiate game model
//    boardModel = new SimpleBoardModel(BOARD_WIDTH, BOARD_HEIGHT, BALLS_TO_WIN);
    boardModel = new SimpleBoardModel(BOARD_WIDTH,
    		BOARD_HEIGHT, BALLS_TO_WIN);
    
    // Wire up pointer and mouse events
    pointer = new Pointer(plat, rootLayer, false);
    plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, false));
    
    // Fill background
    rootLayer.add(new Layer(){
      @Override
      protected void paintImpl(Surface surf) {
        surf.setFillColor(BG_COLOR)
          .fillRect(0, 0, viewSize.width(), viewSize.height());
      }
    });
    
    // add game board and set it invisible
    boardLayer = new BoardLayer(boardModel, viewSize, plat);
    boardLayer.setVisible(false);
    rootLayer.add(boardLayer);
    
    // add main menu
    mainMenuView = new MainMenu(viewSize, plat,
    		new MenuListener() {
				
				@Override
				public void optionChoosed(MainMenuOptions choice) {
					switch(choice) {
					case NEW_GAME:
						mainMenuView.setVisible(false);
						boardLayer.setVisible(true);
						boardModel.startNewGame();
						break;
					case BEST_TIME:
						System.out.println("Best Time Choosed");
						break;
					}
						
					
				}
			});
    rootLayer.add(mainMenuView);
    
    
    
//    boardModel.startNewGame();
    
  }
  
  
}
