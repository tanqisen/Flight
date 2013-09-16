package com.wx.game;

import android.graphics.Rect;



public class Hero extends Art {
	public IWeapon weapon = null;

	public Hero() {
		super();
		
		this.artNormalState = new NormalState();
		this.artAttackedState = null;
		this.artBombingState = new BombingState();
		
		this.ATK = 100;
		this.maxHP = 1;
		this.HP = this.maxHP;
	}
	
	class NormalState extends ArtState {
		NormalState() {
			super();
			
			this.sprites.add("hero_fly_1.png");
			this.sprites.add("hero_fly_2.png");
		}
		
		@Override
		void reset() {
			super.reset();
			
			Sprite s = gameContext.getSprite(this.sprites.get(0));
			Rect r = s.textureRect;
			
			int left = gameContext.VLeft() + (gameContext.VWidth()-r.width())/2;
			int top = gameContext.VTop() + gameContext.VHeight() - r.height();
			
			if (spriteFrame == null) {
				spriteFrame = new Rect();
			}
			spriteFrame.set(left, top, left+r.width(), top+r.height());
		}
		
		@Override
		void move(int dx, int dy) {
			// 覆盖父类，确保飞机不会移出屏幕外
			if (spriteFrame.left+dx>gameContext.VLeft() && spriteFrame.right+dx<gameContext.VRight() && 
					spriteFrame.top+dy>gameContext.VTop() && spriteFrame.bottom+dy<gameContext.VBottom()) {
				spriteFrame.offset(dx, dy);
			}
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
		int blink = 8;

		BombingState() {
			super();

			this.sprites.add("hero_blowup_1.png");
			this.sprites.add("hero_blowup_2.png");
			this.sprites.add("hero_blowup_3.png");
			this.sprites.add("hero_blowup_4.png");
		}
		
		@Override
		boolean isCollision(Art a) {
			return false;
		}
		
		@Override
		void beAttacked(int attack) {
		}
		
		@Override
		void move(float deltaTime) {
			
		}
		
		@Override
		void moveTo(int x, int y) {
			
		}
		
		@Override
		String nextTickSprite() {
			if (index >= sprites.size()) {
				if (this.blink < 0) {
					changeState(null);
					return null;
				} else {
					this.blink --;
					return sprite==null ? "hero_fly_1.png" : null;
				}
			} else {
				return sprites.get(index);
			}
		}
	}
}
