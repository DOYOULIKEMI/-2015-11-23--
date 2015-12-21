package mobi.xjtusei.touchcertifysystemffour.mtcollector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mobi.xjtusei.touchcertifysystemffour.ApproveActivity;
import mobi.xjtusei.touchcertifysystemffour.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MultiTouchCollectorActivity extends Activity implements SensorEventListener{
	private SensorManager sensorManager;
	private String username_sensor;
	private String filepath_a = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_a";
	private String filepath_a_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_a_l";
	private String filepath_a_linear = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_a_linear";
	private String filepath_a_linear_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_a_linear_l";
	private String filepath_a_gravity = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_a_gravity";
	private String filepath_a_gravity_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_a_gravity_l";
	private String filepath_o = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_o";
	private String filepath_o_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_o_l";
	private String filepath_r = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_r";
	private String filepath_r_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_r_l";
	private String filepath_m = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_m";
	private String filepath_m_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_m_l";
	private String filepath_g = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors/Bdata_g";
	private String filepath_g_l = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/sensors_l/Bdata_g_l";
	private FileWriter op_a;
	private FileWriter op_a_linear;
	private FileWriter op_a_gravity;
	private FileWriter op_o;
	private FileWriter op_r;
	private FileWriter op_m;
	private FileWriter op_g;
	private BufferedWriter pw_a;
	private BufferedWriter pw_a_linear;
	private BufferedWriter pw_a_gravity;
	private BufferedWriter pw_o;
	private BufferedWriter pw_r;
	private BufferedWriter pw_m;
	private BufferedWriter pw_g;
	private Sensor aSensor; 
	private Sensor aLinearSensor;
	private Sensor aGravitySensor;
	private Sensor oSensor;
	private Sensor rSensor;
	private Sensor mSensor;
	private Sensor gSensor;
	
	private String fileName_a;
	private String fileName_a_gravity;
	private String fileName_a_linear;
	private String fileName_o;
	private String fileName_r;
	private String fileName_g;
	private String fileName_m;
	private String handlor;
    @Override
    public void onCreate(Bundle savedInstanceState) {   	  
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置成全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
/*        Log.i("initial", "setcontent");   	   */
        try {
			setContentView(new MultiTouchView(this));
/*			Log.i("initial2", "setcontent");   	   */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        Bundle extras = getIntent().getExtras(); 
        String username = extras.getString("username");//获取当前用户名
        String type = extras.getString("type");//获取标志位
        handlor  = extras.getString("direction");//获取标志位
        MultiTouchView.username = username;
        MultiTouchView.type = type;
        MultiTouchView.handlor = handlor;
/*        Log.i("initial3", MultiTouchView.handlor+"");   	  */ 
        username_sensor = username;
//        restartsensor();                                 --------------------------------------------------------><
    }

    protected void onResume(){
		super.onResume();

	}
    
//  -----------------------------------------------------------------------------------------------
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	@Override
	public void onSensorChanged(SensorEvent event) {	
	}
//  -----------------------------------------------------------------------------------------------	
	/*
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	}
	@Override
	public void onSensorChanged(SensorEvent e) {		
		if (e.sensor.getType() != Sensor.TYPE_ACCELEROMETER&&e.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR&&e.sensor.getType()!=Sensor.TYPE_GRAVITY&&e.sensor.getType()!=Sensor.TYPE_LINEAR_ACCELERATION&&e.sensor.getType() != Sensor.TYPE_GYROSCOPE&&e.sensor.getType() != Sensor.TYPE_MAGNETIC_FIELD&&e.sensor.getType() !=Sensor.TYPE_ORIENTATION )           	
                return;		
		if(e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) 
		{   
			try {
				pw_a.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",1+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		else if(e.sensor.getType() == Sensor.TYPE_GRAVITY)
		{
			try {
				pw_a_gravity.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",2+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		else if(e.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			try {
				pw_a_linear.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",3+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		else if(e.sensor.getType()==Sensor.TYPE_GYROSCOPE)
		{
			try {
				pw_g.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",4+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		  }
		else if(e.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
		{
		    try {
				pw_m.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",5+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		else if(e.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR)
		{
			try {
				pw_r.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",1+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
	    }
		else if(e.sensor.getType()==Sensor.TYPE_ORIENTATION)
		{
			try {
				pw_o.write(e.values[0]+"   "+e.values[1]+"   "+e.values[2]+"  "+e.timestamp+" "+"\r" );
//				Log.i("sensor",6+"   "+e.values[0]+"   "+e.values[1]+"   "+e.values[2]);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
	    }
	
	}
    
    public int filecount(String filepath){
		int a = 0;
		File d = new File(filepath);
		File list[] = d.listFiles();
//		Log.i("wt","len:"+list.length);
		a = list.length;
		return a;
	}
	public void filecreate(String filepath){
		File myfilePath1 = new File(filepath);
		if(!myfilePath1.exists()){
			myfilePath1.mkdirs();
		}
	}
	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	public void restartsensor(){
		String fpath_a;
		String fpath_a_gravity;
		String fpath_a_linear;
		String fpath_g;
		String fpath_m;
		String fpath_o;
		String fpath_r;	
		if(handlor.equals("right")){
			fpath_a = filepath_a;
			fpath_a_gravity = filepath_a_gravity;
			fpath_a_linear = filepath_a_linear;
			fpath_g = filepath_g;
			fpath_m = filepath_m;
			fpath_o = filepath_o;
			fpath_r = filepath_r;
		}else {
			fpath_a = filepath_a_l;
			fpath_a_gravity = filepath_a_gravity_l;
			fpath_a_linear = filepath_a_linear_l;
			fpath_g = filepath_g_l;
			fpath_m = filepath_m_l;
			fpath_o = filepath_o_l;
			fpath_r = filepath_r_l;			
		}
		filecreate(fpath_a+"/"+username_sensor);
		filecreate(fpath_a_gravity+"/"+username_sensor);
		filecreate(fpath_a_linear+"/"+username_sensor);
		filecreate(fpath_g+"/"+username_sensor);
		filecreate(fpath_o+"/"+username_sensor);
		filecreate(fpath_r+"/"+username_sensor);
		filecreate(fpath_m+"/"+username_sensor);
		int ct_a = 1+filecount(fpath_a+"/"+username_sensor);
		int ct_a_gravity = 1+filecount(fpath_a_gravity+"/"+username_sensor);
		int ct_a_linear = 1+filecount(fpath_a_linear+"/"+username_sensor);
		int ct_o = 1+filecount(fpath_o+"/"+username_sensor);
		int ct_r = 1+filecount(fpath_r+"/"+username_sensor);
		int ct_g = 1+filecount(fpath_g+"/"+username_sensor);
		int ct_m = 1+filecount(fpath_m+"/"+username_sensor);
		fileName_a = fpath_a+"/"+username_sensor+"/"+"a_"+username_sensor+ct_a+".txt";
		fileName_a_gravity = fpath_a_gravity+"/"+username_sensor+"/"+"a_gravity"+username_sensor+ct_a_gravity+".txt";
		fileName_a_linear = fpath_a_linear+"/"+username_sensor+"/"+"a_linear"+username_sensor+ct_a_linear+".txt";
		fileName_o = fpath_o+"/"+username_sensor+"/"+"o_"+username_sensor+ct_o+".txt";
		fileName_r = fpath_r+"/"+username_sensor+"/"+"r_"+username_sensor+ct_r+".txt";
		fileName_g = fpath_g+"/"+username_sensor+"/"+"g_"+username_sensor+ct_g+".txt";
		fileName_m = fpath_m+"/"+username_sensor+"/"+"m_"+username_sensor+ct_m+".txt";
		aSensor= sensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
		aGravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		aLinearSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	    rSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR); 
	    gSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	    mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		oSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    sensorManager.registerListener(this,aSensor,SensorManager.SENSOR_DELAY_FASTEST);	
	    sensorManager.registerListener(this,aGravitySensor,SensorManager.SENSOR_DELAY_FASTEST);
	    sensorManager.registerListener(this,aLinearSensor,SensorManager.SENSOR_DELAY_FASTEST);
	    sensorManager.registerListener(this,rSensor,SensorManager.SENSOR_DELAY_FASTEST);
	    sensorManager.registerListener(this,gSensor,SensorManager.SENSOR_DELAY_FASTEST);
	    sensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_FASTEST);
	    sensorManager.registerListener(this,oSensor,SensorManager.SENSOR_DELAY_FASTEST);
	    
	    Log.i("collect", fileName_a);
	    
		try {
			op_a = new FileWriter(fileName_a);
			Log.i("sensor4",fileName_a);
			pw_a = new BufferedWriter(op_a,102400);
			op_a_gravity = new FileWriter(fileName_a_gravity);
			pw_a_gravity = new BufferedWriter(op_a_gravity,102400);
			op_a_linear = new FileWriter(fileName_a_linear);
			pw_a_linear = new BufferedWriter(op_a_linear,102400);
			op_o = new FileWriter(fileName_o);
			pw_o = new BufferedWriter(op_o,102400);
			op_r = new FileWriter(fileName_r);
			pw_r = new BufferedWriter(op_r,102400);
			op_g = new FileWriter(fileName_g);
			pw_g = new BufferedWriter(op_g,102400);
			op_m = new FileWriter(fileName_m);
			pw_m = new BufferedWriter(op_m,102400);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
    @Override 
	public boolean onTouchEvent(MotionEvent event){
		//清除
//		super.onTouchEvent(event);
		if(event.getAction() == MotionEvent.ACTION_DOWN&&
				event.getX()<MultiTouchView.pos[0][2]&&event.getX()>MultiTouchView.pos[0][0]&&
				event.getY()<MultiTouchView.pos[0][3]&&event.getY()>MultiTouchView.pos[0][1]){
			Log.i("sensor2","清除");
			quitsensor();
			dellatestfile();
			restartsensor();
		}
		//确定
		if(event.getAction() == MotionEvent.ACTION_DOWN&&
				event.getX()<MultiTouchView.pos[1][2]&&event.getX()>MultiTouchView.pos[1][0]&&
				event.getY()<MultiTouchView.pos[1][3]&&event.getY()>MultiTouchView.pos[1][1]){
			Log.i("sensor2","确定");					
			if(!MultiTouchView.getmultifingers()){
				quitsensor();
				dellatestfile();
				restartsensor();
			}else
			{
				quitsensor();
				restartsensor();
			}
		}
		//退出
		if(event.getAction() == MotionEvent.ACTION_DOWN&&
				event.getX()<MultiTouchView.pos[2][2]&&event.getX()>MultiTouchView.pos[2][0]&&
				event.getY()<MultiTouchView.pos[2][3]&&event.getY()>MultiTouchView.pos[2][1]){
			Log.i("sensor2","退出");
			quitsensor();
			dellatestfile();
		}
		return super.onTouchEvent(event);
	}

	private void dellatestfile() {
		// TODO Auto-generated method stub
		File file1 = new File(fileName_a); 
		if (file1.exists()&&file1.isFile())file1.delete();
		File file2 = new File(fileName_a_gravity); 
		if (file2.exists()&&file2.isFile())file2.delete();
		File file3 = new File(fileName_a_linear); 
		if (file3.exists()&&file3.isFile())file3.delete();
		File file4 = new File(fileName_o); 
		if (file4.exists()&&file4.isFile())file4.delete();
		File file5 = new File(fileName_r); 
		if (file5.exists()&&file5.isFile())file5.delete();
		File file6 = new File(fileName_g); 
		if (file6.exists()&&file6.isFile())file6.delete();
		File file7 = new File(fileName_m); 
		if (file7.exists()&&file7.isFile())file7.delete();
		
	}

	public void quitsensor() {
		// TODO 自动生成的方法存根
			Log.i("back", "123");
			sensorManager.unregisterListener(this,aSensor);	
		    sensorManager.unregisterListener(this,aGravitySensor);
		    sensorManager.unregisterListener(this,aLinearSensor);
		    sensorManager.unregisterListener(this,rSensor);
		    sensorManager.unregisterListener(this,gSensor);
		    sensorManager.unregisterListener(this,mSensor);
		    sensorManager.unregisterListener(this,oSensor);
			    
		    // 刷新缓存区
		    try {
				pw_a.flush();
				pw_a.close();
				pw_a_gravity.flush();
				pw_a_gravity.close();
				pw_a_linear.flush();
				pw_a_linear.close();
				pw_o.flush();
				pw_o.close();
				pw_r.flush();
				pw_r.close();
				pw_g.flush();
				pw_g.close();
				pw_m.flush();
				pw_m.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			Log.i("on","onDestroy");
	        try {
	        	op_a.close();
	        	op_a_gravity.close();
	        	op_a_linear.close();
	        	op_m.close();
	        	op_g.close();
				op_o.close();
				op_r.close();		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ){			
			quitsensor();
			dellatestfile();
			Log.i("fh","删除2");
			Intent intent = new Intent();
            intent.setClass(MultiTouchCollectorActivity.this, MainActivity.class);
            startActivity(intent);
		return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	
	
	   
}