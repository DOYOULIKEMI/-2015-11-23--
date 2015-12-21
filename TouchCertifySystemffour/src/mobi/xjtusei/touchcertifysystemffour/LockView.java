package mobi.xjtusei.touchcertifysystemffour;

import java.io.InputStream;

import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchCollectorActivity;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;


public class LockView extends Activity implements Runnable {

	public int sreenw,sreenh;
	public MView  mv;
	public Paint p;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//全屏
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//获取屏幕的宽和高
		Display display =getWindowManager().getDefaultDisplay();
		sreenw =display.getWidth();
		sreenh=display.getHeight();
		
		mv = new MView(this);
		Log.i("bc","screen lockview");
		this.setContentView(mv);
		new Thread(this).start();

	}
	//封锁Home键
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	//锁的画面
	class MView extends View implements OnTouchListener {
		public MView(Context context) {
			super(context);
			Intent intent = new Intent();
	        intent.setClass(LockView.this, ApproveActivity.class);
	        startActivity(intent);
		}

		public boolean onTouch(View v, MotionEvent e) {

			if(e.getAction() == MotionEvent.ACTION_UP)
			{ //抬起手指解锁
				finish();
			}
			this.postInvalidate();
			return true;
		}

		public void  Viewrun()
		{  //调用Activity的线程

		}

	}


	public void run() 
	{

		while(true){
			mv.Viewrun();
			try {

				Thread.sleep(200);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}