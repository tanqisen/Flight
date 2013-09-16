package com.wx.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/* 角色工厂，负责角色创建、回收再利用
 * 
 */
public class ArtFactory {
	ArrayList<Art> reuseArtList = new ArrayList<Art>();
	Class<?> artClass = null;
	float time = 0.0f;
	float tick = 3.5f;
	float ORI_TICK = 0.0f;
	Random rand = new Random();
	public Iterator<Art> artItr = null;
	int salt = 0; // 毫秒
	
	/* 获取新的角色（可能是new出来的全新对象，也可以是回收后的对象重置状态后再利用）
	 * 
	 */
	public Art genareteArt(GameContext ctx, float deltaTime) {
		time += deltaTime;
		if (time < tick) {
			return null;
		}
		time -= tick;
		if (salt > 0) {
			tick = ORI_TICK + (float)(rand.nextInt(salt)/1000.0);
		}
		
		return getArt(ctx);
	}
	
	/* 实例化工厂
	 * param: cls 生产的对象类型
	 * param: tick 多长时间创建生产一个对象
	 * param: salt tick加上一个随机数 
	 */
	public ArtFactory(Class<?> cls, float tick, int salt) {
		this.artClass = cls;
		this.ORI_TICK = tick;
		this.tick = tick;
		this.salt = salt;
	}
	
	/* 回收对象
	 * 
	 */
	public void reuseArt(Art a) {
		reuseArtList.add(a);
	}
	
	private Art getArt(GameContext ctx)  {
		Art a = null;
		try {
			if (this.reuseArtList.size() > 0) {
				a = this.reuseArtList.remove(0);
			} else {
				a = (Art)this.artClass.newInstance();
				a.artFactor = this;
				a.gameContext = ctx;
			}
			a.reset();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return a;
	}
}
