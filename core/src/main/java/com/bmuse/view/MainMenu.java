package com.bmuse.view;

import com.bmuse.interfaces.MenuListener;

import playn.core.Surface;
import playn.scene.Layer;

public class MainMenu extends Layer implements com.bmuse.interfaces.MainMenu {
	
	private MenuListener listener;

	@Override
	public void addMenuListener(MenuListener listener) {
		this.listener = listener;

	}

	@Override
	protected void paintImpl(Surface surf) {
		// TODO Auto-generated method stub
		
		

	}

}
