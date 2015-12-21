package mobi.xjtusei.touchcertifysystemffour.coregraphics;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

public class CGNode {
	private int color = Color.BLACK;
	private PointF position;
	private float width=0;
	private float height=0;
	
	
	private boolean isTouchIn;
	
	private CGNode parentNode;
	
	private boolean isTouchEnable;
	
	private Rect limitTouchRect = null;
	
	public Rect getLimitTouchRect() {
		return limitTouchRect;
	}
	public void setLimitTouchRect(Rect limitTouchRect) {
		this.limitTouchRect = limitTouchRect;
	}
	public boolean isTouchEnable() {
		return isTouchEnable;
	}
	public void setTouchEnable(boolean isTouchEnable) {
		this.isTouchEnable = isTouchEnable;
	}
	protected Paint p = new Paint();
	
	private boolean moveable;
	private boolean isMutipleTouch = false;
	private boolean isInMutipleEnv = false;
	public boolean isInMutipleEnv() {
		return isInMutipleEnv;
	}
	public void setInMutipleEnv(boolean isInMutipleEnv) {
		this.isInMutipleEnv = isInMutipleEnv;
	}
	public boolean isMutipleTouch() {
		return isMutipleTouch;
	}
	public void setMutipleTouch(boolean isMutipleTouch) {
		this.isMutipleTouch = isMutipleTouch;
		if(isMutipleTouch){
			isInMutipleEnv = true;
		}
	}
	public Rect getRect(){
		Rect rect = new Rect();
		rect.left = (int) (this.getPosition().x-this.getWidth()/2);
		rect.right = (int)(this.getPosition().x+this.getWidth()/2);
		rect.bottom = (int)(this.getPosition().y+this.getHeight()/2);
		rect.top = (int)(this.getPosition().y-this.getHeight()/2);
		return rect;
	}
	public CGNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(CGNode parentNode) {
		this.parentNode = parentNode;
	}
	private ArrayList<CGNode> nodes = new ArrayList<CGNode>();
	
	public CGNode(){
		
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
		p.setColor(color);
	}
	public PointF getPosition() {
		return position;
	}
	public void setPosition(PointF position) {
		this.position = position;
	}
	public void setPosition(float x,float y) {
		this.position = new PointF(x,y);
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void addChild(CGNode n){
		n.setParentNode(this);
		nodes.add(n);
	}
	public ArrayList<CGNode> getChildren(){
		return this.nodes;
	}
	public void removeAllChildren(){
		for(int i=0;i<this.nodes.size();i++){
			nodes.remove(i);
		}
	}
	public void removeChild(CGNode n){
		n.setParentNode(null);
		nodes.remove(n);
	}
	public void removeFromParent(){
		if(this.parentNode!=null){
			this.parentNode.removeChild(this);
		}
	}
	
	public void draw(Canvas c){
		for(int i=0;i<this.nodes.size();i++){
			CGNode node = this.nodes.get(i);
			node.draw(c);
		}
	}
	public boolean onCGTouchEvent(MotionEvent event){
		if(this.isTouchEnable){
			for(int i=0;i<this.nodes.size();i++){
				CGNode node = this.nodes.get(i);
				node.onCGTouchEvent(event);
			}
			if(this.isMoveable()){
				float x = 0;
				float y = 0;
				
				if(this.isMutipleTouch&&event.getPointerCount()==2){
					x = (event.getX(0)+event.getX(1))/2;
					y = (event.getY(0)+event.getY(1))/2;
				}else {
					x = event.getX();
					y = event.getY();
				}
				
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					if(this.isInMutipleEnv){
						isTouchIn = true;
					}else if(this.getRect().contains((int)x, (int)y)){
						isTouchIn = true;
					}
				}else if(event.getAction()==MotionEvent.ACTION_MOVE){
					if(isTouchIn){
						if(limitTouchRect!=null&&!isMutipleTouch){
							//如果限定了影响范围 并且不是多点挪动的情况
							if(!limitTouchRect.contains((int)x, (int)y)){
								x = this.position.x;
								y = this.position.y;
								//如果范围内不包括x,y
								if(event.getPointerCount()==2){
									//如果是有两个点 检查另外一个点是否在限定的范围内
									float x1 = event.getX(1);
									float y1 = event.getY(1);
									if(limitTouchRect.contains((int)x1, (int)y1)){
										x = x1;
										y = y1;
									}
								}
							}
						}
						this.position = new PointF(x,y);
					}
				}else if(event.getAction()==MotionEvent.ACTION_POINTER_UP){
					isTouchIn = false;
				}
				//lastPosition = new PointF(x,y);
			}
		}
		return true;
	}
	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
		if(moveable){
			this.isTouchEnable = true;
		}
	}
	public boolean isMoveable() {
		return moveable;
	}
	public float length(PointF p1,PointF p2){
		float length2 = (float) ((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
		return (float) Math.sqrt(length2);
	}
}
