package com.wx.game;

import android.graphics.Rect;


/* Ë«±¶×Óµ¯
 * 
 */
public class Bullet2 extends Art {
	public Bullet2() {
		super();
		
		this.artNormalState = new NormalState();
	}
	
	public void reset() {
		super.reset();
		
		this.ATK = 2;
	}
	
	class NormalState extends ArtState {
		void move(int dx, int dy) {
			speedY -= 2;
			
			if (spriteFrame != null) {
				spriteFrame.offset(dx, dy);
				if (spriteFrame.bottom < (gameContext.VTop() + 20)) {
					changeState(null);
				}
			}
		}
		
		@Override
		void reset() {
			Sprite s = gameContext.getSprite("bullet2.png");
			Rect r = s.textureRect;
			
			if (spriteFrame == null) {
				spriteFrame = new Rect();
			}
			spriteFrame.set(0, 0, r.width(), r.height());
			
			speedX = 0;
			speedY = -gameContext.VHeight()*3/2;
		}
		
		void beAttacked(int attack) {
			changeState(null);
		}
		
		@Override
		String nextTickSprite() {
			return "bullet2.png";
		}
	}
}
