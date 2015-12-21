package mobi.xjtusei.touchcertifysystemffour.coregraphics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mobi.xjtusei.touchcertifysystemffour.R;
import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchCollectorActivity;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CGView extends SurfaceView implements SurfaceHolder.Callback {
	public static String typeCG;
	private Bitmap mBitmap;
	private Bitmap oBitmap;
	private Bitmap backmap;
	private InputStream is;
	private InputStream backis;
	public int fingercount=0;
	private InputStream iso;
	private float x = 0;
    public int touchfingertimes[]=new int[10];
	private float y = 0;
	private float speedx = 50;
	private float speedy = 50;
	private float addx = 30;
	private int proxbegin=1800-100;
	private int proxlast=2500-100-150;
	private int proybegin=200;
	private int proylast=250;
	private int textx=1585-100;
    private int texty=246;
    private int pronumx=2400-150;
    private int pronumy=246;
	int[] pointx=new int[]{915,980,1053,1149};
	int[] pointy=new int[]{630,578,579,627};
	public int maxcount=0;
	public int trainedNumCG;
	public int remainTrainNumCG=0;
    private float addy = 30;
	private Timer timer;
	private static final int[] SECTION_COLORS = {Color.rgb(44, 143, 254),Color.rgb(22, 180, 254),Color.rgb(0, 216, 255)};
	private int timesofslide=0;   
    public float startX[]=new float[10];//初始x   
    public float startY[]=new float[10];//初始Y  
    public float startXforshow[]=new float[4];//初始x   
    public float startYforshow[]=new float[4];//初始Y  
    //TimerTask一个抽象类，它的子类代表一个可以被Timer计划的任务
    private TimerTask task;		
	protected CGNode rootNode = new CGNode();
	public ArrayList<ArrayList<Float>> smcir = new ArrayList<ArrayList<Float>>();
	public float rad = 5;
	private int widthPixels;
	public Path mPath[]=new Path[10];
	public Path Pathforshow[]=new Path[4];
	public int getWidthPixels() {
		return widthPixels;
	}
	
	public void setWidthPixels(int widthPixels) {
		this.widthPixels = widthPixels;
	}
	public void addChild(CGNode n){
		rootNode.addChild(n);
	}
	public void removeAllChildren(){
		rootNode.removeAllChildren();
	}
	private int heightPixels;
	
	public int getHeightPixels() {
		return heightPixels;
	}

	public void setHeightPixels(int heightPixels) {
		this.heightPixels = heightPixels;
	}

	public CGView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SurfaceHolder holder = getHolder();  
        holder.addCallback(this);  
        setFocusable(true); // 确保我们的View能获得输入焦点  
        setFocusableInTouchMode(true); // 确保能接收到触屏事件  
        
        DisplayMetrics dm = new DisplayMetrics();
		((MultiTouchCollectorActivity) this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;
		rootNode.setTouchEnable(true);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Canvas c = getHolder().lockCanvas();  
		is = getResources().openRawResource(R.drawable.thumb);
		iso = getResources().openRawResource(R.drawable.orbit);
		backis=getResources().openRawResource(R.drawable.backtrain);
		mBitmap = BitmapFactory.decodeStream(is);
		oBitmap = BitmapFactory.decodeStream(iso);
		backmap = BitmapFactory.decodeStream(backis);		
		for(int i=0;i<4;i++)
		{   Pathforshow[i]=new Path();
		    Pathforshow[i].reset();
			Pathforshow[i].moveTo(pointx[i],pointy[i]);
		    startXforshow[i]=pointx[i];
		    startYforshow[i]=pointy[i];
		}
		
		getHolder().unlockCanvasAndPost(c); 
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	public void doDraw(){
		Log.i("diaoyong:","调用2");
		Canvas c = getHolder().lockCanvas(); 
		if (c != null)
        {   
			RectF rectFpic = new RectF(0, 0, widthPixels, heightPixels);
			RectF rectFo = new RectF(widthPixels/3+widthPixels/12,heightPixels/3+heightPixels/12, widthPixels/7+widthPixels/3+widthPixels/8, heightPixels/5+heightPixels/3+heightPixels/8);
			RectF rectF = new RectF(widthPixels/3,heightPixels/3, widthPixels/7+widthPixels/3, heightPixels/4+heightPixels/3);
        	// 背景黑色  
        	//c.drawColor(Color.WHITE);
        	c.drawBitmap(mBitmap,null, rectF,null);
        	c.drawBitmap(oBitmap, null, rectFo,null);
        	c.drawBitmap(backmap,null,rectFpic,null);
            MaskFilter maskFilter = new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID);
        	Paint p = new Paint();
        	Paint picp=new Paint();
        	Paint pt = new Paint();
        	Paint pc0 = new Paint();
        	Paint pc1 = new Paint();
        	Paint pc2 = new Paint();
        	Paint pc3 = new Paint();
        	
        	Paint pttip = new Paint();
        	Paint roundp = new Paint();
        	Paint progressp = new Paint();
        	Paint bgp = new Paint();
        	Paint pc4 = new Paint();
        	pt.setTextSize(40);    
        	bgp.setColor(Color.BLACK);
        	roundp.setStyle(Paint.Style.STROKE);
        	roundp.setStrokeWidth(5);
            pt.setColor(Color.BLACK);
            pc0.setStyle(Paint.Style.STROKE);
            pttip.setColor(Color.rgb(44, 143, 254));
            pttip.setTextSize(50);
            pc0.setStrokeWidth(5);
            pc0.setColor(Color.WHITE);pc1.setColor(Color.WHITE);pc2.setColor(Color.WHITE);
            pc3.setColor(Color.WHITE);pc4.setColor(Color.WHITE);
            pc0.setAntiAlias(true);
            pc1.setAntiAlias(true);
            pc2.setAntiAlias(true);
            pc3.setAntiAlias(true);
            pc4.setAntiAlias(true);  
            pc0.setStyle(Style.STROKE);
            pc1.setStyle(Style.STROKE);
            pc2.setStyle(Style.STROKE);
            pc3.setStyle(Style.STROKE);
            pc4.setStyle(Style.STROKE);
            pttip.setTypeface(Typeface.SERIF);
            pc0.setStrokeWidth(85);
            pc1.setStrokeWidth(85);
            pc2.setStrokeWidth(85);
            pc3.setStrokeWidth(85);
            pc4.setStrokeWidth(85);
           // pc0.setMaskFilter(maskFilter);
           // pc1.setMaskFilter(maskFilter);
          //  pc2.setMaskFilter(maskFilter);
          //  pc3.setMaskFilter(maskFilter);
           // pc4.setMaskFilter(maskFilter);
            pc0.setAlpha(50);
            pc1.setAlpha(75);
            pc2.setAlpha(100);
            pc3.setAlpha(125);
            pc4.setAlpha(150);
        	//pc0.setStyle(Style.FILL);pc1.setStyle(Style.FILL);pc2.setStyle(Style.FILL);
        	//pc3.setStyle(Style.FILL);pc4.setStyle(Style.FILL);
    		p.setColor(Color.LTGRAY);
//    		c.save();   	    	
    		String infoStr = "训练进度";	
    		String infopro =trainedNumCG+""+"/"+(trainedNumCG+remainTrainNumCG)+"";
    		RectF rect = new RectF(MultiTouchView.pos[0][0],MultiTouchView.pos[0][1],
    				MultiTouchView.pos[0][2],MultiTouchView.pos[0][3]);
    		RectF rect2 = new RectF(MultiTouchView.pos[1][0],MultiTouchView.pos[1][1],
    				MultiTouchView.pos[1][2],MultiTouchView.pos[1][3]);
    		/*RectF rect3 = new RectF(MultiTouchView.pos[2][0],MultiTouchView.pos[2][1],
    				MultiTouchView.pos[2][2],MultiTouchView.pos[2][3]);*/
    		//c.drawRoundRect(rect,10,10, p);
    		//c.drawRoundRect(rect2,10,10, p);
    		//c.drawRoundRect(rect3,10,10, p);
    		roundp.setColor(Color.rgb(71, 76, 80));
    		RectF roundrec=new RectF(proxbegin,proybegin,proxlast,proylast);
    		RectF BGrec=new RectF(proxbegin+2,proybegin+2,proxlast-2,proylast-2);
   		   
    		if(typeCG.equals("train"))
    		{drawText(c,infoStr, textx, texty, pttip,0);
    		 drawText(c,infopro, pronumx, pronumy, pttip,0); 
    		c.drawRoundRect(roundrec, 25, 25, roundp); }   
    		if(typeCG.equals("train"))//绘制圆角矩形  
    		    c.drawRoundRect(BGrec, 24, 24, bgp); 
    		    float trainedfloat=trainedNumCG;
    		    float remainfloat=remainTrainNumCG;
    		    float progresspro=trainedfloat/(remainfloat+trainedfloat);
    		    Log.i("概率",progresspro+"");
    		    RectF progressrec=new RectF(proxbegin+3,proybegin+3,progresspro*(proxlast-3-proxbegin)+proxbegin,proylast-3);
    		    if( progresspro <= 0.3){
    		    	   if( progresspro != 0.0f){
    		    	    progressp.setColor(SECTION_COLORS[0]);
    		    	   }else{
    		    		   progressp.setColor(Color.TRANSPARENT);
    		    	   }
    		    	  }else{
    		    	   int count = ( progresspro <= 0.6 ) ? 2 : 3;
    		    	   Log.i("概率",count+"");
    		    	   int[] colors = new int[count];
    		    	   System.arraycopy(SECTION_COLORS, 0, colors, 0, count);
    		    	   float[] positions = new float[count];
    		    	   if(count == 2){
    		    	    positions[0] = 0.0f;
    		    	    positions[1] = 1.0f-positions[0];
    		    	   }else{
    		    	    positions[0] = 0.0f;
    		    	    positions[1] = ((trainedfloat+remainfloat)/3)/trainedfloat;
    		    	    positions[2] = 1.0f-positions[0]*2;
    		    	   }
    		    	   positions[positions.length-1] = 1.0f;
    		    	   LinearGradient shader = new LinearGradient(3, 3, (proxlast-3)*progresspro, proybegin-3, colors,null, Shader.TileMode.MIRROR);
    		    	   progressp.setShader(shader);
    		    	  }
    		    if(typeCG.equals("train"))
    		    	  c.drawRoundRect(progressrec, 22, 22, progressp);
    		    	 

    		if (MultiTouchView.handlor.equals("right")){	       			
	    	//	drawText(c,"清除", 80, 110, pt,0);
	 //   		c.restore();
	    	//	drawText(c,"确定", 80, 310, pt,0);
	    		//drawText(c,"退出", 80, 510, pt,0);
    		}else {
	    		//drawText(c,"清除", 1120, 110, pt,0);
	 //   		c.restore();
	    		//drawText(c,"确定", 1120, 310, pt,0);
	    		//drawText(c,"退出", 1120, 510, pt,0);
			}
//    		pt.setTextAlign(Paint.Align.CENTER);
//        	rootNode.draw(c);
        	// 画完后，unlock 
//    		c.restore();
    	for (int i = 0;i<MultiTouchView.fplot.size();i++){
    			/*if(((MultiTouchView.fplot.get(i).get(0)).intValue()==0)&&i>1){
    				MultiTouchView.fplot.get(i).set(0, (MultiTouchView.fplot.get(i-1).get(0)));
    			}*/
//    			来自android层的数据
    			if((MultiTouchView.fplot.get(i).get(2)).intValue()==0){
    			//	c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc0);    			   				
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==1){
    			//	c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc1);    			
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==2){
       				//c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc2);   				
    		}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==3){
    			//	c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc3);    				
    			    MultiTouchView.setsumfingers(true);
    			}else{
    		//		c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc4);				
    		        MultiTouchView.setmultifingers(false);
    			}  
//    			来自底层的数据
/*    			if((MultiTouchView.fplot.get(i).get(2)).intValue()==0){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc0);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==1){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc1);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==2){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc2);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==3){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc3);
    			}else{
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc4);
    				MultiTouchView.setmultifingers(false);
    			}*/
     		}            
    	      Log.i("circlefinished","haha");
    	      Log.i("circlefinished",fingercount+"");
    	      for(int i=0;i<maxcount;i++)
    	      {
    	      if(i==0)
		      c.drawPath(mPath[i],pc0);
    	      if(i==1)
    		      c.drawPath(mPath[i],pc1);
    	      if(i==2)
    		      c.drawPath(mPath[i],pc2);
    	      if(i==3)
    		      c.drawPath(mPath[i],pc3);
    	      if(i==4)
    		      c.drawPath(mPath[i],pc4);
    	      }
		     
/*    		for (int i = 0;i<smcir.size();i++){
    			c.drawCircle(smcir.get(i).get(0),smcir.get(i).get(1),rad,pc0);
    			Log.i("smcir",smcir.get(i).get(0)+" "+smcir.get(i).get(1));
    		}*/
//    		smcir.clear();
//    		smcir = new ArrayList<ArrayList<Float>>();
            getHolder().unlockCanvasAndPost(c);             
        }
	}
	public void do1Draw(){
		Log.i("diaoyong:","调用2");
		Canvas c = getHolder().lockCanvas(); 
		String infoStr = "训练进度";		
		if (c != null)
        {			
			if (y<600)
			y += addy;	
			else
			x += addx;
			if(x>500)
				{
				for(int i=0;i<4;i++)
				{   Pathforshow[i]=new Path();
				    Pathforshow[i].reset();
					Pathforshow[i].moveTo(pointx[i],pointy[i]);
					startXforshow[i]=pointx[i];
					startYforshow[i]=pointy[i];
				}
				timesofslide++;
				x=0;
				y=0;}	
			for(int i=0;i<4;i++)
			{
				Pathforshow[i].lineTo(pointx[i]+x, pointy[i]+y);			      
			    float cX = (startXforshow[i]+pointx[i]+x) / 2;  
			    float cY = (startYforshow[i]+pointy[i]+y) / 2;	
			   // this.mPath[i].moveTo(startX[i], startY[i]);
			   this.Pathforshow[i].quadTo(startXforshow[i],startYforshow[i],cX,cY);	
			   // this.mPath[i].lineTo(event.getX(i), event.getY(i));
			    this.startXforshow[i]=pointx[i]+x;
			    this.startYforshow[i]=pointy[i]+y;
			}
        	// 背景黑色  	
			RectF rectFpic = new RectF(0, 0, widthPixels, heightPixels);
			RectF rectF = new RectF(widthPixels/3+x,y+heightPixels/3, x+widthPixels/7+widthPixels/3, y+heightPixels/4+heightPixels/3);
			RectF rectFo = new RectF(widthPixels/3+widthPixels/12,heightPixels/3+heightPixels/12, widthPixels/7+widthPixels/3+widthPixels/8, heightPixels/5+heightPixels/3+heightPixels/8);
			//c.drawColor(Color.WHITE);
			c.drawBitmap(backmap,null,rectFpic,null);
        	c.drawBitmap(mBitmap,null, rectF,null); 
        	if(timesofslide==2)
        	//c.drawBitmap(oBitmap, null, rectFo,null);
        	if(timesofslide==2)
				this.stopTimer();
        	Paint p = new Paint();
        	Paint pt = new Paint();
        	Paint pttip = new Paint();
        	Paint pc0 = new Paint();
        	Paint pc1 = new Paint();
        	Paint pc2 = new Paint();
        	Paint pc3 = new Paint();
        	Paint pc4 = new Paint();
        	Paint roundp = new Paint();
        	Paint progressp = new Paint();
        	Paint bgp = new Paint();
        	Paint pc0forshow = new Paint();
        	Paint pc1forshow = new Paint();
        	Paint pc2forshow = new Paint();
        	Paint pc3forshow = new Paint();
        	Paint pc4forshow = new Paint();
        	 pc0forshow.setStyle(Style.STROKE);
             pc1forshow.setStyle(Style.STROKE);
             pc2forshow.setStyle(Style.STROKE);
             pc3forshow.setStyle(Style.STROKE);
             pc4forshow.setStyle(Style.STROKE);
             pc0forshow.setStrokeWidth(85);
             pc1forshow.setStrokeWidth(85);
             pc2forshow.setStrokeWidth(85);
             pc3forshow.setStrokeWidth(85);
             pc4forshow.setStrokeWidth(85);
           
        	pt.setTextSize(40); 
        	pttip.setTextSize(50);
            pt.setColor(Color.BLACK);
            pttip.setColor(Color.rgb(44,143,254));
            pc0forshow.setColor(Color.WHITE);pc1forshow.setColor(Color.WHITE);pc2forshow.setColor(Color.WHITE);
            pc3forshow.setColor(Color.WHITE);pc4forshow.setColor(Color.WHITE);
      
            pc0forshow.setAntiAlias(true);
            pc1forshow.setAntiAlias(true);
            pc2forshow.setAntiAlias(true);
            pc3forshow.setAntiAlias(true);
            pc4forshow.setAntiAlias(true); 
            pc0forshow.setAlpha(50);
            pc1forshow.setAlpha(75);
            pc2forshow.setAlpha(100);
            pc3forshow.setAlpha(125);
            pc4forshow.setAlpha(150);     
    		p.setColor(Color.LTGRAY);
    		pttip.setTypeface(Typeface.SERIF);
//    		c.save();
    		c.drawPath(Pathforshow[0],pc0forshow);
    		c.drawPath(Pathforshow[1],pc1forshow);
    		c.drawPath(Pathforshow[2],pc2forshow);
    		c.drawPath(Pathforshow[3],pc3forshow);
    		RectF rect = new RectF(MultiTouchView.pos[0][0],MultiTouchView.pos[0][1],
    				MultiTouchView.pos[0][2],MultiTouchView.pos[0][3]);
    		RectF rect2 = new RectF(MultiTouchView.pos[1][0],MultiTouchView.pos[1][1],
    				MultiTouchView.pos[1][2],MultiTouchView.pos[1][3]);
    		/*RectF rect3 = new RectF(MultiTouchView.pos[2][0],MultiTouchView.pos[2][1],
    				MultiTouchView.pos[2][2],MultiTouchView.pos[2][3]);*/
    		//c.drawRoundRect(rect,10,10, p);
    		//c.drawRoundRect(rect2,10,10, p);
    		//c.drawRoundRect(rect3,10,10, p);
    		roundp.setColor(Color.rgb(71, 76, 80));
    		RectF roundrec=new RectF(proxbegin,proybegin,proxlast,proylast);
    		RectF BGrec=new RectF(proxbegin+2,proybegin+2,proxlast-2,proylast-2);
    		String infopro =trainedNumCG+""+"/"+(trainedNumCG+remainTrainNumCG)+"";
    		if(typeCG.equals("train"))
    		{drawText(c,infoStr, textx, texty, pttip,0);
    		 drawText(c,infopro, pronumx, pronumy, pttip,0);
    			c.drawRoundRect(roundrec, 25, 25, roundp);   } 
    		if(typeCG.equals("train"))//绘制圆角矩形  
    		    c.drawRoundRect(BGrec, 24, 24, bgp); 
    		    float trainedfloat=trainedNumCG;
    		    float remainfloat=remainTrainNumCG;
    		    float progresspro=trainedfloat/(remainfloat+trainedfloat);
    		    Log.i("概率",progresspro+"");
    		    RectF progressrec=new RectF(proxbegin+3,proybegin+3,progresspro*(proxlast-3-proxbegin)+proxbegin,proylast-3);
    		    if( progresspro <= 0.3){
    		    	   if( progresspro != 0.0f){
    		    	    progressp.setColor(SECTION_COLORS[0]);
    		    	   }else{
    		    		   progressp.setColor(Color.TRANSPARENT);
    		    	   }
    		    	  }else{
    		    	   int count = ( progresspro <= 0.6 ) ? 2 : 3;
    		    	   Log.i("概率",count+"");
    		    	   int[] colors = new int[count];
    		    	   System.arraycopy(SECTION_COLORS, 0, colors, 0, count);
    		    	   float[] positions = new float[count];
    		    	   if(count == 2){
    		    	    positions[0] = 0.0f;
    		    	    positions[1] = 1.0f-positions[0];
    		    	   }else{
    		    	    positions[0] = 0.0f;
    		    	    positions[1] = ((trainedfloat+remainfloat)/3)/trainedfloat;
    		    	    positions[2] = 1.0f-positions[0]*2;
    		    	   }
    		    	   positions[positions.length-1] = 1.0f;
    		    	   LinearGradient shader = new LinearGradient(3, 3, (proxlast-3)*progresspro, proybegin-3, colors,null, Shader.TileMode.MIRROR);
    		    	   progressp.setShader(shader);
    		    	  }
    		    if(typeCG.equals("train"))
    		    	  c.drawRoundRect(progressrec, 22, 22, progressp);
    		if(typeCG.equals("train"))
    		//drawText(c,"请按照如上动画轨迹进行注册", 850, 1500, pttip,0);
    		if(typeCG.equals("test"))
    		//drawText(c,"请按照如上动画轨迹进行认证", 850, 1500, pttip,0);
    		if (MultiTouchView.handlor.equals("right")){	       			
	    		//drawText(c,"清除", 80, 110, pt,0);
	 //   		c.restore();
	    		//drawText(c,"确定", 80, 310, pt,0);
	    		//drawText(c,"退出", 80, 510, pt,0);
    		}else {
	    		drawText(c,"清除", 1120, 110, pt,0);
	 //   		c.restore();
	    		drawText(c,"确定", 1120, 310, pt,0);
	    		//drawText(c,"退出", 1120, 510, pt,0);
			}
//    		pt.setTextAlign(Paint.Align.CENTER);
//        	rootNode.draw(c);
        	// 画完后，unlock 
//    		c.restore();
    		for (int i = 0;i<MultiTouchView.fplot.size();i++){
    			/*if(((MultiTouchView.fplot.get(i).get(0)).intValue()==0)&&i>1){
    				MultiTouchView.fplot.get(i).set(0, (MultiTouchView.fplot.get(i-1).get(0)));
    			}*/
//    			来自android层的数据
    			if((MultiTouchView.fplot.get(i).get(2)).intValue()==0){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc0);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==1){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc1);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==2){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc2);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==3){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc3);
    				MultiTouchView.setsumfingers(true);
    			}else{
    				c.drawCircle(MultiTouchView.fplot.get(i).get(0),MultiTouchView.fplot.get(i).get(1),rad,pc4);
    				MultiTouchView.setmultifingers(false);
    			}
