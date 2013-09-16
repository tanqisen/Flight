package com.wx.game;

import java.util.Random;

import android.graphics.Rect;


/*
 * ´óÐÍµÐ»ú
 */
public class Enemy2 extends Art {
	public Enemy2() {
		super();
		
		this.score = 100;
		
		this.artNormalState   = new NormalState();
		this.artAttackedState = new AtackedState();
		this.artBombingState  = new BombingState();	
	}
	
	public void reset() {
		super.reset();
		
		this.maxHP = 10;
		this.HP = this.maxHP;
		
		this.ATK = 1;
	}
	
	class NormalState extends ArtState {
		Random rand = new Random();
		
		NormalState() {
			super();
			
			this.sprites.add("enemy2_fly_1.png");
			this.sprites.add("enemy2_fly_2.png");
		}

		@Override
		void reset() {
			super.reset();
			
			Sprite s = gameContext.getSprite("enemy2_fly_1.png");
			Rect r = s.textureRect;
			
			int left = gameContext.VLeft() + rand.nextInt(gameContext.VWidth() - r.width());
			int top = gameContext.VTop() - r.height();
			
			if (spriteFrame == null) {
				spriteFrame = new Rect();
			}
			spriteFrame.set(left, top, left+r.width(), top+r.height());
			
			//
			speedX = 0;
			
			float tmp = (float) (gameContext.VHeight()/10.0);
			speedY = (int) (tmp+rand.nextInt(gameContext.VHeight()/10));
		}
		
		void move(int dx, int dy) {
			speedY += 5;
			super.move(dx, dy);
		}
		
		void beAttacked(int attack) {
			HP -= attack;
			if (HP <= 0) {
				changeState(artBombingState);
			} else {
				changeState(artAttackedState);
			}
		}
		
		@Override
		String nextTickSprite() {
			return sprites.get(tickCount%sprites.size());
		}
	}
	
	class AtackedState extends ArtState {
		AtackedState() {
			super();
			
			sprites.add("enemy2_fly_1.png");
			sprites.add("enemy2_hit_1.png");
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
			return sprites.get(tickCount%sprites.size());
		}
	}
	
	class BombingState extends ArtState {
		BombingState() {
			super();
			
			this.sprites.add("enemy2_blowup_1.png");
			this.sprites.add("enemy2_blowup_2.png");
			this.sprites.add("enemy2_blowup_3.png");
			this.sprites.add("enemy2_blowup_4.png");
			this.sprites.add("enemy2_blowup_5.png");
			this.sprites.add("enemy2_blowup_6.png");
			this.sprites.add("enemy2_blowup_7.png");
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
