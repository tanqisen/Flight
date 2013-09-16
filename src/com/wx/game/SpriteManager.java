package com.wx.game;

import java.util.HashMap;


public class SpriteManager {
	
	public HashMap<String, Sprite> spritesMap = new HashMap<String, Sprite>();
	
	public Sprite getSprite(String name) {
		return this.spritesMap.get(name);
	}
	
	public void setSprite(Sprite s) {
		this.spritesMap.put(s.name, s);
	}
}
