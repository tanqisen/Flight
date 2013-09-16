package com.wx.game;

import android.graphics.Rect;


/* ÆÕÍ¨×Óµ¯
 * 
 */
public class Bullet1 extends Art {
	public Bullet1() {
		super();
		
		this.artNormalState = new NormalState();
	}
	
	public void reset() {
		super.reset();
		
		this.ATK = 1;
	}
	
	class NormalState extends ArtState {
		void move(int dx, int dy) {
			if (spriteFrame != null) {
				spriteFrame.offset(dx, dy);
				if (spriteFrame.bottom < (gameContext.VTop() + 20)) {
					changeState(null);
				}
			}
		}
		
		@Override
		void reset() {
			Sprite s = gameContext.getSprite("bullet1.png");
			Rect r = s.textureRect;
			
			if (spriteFrame == null) {
				spriteFrame = new Rect();
			}
			spriteFrame.set(0, 0, r.width(), r.height());
			
			speedX = 0;
			speedY = -gameContext.VHeight()*3/2;
		}
		
		@Override
		void beAttacked(int attack) {
			changeState(null);
		}
		
		@Override
		String nextTickSprite() {
			return "bullet1.png";
		}
	}
}
