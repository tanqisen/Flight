package com.wx.game;

import java.util.Random;

import android.graphics.Rect;


/*
 * Ð¡ÐÍµÐ»ú
 */
public class Enemy1 extends Art {
	public Enemy1() {
		super();
		
		this.score = 10;
		
		this.artNormalState = new NormalState();
		this.artBombingState = new BombingState();
	}
	
	public void reset() {
		super.reset();
		
		this.maxHP = 1;
		this.HP = this.maxHP;
		
		this.ATK = 1;
	}
	
	class NormalState extends ArtState {
		Random rand = new Random();
		
		@Override
		void reset() {
			super.reset();
			
			Sprite s = gameContext.getSprite("enemy1_fly_1.png");
			Rect r = s.textureRect;
			
			
			int left = gameContext.VLeft() + rand.nextInt(gameContext.VWidth() - r.width() - 20) + 10;
			int top = gameContext.VTop() - r.height();
			
			if (spriteFrame == null) {
				spriteFrame = new Rect();
			}
			spriteFrame.set(left, top, left+r.width(), top+r.height());
			
			//
			speedX = 0;
			
			float tmp = (float) (gameContext.VHeight()/10.0);
			speedY = (int) (tmp+rand.nextInt(gameContext.VHeight()/8));
		}
		
		void move(int dx, int dy) {
			speedY += 5;
			super.move(dx, dy);
		}
		
		void beAttacked(int attack) {
			HP -= attack;
			if (HP <= 0) {
				changeState(artBombingState);
			}
		}
		
		@Override
		String nextTickSprite() {
			return "enemy1_fly_1.png";
		}
	}
	
	class BombingState extends ArtState {
		BombingState() {
			super();
			
			this.sprites.add("enemy1_blowup_1.png");
			this.sprites.add("enemy1_blowup_2.png");
			this.sprites.add("enemy1_blowup_3.png");
			this.sprites.add("enemy1_blowup_4.png");
		}
		
		@Override
		void move(float deltaTime) {
			
		}
		
		@Override
		boolean isCollision(Art a) {
			return false;
		}
		
		@Override
		void beAttacked(int attack) {
		}
		
		@Override
		String nextTickSprite() {
			if (index >= sprites.size()) {
				changeState(null);
				return null;
			} else {
				return sprites.get(index);
			}
		}
	}
}
