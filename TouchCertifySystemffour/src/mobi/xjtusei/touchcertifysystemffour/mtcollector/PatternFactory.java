package mobi.xjtusei.touchcertifysystemffour.mtcollector;

import java.util.ArrayList;

import mobi.xjtusei.touchcertifysystemffour.coregraphics.CGRectStroke;

import android.graphics.Color;
import android.graphics.Rect;


public class PatternFactory {
	public static float heightPixels;
	public static float widthPixels;
	
	public static ArrayList<PatternRectFill> getRects(int patternId,int detailIndex){
		int width = (int)(50*widthPixels/320);//单点触摸时矩形的大小
		int height = (int)(50*heightPixels/480);
		
		ArrayList<PatternRectFill> rects = new ArrayList<PatternRectFill>();
		if(patternId==0){
			//模式1 单点挪动
			switch(detailIndex){
				case 0:
					//从左边移动到右边
					PatternRectFill rect0 = new PatternRectFill(width,height);
					rect0.setPosition(width/2,heightPixels/2);
					CGRectStroke strokeRect0 = new CGRectStroke(width,height);
					strokeRect0.setPosition(widthPixels-width/2,heightPixels/2);
					rect0.setDstRect(strokeRect0);
					rects.add(rect0);
					break;
				case 1:
					//从上移动到下
					PatternRectFill rect1 = new PatternRectFill(width,height);
					rect1.setPosition(widthPixels/2,height/2);
					CGRectStroke strokeRect1 = new CGRectStroke(width,height);
					strokeRect1.setPosition(widthPixels/2,heightPixels-height/2);
					rect1.setDstRect(strokeRect1);
					rects.add(rect1);
					break;
				case 2:
					//从右上移动到左下
					PatternRectFill rect2 = new PatternRectFill(width,height);
					rect2.setPosition(widthPixels-width/2,height/2);
					CGRectStroke strokeRect2 = new CGRectStroke(width,height);
					strokeRect2.setPosition(width/2,heightPixels-height/2);
					rect2.setDstRect(strokeRect2);
					rects.add(rect2);
					break;
				case 3:
					//从右上移动到左下
					PatternRectFill rect3 = new PatternRectFill(width,height);
					rect3.setPosition(width/2,height/2);
					CGRectStroke strokeRect3 = new CGRectStroke(width,height);
					strokeRect3.setPosition(widthPixels-width/2,heightPixels-height/2);
					rect3.setDstRect(strokeRect3);
					rects.add(rect3);
					break;
			}
			
		}else if(patternId==1){
			//多点触摸 移动
			switch(detailIndex){
				case 0:
					//从左边移动到右边
					PatternRectFill rect0 = new PatternRectFill(2*width,2*height);
					rect0.setPosition(width,heightPixels/2);
					rect0.setMutipleTouch(true);
					CGRectStroke strokeRect0 = new CGRectStroke(2*width,2*height);
					strokeRect0.setPosition(widthPixels-width,heightPixels/2);
					rect0.setDstRect(strokeRect0);
					rects.add(rect0);
					break;
				case 1:
					//从上移动到下
					PatternRectFill rect1 = new PatternRectFill(2*width,2*height);
					rect1.setPosition(widthPixels/2,height);
					rect1.setMutipleTouch(true);
					CGRectStroke strokeRect1 = new CGRectStroke(2*width,2*height);
					strokeRect1.setPosition(widthPixels/2,heightPixels-height);
					rect1.setDstRect(strokeRect1);
					rects.add(rect1);
					break;
				case 2:
					//从右上移动到左下
					PatternRectFill rect2 = new PatternRectFill(2*width,2*height);
					rect2.setPosition(widthPixels-width,height);
					rect2.setMutipleTouch(true);
					CGRectStroke strokeRect2 = new CGRectStroke(2*width,2*height);
					strokeRect2.setPosition(width,heightPixels-height);
					rect2.setDstRect(strokeRect2);
					rects.add(rect2);
					break;
				case 3:
					//从右上移动到左下
					PatternRectFill rect3 = new PatternRectFill(2*width,2*height);
					rect3.setPosition(width,height);
					rect3.setMutipleTouch(true);
					CGRectStroke strokeRect3 = new CGRectStroke(2*width,2*height);
					strokeRect3.setPosition(widthPixels-width,heightPixels-height);
					rect3.setDstRect(strokeRect3);
					rects.add(rect3);
					break;
			}
		}else if(patternId==2){
			//多点触摸 缩放
			switch(detailIndex){
				case 0:
					//从左下 右上 到中间
					PatternRectFill rect0 = new PatternRectFill(width,height);
					rect0.setPosition(width/2,heightPixels-height/2);
					rect0.setInMutipleEnv(true);
					PatternRectFill rect1 = new PatternRectFill(width,height);
					rect1.setPosition(widthPixels-width/2,height/2);
					rect1.setColor(Color.GREEN);
					rect1.setInMutipleEnv(true);
					
					//限定移动范围
					Rect limitRect0 = new Rect();
					limitRect0.left = 0;
					limitRect0.bottom = (int) heightPixels;
					limitRect0.right = (int) (widthPixels/2);
					limitRect0.top = (int) (heightPixels/2);
					rect0.setLimitTouchRect(limitRect0);
					
					Rect limitRect1 = new Rect();
					limitRect1.left = (int) (widthPixels/2);
					limitRect1.bottom = (int) (heightPixels/2);
					limitRect1.right = (int) widthPixels;
					limitRect1.top = 0;
					rect1.setLimitTouchRect(limitRect1);
					
					CGRectStroke strokeRect0 = new CGRectStroke(width,height);
					strokeRect0.setPosition(widthPixels/2-width/2,heightPixels/2+height/2);
					rect0.setDstRect(strokeRect0);
					
					CGRectStroke strokeRect1 = new CGRectStroke(width,height);
					strokeRect1.setPosition(widthPixels/2+width/2,heightPixels/2-height/2);
					rect1.setDstRect(strokeRect1);
					
					rects.add(rect0);
					rects.add(rect1);
					break;
				case 1:
					//从中间到左下 右上
					PatternRectFill rect2 = new PatternRectFill(width,height);
					rect2.setPosition(widthPixels/2-width/2,heightPixels/2+height/2);
					rect2.setInMutipleEnv(true);
					PatternRectFill rect3 = new PatternRectFill(width,height);
					rect3.setPosition(widthPixels/2+width/2,heightPixels/2-height/2);
					rect3.setColor(Color.GREEN);
					rect3.setInMutipleEnv(true);
					
					//限定移动范围
					Rect limitRect2 = new Rect();
					limitRect2.left = 0;
					limitRect2.bottom = (int) heightPixels;
					limitRect2.right = (int) (widthPixels/2);
					limitRect2.top = (int) (heightPixels/2);
					rect2.setLimitTouchRect(limitRect2);
					
					Rect limitRect3 = new Rect();
					limitRect3.left = (int) (widthPixels/2);
					limitRect3.bottom = (int) (heightPixels/2);
					limitRect3.right = (int) widthPixels;
					limitRect3.top = 0;
					rect3.setLimitTouchRect(limitRect3);
					
					CGRectStroke strokeRect2 = new CGRectStroke(width,height);
					strokeRect2.setPosition(width/2,heightPixels-height/2);
					rect2.setDstRect(strokeRect2);
					
					CGRectStroke strokeRect3 = new CGRectStroke(width,height);
					strokeRect3.setPosition(widthPixels-width/2,height/2);
					rect3.setDstRect(strokeRect3);
					
					rects.add(rect2);
					rects.add(rect3);
					break;
				case 2:
					//旋转1
					PatternRectFill rect4 = new PatternRectFill(width,height);
					rect4.setInMutipleEnv(true);
					rect4.setPosition(widthPixels/2-widthPixels/4,heightPixels/2-heightPixels/8);
					
					PatternRectFill rect5 = new PatternRectFill(width,height);
					rect5.setInMutipleEnv(true);
					rect5.setPosition(widthPixels/2+widthPixels/4,heightPixels/2+heightPixels/8);
					rect5.setColor(Color.GREEN);
					
					//限定移动范围
					Rect limitRect4 = new Rect();
					limitRect4.left = 0;
					limitRect4.bottom = (int) (heightPixels/2);
					limitRect4.right = (int) (widthPixels);
					limitRect4.top =0;
					rect4.setLimitTouchRect(limitRect4);
					
					Rect limitRect5= new Rect();
					limitRect5.left = 0;
					limitRect5.bottom = (int) (heightPixels);
					limitRect5.right = (int) widthPixels;
					limitRect5.top = (int) (heightPixels/2);
					rect5.setLimitTouchRect(limitRect5);
					
					
					CGRectStroke strokeRect4 = new CGRectStroke(width,height);
					strokeRect4.setPosition(widthPixels/2,heightPixels/2-heightPixels/5-height);
					rect4.setDstRect(strokeRect4);
					
					CGRectStroke strokeRect5 = new CGRectStroke(width,height);
					strokeRect5.setPosition(widthPixels/2,heightPixels/2+heightPixels/5+height);
					rect5.setDstRect(strokeRect5);
					
					rects.add(rect4);
					rects.add(rect5);
					break;
				case 3:
					//旋转2
					PatternRectFill rect6 = new PatternRectFill(width,height);
					rect6.setInMutipleEnv(true);
					rect6.setPosition(widthPixels/2,heightPixels/2-heightPixels/5-height);
					
					PatternRectFill rect7 = new PatternRectFill(width,height);
					rect7.setInMutipleEnv(true);
					rect7.setPosition(widthPixels/2,heightPixels/2+heightPixels/5+height);
					rect7.setColor(Color.GREEN);
					
					//限定移动范围
					Rect limitRect6 = new Rect();
					limitRect6.left = 0;
					limitRect6.bottom = (int) (heightPixels/2);
					limitRect6.right = (int) (widthPixels);
					limitRect6.top =0;
					rect6.setLimitTouchRect(limitRect6);
					
					Rect limitRect7 = new Rect();
					limitRect7.left = 0;
					limitRect7.bottom = (int) (heightPixels);
					limitRect7.right = (int) widthPixels;
					limitRect7.top = (int) (heightPixels/2);
					rect7.setLimitTouchRect(limitRect7);
					
					
					CGRectStroke strokeRect6 = new CGRectStroke(width,height);
					strokeRect6.setPosition(widthPixels/2-widthPixels/4,heightPixels/2-heightPixels/8);
					rect6.setDstRect(strokeRect6);
					
					CGRectStroke strokeRect7 = new CGRectStroke(width,height);
					strokeRect7.setPosition(widthPixels/2+widthPixels/4,heightPixels/2+heightPixels/8);
					rect7.setDstRect(strokeRect7);
					
					rects.add(rect6);
					rects.add(rect7);
					break;
			}
		}
		
		
		
		return rects;
	}  
}
