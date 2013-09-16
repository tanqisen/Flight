package com.wx;

import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wx.R;
import com.wx.game.GameContext;
import com.wx.game.GameController;
import com.wx.game.Sprite;
import com.wx.game.SpriteManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class FlightSurfaceView extends SurfaceView implements Callback, Runnable {
	private Thread th = new Thread(this);
	private SurfaceHolder sfh;
	private int SH, SW;
	private Paint p;
	private Paint p2;
	
	private GameController gameController = null;

	private SpriteManager genSpriteManager() {
		SpriteManager spriteManager = new SpriteManager();
		
		try {
			String json = "";
			InputStream in = getResources().openRawResource(R.raw.gamerarts);
			int length = in.available();
			byte [] buffer = new byte[length];
			in.read(buffer);
			json = EncodingUtils.getString(buffer, "UTF8");
			
			//
			try {
				JSONTokener jsonTokener = new JSONTokener(json);
				JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
				JSONObject frames = jsonObject.getJSONObject("frames");
				Iterator<?> keyEnum = frames.keys();
				while(keyEnum.hasNext()) {
					String key = (String)keyEnum.next();
					JSONObject art = frames.getJSONObject(key);
					String textureRectStr = art.getString("textureRect");
					String spriteColorRectStr = art.getString("spriteColorRect");
					
					//
					Rect textureRect = null;
					Rect spriteColorRect = null;
					
					Pattern p = Pattern.compile("\\{\\{(\\d+), (\\d+)\\}, \\{(\\d+), (\\d+)\\}\\}");
					Matcher m = p.matcher(textureRectStr);
					
					if(m.find()) {
						MatchResult mr = m.toMatchResult();
						
						int left = Integer.parseInt(mr.group(1));
						int top = Integer.parseInt(mr.group(2));
						int right = Integer.parseInt(mr.group(3)) + left;
						int bottom = Integer.parseInt(mr.group(4)) + top;
						
						textureRect = new Rect(left,top,right,bottom);
					}
					
					m = p.matcher(spriteColorRectStr);
					if (m.find()) {
						MatchResult mr = m.toMatchResult();
						
						int left = Integer.parseInt(mr.group(1));
						int top = Integer.parseInt(mr.group(2));
						int right = Integer.parseInt(mr.group(3)) + left;
						int bottom = Integer.parseInt(mr.group(4)) + top;
						
						spriteColorRect = new Rect(left,top,right,bottom);
					}
					
					Sprite s = new Sprite();
					s.name = key;
					s.textureRect = textureRect;
					s.spriteColorRect = spriteColorRect;
					spriteManager.setSprite(s);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return spriteManager;
	}
	
	private GameContext genGameContext() {
		SpriteManager sm = genSpriteManager();
		Sprite s = sm.getSprite("background_2.png");
		Rect r = new Rect(s.spriteColorRect);
		
		//r.offset(Math.max(0,(SW-s.spriteColorRect.width())/2), Math.max(0, (SH-s.spriteColorRect.height())/2));
		//r.offset((SW-s.spriteColorRect.width())/2, (SH-s.spriteColorRect.height())/2);
		r.bottom = SH;
		r.right = SW;
		
		GameContext ctx = new GameContext(r);
		
		Bitmap bmp = null;
		try {
			InputStream is = this.getResources().openRawResource(R.raw.gamearts); 
			bmp = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//bmp = BitmapFactory.decodeResource(res, R.raw.gamearts); // 会改变图片原始尺寸，有时候这是个坑
		
		if (SH > 480) {
			Bitmap bgbmp = null;
			try {
				InputStream is = this.getResources().openRawResource(R.raw.backroundhd);
				bgbmp = BitmapFactory.decodeStream(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ctx.backgroundHDBmp = bgbmp; // 游戏背景，适配大屏幕
		}
		
		ctx.setBmp(bmp);
		ctx.setPaint(p);
		ctx.setScorePaint(p2);
		ctx.setSh(sfh);
		ctx.setSpriteManager(sm);
		
		return ctx;
	}
	
	public FlightSurfaceView(Context context) {
		super(context);
		this.setKeepScreenOn(true);
		
		sfh = this.getHolder();
		sfh.addCallback(this);
		p = new Paint();
		p.setColor(Color.WHITE);
		p.setAntiAlias(true);
		
		p2 = new Paint();
		p2.setColor(Color.rgb(60, 60, 60));
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
		p2.setTypeface( font );
		p2.setTextSize(20);
		
		setFocusable(true);
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		SH = this.getHeight();
		SW = this.getWidth();
		
		gameController = new GameController();
		gameController.gameContext = genGameContext();
		gameController.start();
		
		th.start();
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent e) { 
		int x = (int) e.getX();
		int y = (int) e.getY(); 
		
		switch (e.getAction()) { 
			case MotionEvent.ACTION_MOVE:{
				gameController.touchMove(x, y);
			}
				break;
			case MotionEvent.ACTION_DOWN:{
				gameController.touchDown(x, y);
			}
				break;
			case MotionEvent.ACTION_UP:{
				gameController.touchUp(x, y);
			}
				break;
		}
		return true;
	}

	@Override
	public void run() {
		long startTime = System.nanoTime(); 
		while (true) {
			float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
			startTime = System.nanoTime(); 
			this.gameController.updateFrame(deltaTime);
			this.gameController.renderFrame(deltaTime);
			try {
				Thread.sleep(30);
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
