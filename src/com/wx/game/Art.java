package com.wx.game;
import java.util.ArrayList;

import android.graphics.Rect;

public class Art {
	public Rect spriteFrame = null;
	
	public int speedX = 0;
	public int speedY = 0;
	
	public int maxHP = 0;
	public int HP = 0;
	
	public int ATK = 0;
	
	public String sprite = null;
	
	public ArtState artState = null;
	public ArtState artNormalState = null;
	public ArtState artBombingState = null;
	public ArtState artAttackedState = null;
	
	public ArtFactory artFactor = null;
	
	public GameContext gameContext = null;
	
	public int score = 0;
	
	/* 重置角色状态，如血量、速度等。用于对`死亡`角色的重用
	 * 
	 */
	public void reset() {
		// 初始化顺序会影响角色第一次出现时的贴图
		if (this.artBombingState != null) {
			this.artBombingState.reset();
		}
		
		if (this.artAttackedState != null) {
			this.artAttackedState.reset();
		}
		
		if (this.artNormalState != null) {
			this.artNormalState.reset();
		}
		
		this.artState = this.artNormalState;
		
		this.HP = this.maxHP;
	}
	
	/* 回收对象，以便重用
	 * 
	 */
	public void reuse() {
		if (artFactor != null) {
			artFactor.reuseArt(this);
		}
	}
	
	/* 角色当前在地图中的位置
	 * 
	 */
	public Rect spriteFrame() {
		return this.spriteFrame;
	}
	
	/* 移动角色一段距离
	 * param:dx 水平偏移
	 * param:dy 垂直偏移
	 */
	public void move(int dx, int dy) {
		if (this.artState != null) {
			this.artState.move(dx, dy);
		}
	}
	
	/* 移动角色
	 * 
	 */
	public void move(float deltaTime) {
		if (this.artState != null) {
			this.artState.move(deltaTime);
		}
	}
	
	/* 移动到指定位置
	 * 
	 */
	void moveTo(int x, int y) {
		if (this.artState != null) {
			this.artState.moveTo(x, y);
		}
	}
	
	/* 是否与角色a碰撞
	 * 
	 */
	public boolean isCollision(Art a) {
		return (this.artState != null && this.artState.isCollision(a));
	}
	
	/*  被角色a攻击
	 * 
	 */
	public void beAttacked(Art a) {
		beAttacked(a.ATK);
	}
	
	/* 被attack大小的攻击力攻击
	 * 
	 */
	public void beAttacked(int attack) {
		if (this.artState != null) {
			this.artState.beAttacked(attack);
		}
	}
	
	void changeState(ArtState s) {
		this.artState = s;
	}
	
	public boolean isNormalState() {
		return (this.artState != null && this.artState == this.artNormalState);
	}
	
	public boolean isAttackingState() {
		return (this.artState != null && this.artState == this.artAttackedState);
	}
	
	public boolean isBombingState() {
		return (this.artState != null && this.artState == this.artBombingState);
	}
	
	public boolean isDestroyState() {
		return (this.artState == null);
	}
	
	public void draw(float deltaTime) {
		if (this.artState != null) {
			this.artState.draw(deltaTime);
		}
	}
	
	void displaySprite() {
		if (this.sprite != null) {
			this.gameContext.displayArt(this);
		}
	}
	
	/* 角色状态基类
	 * 
	 */
	class ArtState {
		ArrayList<String> sprites = new ArrayList<String>(); /* 角色当前状态的贴图集合 */
		int index = 0;     /* sprites数组索引 */
		int tickCount = 0; /* 执行次数 */
		float tick = 0.1f; /* 多长时间执行一次 */
		float time = 0.0f; /* 执行时间 */
		
		/* 重置当前状态
		 *  
		 */
		void reset() {
			index = 0;
			tickCount = 0;
			time = 0.0f;
			tick = 0.1f;
			
			sprite = nextTickSprite();
		}
		
		void move(int dx, int dy) {
			if (spriteFrame != null) {
				spriteFrame.offset(dx, dy);
				if (spriteFrame.top > gameContext.VBottom()) {
					changeState(null); // 角色移动到屏幕底部，默认销毁角色（子弹、英雄需重新实现）
				}
			}
		}
		
		void moveTo(int x, int y) {
			if (spriteFrame != null) {
				spriteFrame.offsetTo(x, y);
			}
		}

		void move(float deltaTime) {
			this.move((int)(speedX*deltaTime), (int)(speedY*deltaTime));
		}
		
		boolean isCollision(Art a) {
			if (spriteFrame == null) {
				return false;
			}
			
			return Rect.intersects(spriteFrame, a.spriteFrame);
		}
		
		void beAttacked(Art a) {
			beAttacked(a.ATK);
		}
		
		void beAttacked(int attack) {
			
		}
		
		void draw(float deltaTime) {
			displaySprite();
			
			if (nextTick(deltaTime)) {
				sprite = nextTickSprite();
			}
		}
		
		/* tick时间之后显示下一张贴图
		 * 
		 */
		String nextTickSprite() {
			return null;
		}
		
		private boolean nextTick(float deltaTime) {
			time += deltaTime;
			if (time < tick) {
				return false;
			}
			time -= tick;
			
			tickCount++;
			index ++;
			return true;
		}
	}
}
