package mobi.xjtusei.touchcertifysystemffour.mtcollector;

import mobi.xjtusei.touchcertifysystemffour.coregraphics.CGRectFill;
import mobi.xjtusei.touchcertifysystemffour.coregraphics.CGRectStroke;
import android.graphics.Color;

public class PatternRectFill extends CGRectFill {
	public PatternRectFill(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
		this.setColor(Color.BLUE);
	}

	private CGRectStroke dstRect;

	public CGRectStroke getDstRect() {
		return dstRect;
	}

	public void setDstRect(CGRectStroke dstRect) {
		dstRect.setColor(this.getColor());
		this.dstRect = dstRect;
		
	}
	/**
	 * 是否达到目的地
	 * @return
	 */
	public boolean isReachDst(){
		return this.getDstRect().getRect().intersect(this.getRect());
	}
	public void removeFromParent(){
		super.removeFromParent();
		this.getDstRect().removeFromParent();
	}
}
