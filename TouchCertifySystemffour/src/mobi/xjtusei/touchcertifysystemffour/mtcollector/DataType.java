package mobi.xjtusei.touchcertifysystemffour.mtcollector;

import android.graphics.PointF;

public class DataType {
	PointF point;//×ø±ê
	long  time;//Ê±¼ä
	
	DataType (PointF point,long time){
		this.point = point;
		this.time = time;
	}
	
	public PointF getPoint() {
		return point;
	}
	public void setPoint(PointF point) {
		this.point = point;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
