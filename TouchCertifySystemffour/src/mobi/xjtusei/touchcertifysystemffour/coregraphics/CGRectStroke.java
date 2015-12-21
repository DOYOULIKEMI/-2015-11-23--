package mobi.xjtusei.touchcertifysystemffour.coregraphics;

import android.graphics.Canvas;

import android.graphics.Paint.Style;

public class CGRectStroke extends CGNode {
	public CGRectStroke(int width,int height){
		super();
		this.setWidth(width);
		this.setHeight(height);
		p.setStyle(Style.STROKE);
		this.setMoveable(true);
	}
	public void draw(Canvas c){
    	c.drawRect(this.getRect(), p);
    	super.draw(c);
	}
}
