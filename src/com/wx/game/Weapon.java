package com.wx.game;

public class Weapon implements IWeapon {
	Art hero = null;
	ArtFactory bulletFactory = null;
	
	public Weapon(Art hero, ArtFactory bulletFactory) {
		this.hero = hero;
		this.bulletFactory = bulletFactory;
	}
	
	@Override
	public Art fire(float deltaTime) {
		if (bulletFactory != null) {
			Art b = bulletFactory.genareteArt(hero.gameContext, deltaTime);
			if (b != null) {
				b.spriteFrame.offset(hero.spriteFrame.left+(hero.spriteFrame.width() - b.spriteFrame.width())/2, hero.spriteFrame.top-b.spriteFrame.top);
			}
			return b;
		}
		return null;
	}

}
