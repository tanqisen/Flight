package com.wx.game;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class GameContext {
	private Rect viewRect = null;
	private SurfaceHolder surfaceHolder = null;
	private Paint paint = null;
	private Paint scorePaint = null;
	private Bitmap bmp = null;
	private Canvas canvas = null;
	private SpriteManager spriteManager = null;
	
	int mapOffsetY = 0;
	int mapSpeed = 40;
	
	public Bitmap backgroundHDBmp = null;
	
	public GameContext(Rect viewRect) {
		this.viewRect = viewRect;
	}
	
	public void lock() {
		canvas = surfaceHolder.lockCanvas();
		canvas.drawRect(viewRect, paint);
	}
	
	public void unlock() {
		surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	public void displayArt(Art a) {
		canvas.save();
		
		Sprite s = spriteManager.getSprite(a.sprite);
		
		Rect colorRect = s.spriteColorRect;
		Rect textureRect = s.textureRect;
		
		int left   = a.spriteFrame.left + colorRect.left;
		int top    = a.spriteFrame.top  + colorRect.top;
		int right  = left + colorRect.width();
		int bottom = top + colorRect.height();
		
		canvas.clipRect(left<viewRect.left?viewRect.left:left, top<viewRect.top?viewRect.top:top, 
				right>viewRect.right?viewRect.right:right, bottom>viewRect.bottom?viewRect.bottom:bottom);
		canvas.drawBitmap(bmp, left-textureRect.left, top-textureRect.top, paint);
		
		canvas.restore();
	}
	
	public void displayScore(int score) {
		canvas.save();
		canvas.drawText("分数:" + score, VLeft()+20, VTop()+30, scorePaint);
		canvas.restore();
	}
	
	public void displayFPS(String fps) {
		canvas.save();
		canvas.drawText(fps, VRight()-140, VTop()+30, scorePaint);
		canvas.restore();
	}
	
	public Sprite getSprite(String name) {
		return spriteManager.getSprite(name);
	}
	
	public void drawMap(float deltaTime) {
		//canvas.drawRect(viewRect, paint);
		
		int left   = VLeft();
		int top    = VTop() + mapOffsetY;
		int right  = VRight();
		int bottom = VBottom();
		
		// 适配屏幕高度大于480px
		if (backgroundHDBmp != null) {
			int h = backgroundHDBmp.getHeight();
			
			canvas.save();
			canvas.clipRect(left,top,right,bottom);
			canvas.drawBitmap(backgroundHDBmp, 0, top, paint);
			canvas.restore();
			
			top    = VTop();
			bottom = top + mapOffsetY; 
			
			canvas.save();
			canvas.clipRect(left,top,right,bottom);
			canvas.drawBitmap(backgroundHDBmp, 0,  top - (h - mapOffsetY), paint);
			canvas.restore();
			
			int d = (int) (mapSpeed * deltaTime);
			mapOffsetY += d;
			if (mapOffsetY > h) {
				mapOffsetY -= h;
			}
			
			return;
		}
		
		// 普通屏幕
		Sprite s = spriteManager.getSprite("background_2.png");
		Rect r = s.textureRect;
		
		canvas.save();
		canvas.clipRect(left,top,right,bottom);
		canvas.drawBitmap(bmp, left-r.left, top-r.top, paint);
		canvas.restore();
		
		top    = VTop();
		bottom = top + mapOffsetY; 
		
		canvas.save();
		canvas.clipRect(left,top,right,bottom);
		canvas.drawBitmap(bmp, left-r.left, top-r.top - (r.height() - mapOffsetY), paint);
		canvas.restore();
		
		int d = (int) (mapSpeed * deltaTime);
		mapOffsetY += d;
		if (mapOffsetY > r.height()) {
			mapOffsetY -= r.height();
		}
	}

	/**
	 * @return the viewRect
	 */
	public Rect getViewRect() {
		return viewRect;
	}

	/**
	 * @param viewRect the viewRect to set
	 */
	public void setViewRect(Rect viewRect) {
		this.viewRect = viewRect;
	}

	/**
	 * @return the paint
	 */
	public Paint getPaint() {
		return paint;
	}

	/**
	 * @param paint the paint to set
	 */
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	/**
	 * @return the bmp
	 */
	public Bitmap getBmp() {
		return bmp;
	}

	/**
	 * @param bmp the bmp to set
	 */
	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	/**
	 * @return the sh
	 */
	public SurfaceHolder getSh() {
		return surfaceHolder;
	}

	/**
	 * @param sh the sh to set
	 */
	public void setSh(SurfaceHolder sh) {
		this.surfaceHolder = sh;
	}

	/**
	 * @return the spriteManager
	 */
	public SpriteManager getSpriteManager() {
		return spriteManager;
	}

	/**
	 * @param spriteManager the spriteManager to set
	 */
	public void setSpriteManager(SpriteManager spriteManager) {
		this.spriteManager = spriteManager;
	}
	
	public int VLeft() {
		return viewRect.left;
	}
	
	public int VTop() {
		return viewRect.top;
	}
	
	public int VRight() {
		return viewRect.right;
	}
	
	public int VBottom() {
		return viewRect.bottom;
	}
	
	public int VWidth() {
		return viewRect.width();
	}
	
	public int VHeight() {
		return viewRect.height();
	}

	/**
	 * @return the scorePaint
	 */
	public Paint getScorePaint() {
		return scorePaint;
	}

	/**
	 * @param scorePaint the scorePaint to set
	 */
	public void setScorePaint(Paint scorePaint) {
		this.scorePaint = scorePaint;
	}
}
