package com.bmuse.core;

import static com.bmuse.resources.Context.BALLS_TO_WIN;
import static com.bmuse.resources.Context.BG_COLOR;
import static com.bmuse.resources.Context.BOARD_HEIGHT;
import static com.bmuse.resources.Context.BOARD_WIDTH;

import com.bmuse.interfaces.BoardModel;
import com.bmuse.interfaces.GameExitHandler;
import com.bmuse.interfaces.MainMenuOptions;
import com.bmuse.interfaces.MenuListener;
import com.bmuse.model.SimpleBoardModel;
import com.bmuse.view.BestTime;
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
  private final BestTime bestTimeView;
  
  public final Pointer pointer;

  public FourInARow(Platform plat) {
    // update our "simulation" 33ms (30 times per second)
    super(plat, 33); 
    
    // Getting window size
    final IDimension viewSize = plat.graphics().viewSize;
    
    // instantiate game model
    
    boardModel = createSimpleBoard();
    
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
    
    // add best time layer
    bestTimeView = new BestTime(plat, viewSize);
    bestTimeView.setVisible(false);
    rootLayer.add(bestTimeView);
    
    // add main menu
    mainMenuView = new MainMenu(viewSize, plat,
    		new MenuListener() {
				
				@Override
				public void optionChoosed(MainMenuOptions choice) {
					mainMenuView.setVisible(false);
					switch(choice) {
					case NEW_GAME:
						boardLayer.setVisible(true);
						boardModel.startNewGame();
						break;
					case BEST_TIME:
						bestTimeView.setVisible(true);
					}
						
					
				}
			});
    rootLayer.add(mainMenuView);
    
  }

private SimpleBoardModel createSimpleBoard() {
	return new SimpleBoardModel(new GameExitHandler() {
		
		@Override
		public void onMenuPressed() {
			mainMenuView.setVisible(true);
			boardLayer.setVisible(false);
			boardModel.clearBoard();
			
		}
		
		@Override
		public void onGameFinished(long timeElapsed) {
			bestTimeView.updateBestTime(timeElapsed);
			
		}
		
		@Override
		public void onBestTimePressed() {
			bestTimeView.setVisible(true);
			boardLayer.setVisible(false);
			
		}
	});
}
  
  
}
