package com.wx.game;


import android.graphics.Rect;

public class BombButton extends Art {
	public BombButton() {
		super();
		
		this.sprite = "bomb.png";
		this.spriteFrame = new Rect();
	}
	
	public void reset() {
		Sprite s = this.gameContext.getSprite("bomb.png");
		Rect r = s.textureRect;
		this.spriteFrame.set(0, 0, r.width(), r.height());
		this.spriteFrame.offset(this.gameContext.VLeft() + 20, this.gameContext.VBottom()-r.height()-20);
	}
	
	public void draw(float deltaTime) {
		this.displaySprite();
	}
}
