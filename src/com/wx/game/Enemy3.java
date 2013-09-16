package com.wx.game;

import java.util.Random;

import android.graphics.Rect;


/*
 * ÖÐÐÍµÐ»ú
 */
public class Enemy3 extends Art {
	public Enemy3() {
		super();
		
		this.score = 25;
		
		this.artNormalState   = new NormalState();
		this.artAttackedState = new AtackedState();
		this.artBombingState  = new BombingState();	
	}
	
	public void reset() {
		super.reset();
		
		this.maxHP = 4;
		this.HP = this.maxHP;
		
		this.ATK = 1;
	}
	
	class NormalState extends ArtState {
		Random rand = new Random();
		
		NormalState() {
			super();
		}
		
		@Override
		void reset() {
			Sprite s = gameContext.getSprite("enemy3_fly_1.png");
			Rect r = s.textureRect;
			
			int left = gameContext.VLeft() + rand.nextInt(gameContext.VWidth() - r.width() - 10) + 5;
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
		
		@Override
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
			return "enemy3_fly_1.png";
		}
	}
	
	class AtackedState extends ArtState {
		AtackedState() {
			super();
			
			sprites.add("enemy3_fly_1.png");
			sprites.add("enemy3_hit_1.png");
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

			this.sprites.add("enemy3_blowup_1.png");
			this.sprites.add("enemy3_blowup_2.png");
			this.sprites.add("enemy3_blowup_3.png");
			this.sprites.add("enemy3_blowup_4.png");
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
