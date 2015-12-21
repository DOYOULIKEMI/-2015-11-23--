package dataprocessing;


public class DataType {
	double y;//x坐标
	double x;//y坐标
	long  time;//时间
	float pressure;//压力
	int id;//标志第几根手指
	int tool;//设备表面到工具的距离
	int touch;//center x tool position
	
	DataType (double y,double x,long time,float p,int id,int tool,int touch){
		this.y = y;
		this.x = x;
		this.time = time;
		this.pressure = p;
		this.id = id;
		this.tool = tool;
		this.touch = touch;
	}
	DataType (double y,double x,long time,float p,int id){
		this.y = y;
		this.x = x;
		this.time = time;
		this.pressure = p;
		this.id = id;
	}
	public float getPressure() {
		return pressure;
	}


	public void setPressure(float pressure) {
		this.pressure = pressure;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getDistance() {
		return tool;
	}


	public void setDistance(int touch) {
		this.tool = touch;
	}


	public int getToolx() {
		return touch;
	}


	public void setToolx(int touch) {
		this.touch = touch;
	}
	
	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
