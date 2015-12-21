package mobi.xjtusei.touchcertifysystemffour.handler;

public class DataType {
	double x;//x����
	double y;//y����
	long  time;//ʱ��
	float pressure;//ѹ��
	int id;//��־�ڼ�����ָ
	int distance;//�豸���浽���ߵľ���
	int toolx;//center x tool position
	
	DataType (double x,double y,long time,float p,int id,int distance,int toolx){
		this.x = x;
		this.y = y;
		this.time = time;
		pressure = p;
		this.id = id;
		this.distance = distance;
		this.toolx = toolx;
	}
	DataType (double x,double y,long time,float p,int id){
		this.x = x;
		this.y = y;
		this.time = time;
		pressure = p;
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
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public int getToolx() {
		return toolx;
	}


	public void setToolx(int toolx) {
		this.toolx = toolx;
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
