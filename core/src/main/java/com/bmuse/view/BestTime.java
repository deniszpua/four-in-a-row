package com.bmuse.view;

import com.bmuse.interfaces.BestTimeView;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.Platform;
import playn.core.Surface;
import playn.core.TextBlock;
import playn.core.TextFormat;
import playn.core.TextWrap;
import playn.core.Texture;
import playn.scene.Layer;
import pythagoras.f.IDimension;

public class BestTime extends Layer implements BestTimeView {
	private String bestTime = "-:--";
	private Platform plat;
	private IDimension viewSize;

	public BestTime(Platform plat, IDimension viewSize) {
		this.plat = plat;
		this.viewSize = viewSize;
		
	}

	@Override
	public void updateBestTime(String newRecord) {
		bestTime = newRecord;
		

	}

	@Override
	protected void paintImpl(Surface surf) {
		TextBlock text = new TextBlock(plat.graphics().layoutText(bestTime, 
				new TextFormat(new Font("Sans", 30)),
				new TextWrap(0.8f * viewSize.width())));
		Canvas canvas = plat.graphics().createCanvas(viewSize);
		canvas.setFillColor(0xFF333333);
		text.fill(canvas, TextBlock.Align.CENTER, 
				(canvas.width - text.bounds.width()) / 2,
				(canvas.height - text.bounds.height()) / 3);
		Texture texture = canvas.toTexture();
		surf.draw(texture, 0f, 0f);
		

	}
	
}
