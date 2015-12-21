package mobi.xjtusei.touchcertifysystemffour.coregraphics;

import android.graphics.Canvas;

public class CGRectFill extends CGNode {
	public CGRectFill(int width,int height){
		super();
		this.setWidth(width);
		this.setHeight(height);
		this.setMoveable(true);
	}
	public void draw(Canvas c){
		c.drawRect(this.getRect(), p);
    	super.draw(c);
	}
}
