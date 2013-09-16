package com.wx.game;

import java.util.ArrayList;
import java.util.Iterator;

public class GameController {
	
	public Hero hero = new Hero();
	public GameContext gameContext = null;
	
	IWeapon weapon1 = new Weapon(hero, new ArtFactory(Bullet1.class, 0.2f, 0));
	IWeapon weapon2 = new Weapon(hero, new ArtFactory(Bullet2.class, 0.2f, 0));
	
	public ArtFactory enemy1Factory  = new ArtFactory(Enemy1.class, 1, 50);
	public ArtFactory enemy2Factory  = new ArtFactory(Enemy2.class, 15, 1000);
	public ArtFactory enemy3Factory  = new ArtFactory(Enemy3.class, 5, 1000);
	
	ArrayList<Art> enemies = new ArrayList<Art>();
	ArrayList<Art> bullets = new ArrayList<Art>();
	
	ArtFactory dropBombFactory = new ArtFactory(Enemy4.class, 10, 10000);
	Art dropBomb = null;
	int bombCount = 0;
	
	ArtFactory dropDoubleFactory = new ArtFactory(Enemy5.class, 10, 10000);
	Art dropDouble = null;
	float doubleDuration = 10.0f;
	
	Art bombButton = new BombButton();
	boolean isDropBomb = false;
	
	int score = 0;
	
	CFPSMaker fpsMaker = new CFPSMaker();
	
	int originX,originY;
	boolean isMove = false;
	
	public void  start() {
		this.hero.gameContext = gameContext;
		this.hero.reset();
		this.hero.weapon = weapon1;
		
		dropBomb = null;
		dropDouble = null;
		
		bombButton.gameContext = gameContext;
		bombButton.reset();
		
		for(Art e : enemies) {
			e.reuse();
		}
		enemies.clear();
		
		for (Art b : bullets) {
			b.reuse();
		}
		bullets.clear();
		
		score = 0;
	}
	
	public void pause() {
		
	}
	
	public void resume() {
		
	}
	
	public void restart() {
		start();
	}
	
	void switchWeapon() {
		hero.weapon = (hero.weapon == weapon1) ? weapon2 : weapon1;
	}
	
	private void genEnemies(float deltaTime) {
		Art e = enemy1Factory.genareteArt(gameContext, deltaTime);
		if (e != null) {
			enemies.add(e);
		}
		
		e = enemy2Factory.genareteArt(gameContext, deltaTime);
		if (e != null) {
			enemies.add(e);
		}
		
		e = enemy3Factory.genareteArt(gameContext, deltaTime);
		if (e != null) {
			enemies.add(e);
		}
		
		if (dropBomb == null && dropDouble == null && this.hero.weapon == weapon1) {
			e = dropBombFactory.genareteArt(gameContext, deltaTime);
			if (e != null) {
				dropBomb = e;
			}
		}
		
		if (dropBomb == null && dropDouble == null && bombCount == 0) {
			e = dropDoubleFactory.genareteArt(gameContext, deltaTime);
			if (e != null) {
				dropDouble = e;
			}
		}
	}
	
	public void updateFrame(float deltaTime) {
		fpsMaker.makeFPS();
		
		if (hero.isDestroyState()) {
			return;
		}
		
		genEnemies(deltaTime);
		Art blt = hero.weapon.fire(deltaTime);
		if (blt != null) {
			this.bullets.add(blt);
		}
		
		// Ë«±¶Ê±¼äµ½
		if (hero.weapon == weapon2) {
			doubleDuration -= deltaTime;
			if (doubleDuration <= 0) {
				hero.weapon = weapon1;
			}
		}
		
		// Í¶µ¯
		if (isDropBomb && bombCount>0) {
			for(Art e : enemies) {
				e.beAttacked(100000);
			}
			
			bombCount = 0;
			isDropBomb = false;
			
			return;
		}
		
		for(Art b : bullets) {
			b.move(deltaTime);
		}
		
		Iterator<Art> eItr = enemies.iterator();
		while(eItr.hasNext()) {
			Art e = eItr.next();
			if (e.isDestroyState()) {
				score += e.score;
				e.reuse();
				eItr.remove();
				continue;
			}
			
			e.move(deltaTime);
			
			if (hero.isCollision(e)) {
				hero.beAttacked(e);
				e.beAttacked(hero);
			}
			
			Iterator<Art> bItr = bullets.iterator();
			while(bItr.hasNext()) {
				Art b = bItr.next();
				if (b.isDestroyState()) {
					b.reuse();
					bItr.remove();
					continue;
				}
				
				if (b.isCollision(e)) {
					b.beAttacked(e);
					e.beAttacked(b);
				}
			}
		}
		
		// Õ¨µ¯
		if (dropBomb != null) {
			if (dropBomb.isDestroyState()) {
				dropBomb.reuse();
				dropBomb = null;
			} else {
				dropBomb.move(deltaTime);
				
				if (dropBomb.isCollision(hero)) {
					dropBomb.beAttacked(hero);
					bombCount ++;
				}
			}
		}
		
		// Ë«±¶×Óµ¯
		if (dropDouble != null) {
			if (dropDouble.isDestroyState()) {
				dropDouble.reuse();
				dropDouble = null;
			} else {
				dropDouble.move(deltaTime);
				
				if (dropDouble.isCollision(hero)) {
					dropDouble.beAttacked(hero);
					doubleDuration = 10.0f;
					hero.weapon = weapon2;
				}
			}
		}
	}
	
	public void renderFrame(float deltaTime) {
		gameContext.lock();
		
		gameContext.drawMap(deltaTime);
		
		for(Art e : enemies) {
			e.draw(deltaTime);
		}
		
		for(Art b : bullets) {
			b.draw(deltaTime);
		}
		
		if (dropBomb != null) {
			dropBomb.draw(deltaTime);
		}
		
		if (dropDouble != null) {
			dropDouble.draw(deltaTime);
		}
		
		if (bombCount > 0) {
			bombButton.draw(deltaTime);
		}
		
		gameContext.displayScore(score);
		
		gameContext.displayFPS(fpsMaker.getFPS() + " FPS");
		
		hero.draw(deltaTime);
		
		gameContext.unlock();
	}
	
	public void touchDown(int x, int y) {
		originX = x;
		originY = y;
		
		isMove = hero.spriteFrame.contains(x,y);
		isDropBomb = (bombCount>0 && bombButton.spriteFrame.contains(x, y));
		if (isDropBomb) {
			isDropBomb = true;
		}
	}
	
	public void touchMove(int x, int y) {
		if (isMove) {
			int dx = x - originX;
			int dy = y - originY;
			
			originX = x;
			originY = y;

			hero.move(dx, dy);
		}
	}

	public void touchUp(int x, int y) {
		isMove = false;
	}
}
