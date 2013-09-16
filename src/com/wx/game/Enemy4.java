package com.wx.game;

import android.graphics.Rect;


/*
 * Õ¨µ¯½µÂäÉ¡
 */
public class Enemy4 extends Art {
	public Enemy4() {
		super();
		
		this.artNormalState  = new NormalState();
		this.artBombingState = new BombingState();	
	}
	
	public void reset() {
		super.reset();
		
		this.maxHP = 1;
		this.HP = this.maxHP;
		
		this.ATK = 0;
	}
	
	class NormalState extends ArtState {
		boolean down1 = true;
		boolean up1 = false;
		boolean down2 = false;

		public NormalState() {
			super();
		}

		@Override
		void reset() {
			super.reset();
			
			Sprite s = gameContext.getSprite("enemy4_fly_1.png");
			Rect r = s.textureRect;
			
			int random = (int) (Math.random()*(gameContext.VWidth() - r.width()-10) + 20);
			int left = gameContext.VLeft() + random;
			int top = gameContext.VTop() - r.height();
			
			if (spriteFrame == null) {
				spriteFrame = new Rect();
			}
			spriteFrame.set(left, top, left+r.width(), top+r.height());
			
			//
			speedX = 0;
			speedY = 150;
		}
		
		@Override
		void move(float deltaTime) {
			if (down1) {
				speedY -= 10;
				if (speedY <= 0) {
					down1 = false;
					up1 = true;
					speedY = -200;
				}
			}
			
			if (up1) {
				speedY += 5;
				if (spriteFrame.top <= gameContext.VTop()) {
					up1 = false;
					down2 = true;
					speedY = 300;
				}
			}
			
			if (down2) {
				speedY += 10;
			}
			
			super.move(deltaTime);
		}
		
		@Override
		void beAttacked(int attack) {
			HP -= attack;
			if (HP <= 0) {
				changeState(artBombingState);
			}
		}
		
		@Override
		String nextTickSprite() {
			return "enemy4_fly_1.png";
		}
	}
	
	class BombingState extends ArtState {
		int blink = 4;
		
		BombingState() {
			super();
		}
		
		@Override
		void move(float deltaTime) {
		}
		
		@Override
		void beAttacked(int attack) {
		}
		
		@Override
		String nextTickSprite() {
			blink--;
			if (blink < 0) {
				changeState(null);
				return null;
			}
			return (sprite==null) ? "enemy4_fly_1.png" : null;
		}
	}
}
