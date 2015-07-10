package com.bmuse.view;

import com.bmuse.interfaces.MainMenuOptions;
import com.bmuse.interfaces.MenuListener;
import com.bmuse.resources.Context;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.Platform;
import playn.core.TextBlock;
import playn.core.TextFormat;
import playn.core.TextWrap;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Pointer;
import playn.scene.Pointer.Interaction;
import pythagoras.f.IDimension;

public class MainMenu extends GroupLayer implements com.bmuse.interfaces.MainMenu {
	
	private final MenuListener listener;
	private IDimension viewSize;
	private Platform plat;

	public MainMenu(IDimension viewSize, Platform plat, MenuListener listener) {
		this.viewSize = viewSize;
		this.plat = plat;
		this.listener = listener;
		init();
	}

	protected void init() {
		float leftMargin = viewSize.width() / 4;
		float topMarginUpper = viewSize.height() / 8, topMarginLower = 5 * topMarginUpper;
		
		//draw "New Game" button
		ImageLayer newGameButton = createButton("New Game");
		newGameButton.events().connect(new Pointer.Listener() {

			@Override
			public void onStart(Interaction iact) {
				listener.optionChoosed(MainMenuOptions.NEW_GAME);;
				super.onStart(iact);
			}
		});
		this.addFloorAt(newGameButton, leftMargin, topMarginUpper);
		
		ImageLayer bestScoreButton = createButton("Best Time");
		bestScoreButton.events().connect(new Pointer.Listener() {

			@Override
			public void onStart(Interaction iact) {
				listener.optionChoosed(MainMenuOptions.BEST_TIME);
				super.onStart(iact);
			}
			
		});
		this.addFloorAt(bestScoreButton, leftMargin, topMarginLower);

	}
	
	private ImageLayer createButton(String buttonText) {
		float width = viewSize.width() / 2, height = viewSize.height()/4;
		
		Canvas canvas = plat.graphics().createCanvas(width, height);
		canvas.setFillColor(Context.BUTTONS_BG_COLOR);
		canvas.fillRect(0, 0, width, height);
		
		TextBlock textLabel = new TextBlock(plat.graphics().layoutText(buttonText,
				new TextFormat(new Font("Sans", 22)), new TextWrap(viewSize.width()/2)));
		canvas.setFillColor(Context.BUTTONS_TEXT_COLOR);
		textLabel.fill(canvas, TextBlock.Align.CENTER, 
				(width - textLabel.bounds.width()) / 2,
				(height - textLabel.bounds.height()) / 2);
		ImageLayer buttonImage = new ImageLayer(canvas.toTexture());
		
		return buttonImage;
	}

}