//    			来自底层的数据
/*    			if((MultiTouchView.fplot.get(i).get(2)).intValue()==0){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc0);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==1){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc1);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==2){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc2);
    			}else if((MultiTouchView.fplot.get(i).get(2)).intValue()==3){
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc3);
    			}else{
    				c.drawCircle(MultiTouchView.fplot.get(i).get(1),heightPixels-MultiTouchView.fplot.get(i).get(0),rad,pc4);
    				MultiTouchView.setmultifingers(false);
    			}*/
     		}
/*    		for (int i = 0;i<smcir.size();i++){
    			c.drawCircle(smcir.get(i).get(0),smcir.get(i).get(1),rad,pc0);
    			Log.i("smcir",smcir.get(i).get(0)+" "+smcir.get(i).get(1));
    		}*/
//    		smcir.clear();
//    		smcir = new ArrayList<ArrayList<Float>>();
            getHolder().unlockCanvasAndPost(c); 
          
        }
	}
	public void startTimer() {
        timesofslide=0;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                //在定时器线程中调用绘图方法    	
            	do1Draw();
            }
        };
        timer.schedule(task, 100, 50);
	}
	public void stopTimer() {
        timer.cancel(); 
        
    }
	public boolean onTouchEvent(MotionEvent event){
		super.onTouchEvent(event);
//		Log.i("CGNode", "touchCount:"+event.getPointerCount()+" "+event.getX()+" "+event.getY()+" "+event.getPressure());
		int ptn = event.getPointerCount();
		for (int i = 0;i<ptn;i++){
			ArrayList<Float> temp = new ArrayList<Float>();
			temp.add(event.getX(i));
			temp.add(event.getY(i));
			smcir.add(temp);
		}		
		if(event.getAction() == MotionEvent.ACTION_DOWN&&
				event.getX()<MultiTouchView.pos[0][2]&&event.getX()>MultiTouchView.pos[0][0]&&
				event.getY()<MultiTouchView.pos[0][3]&&event.getY()>MultiTouchView.pos[0][1]){
			//MultiTouchView.fplot.clear();
		//	smcir.clear();
		}
		if(event.getAction() == MotionEvent.ACTION_DOWN&&
				event.getX()<MultiTouchView.pos[1][2]&&event.getX()>MultiTouchView.pos[1][0]&&
				event.getY()<MultiTouchView.pos[1][3]&&event.getY()>MultiTouchView.pos[1][1]){
			//MultiTouchView.fplot.clear();
			//smcir.clear();
		}	
		return rootNode.onCGTouchEvent(event);
		
	}	
	void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y); 
        }
        canvas.drawText(text, x, y, paint);
 /*       if(angle != 0){
            canvas.rotate(-angle, x, y); 
        }*/
    }	
}
