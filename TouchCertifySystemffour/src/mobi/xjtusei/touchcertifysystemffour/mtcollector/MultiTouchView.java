package mobi.xjtusei.touchcertifysystemffour.mtcollector;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.cocoa4android.ns.NSTimer;

import dataprocessing.dataSave;

import mobi.xjtusei.touchcertifysystemffour.MainActivity;
import mobi.xjtusei.touchcertifysystemffour.R;
import mobi.xjtusei.touchcertifysystemffour.R.drawable;
import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.coregraphics.CGView;
import android.R.integer;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.audiofx.BassBoost.Settings;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MultiTouchView extends CGView {
	private ProgressDialog progressDialog;
	private String trainRes;
	private boolean testRes;
    private SoundPool sp;
    private int music;
	private static boolean multifingers;
	private static boolean sumfingers;
	private CollectDataLoader loader;
	public static String username = "";//�û���
	public static String type;//��ʾ��ѵ��������֤
	public static String handlor;
	private final int count = 8;//�϶��ܴ���
	private final int count4s = 4;//���㴥���϶��ܴ���
	private int patternId = 0;//��ǰ�ڼ����϶�
	private int detailIndex = 0;
	static public ArrayList<ArrayList<Float>> fplot = new ArrayList<ArrayList<Float>>();
	public static int[][] pos= new int[3][4];
	private EditText wrongUserNameInput;
	private boolean touchCollecting = false; 
	private boolean uploadRes;	

    private StringBuffer saveDataStr;//�洢����    
    
    private ArrayList<PatternRectFill> rects;
    
    private static final String TAG = "MultiTouchView";
    
	static {
		System.loadLibrary("MultiTouchView");
	}
	
	private native String CollectData();
	private native int Init(int fileFlag);
	private native int UnInit();
	
	private int totalTrainNum = -1;//�ܹ���Ҫѵ���Ĵ���
	private int trainedNum = -1;//�Ѿ�ѵ���Ĵ���
	public static String sdcard = android.os.Environment.getExternalStorageDirectory().toString();

    public MultiTouchView(Context context) throws Exception {  
        super(context);        
        init();      

    }   
   
    public void init() throws Exception{
    	File destFileDir = new File(UserInfo.destDir); 
    	/*try {
    		String command = "chmod 777 " + destFileDir.getAbsolutePath();
    		Process process = Runtime.getRuntime().exec("su");
    		DataOutputStream os = new DataOutputStream(process.getOutputStream());
    		os.writeBytes(command + "\n");
    		os.writeBytes("exit\n");
    		os.flush();
    		process.waitFor();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}*/
    	saveDataStr = new StringBuffer();
    	multifingers = true;
    	sumfingers = false;
    	//��ʼ���ɼ�����
    	Init(2);//note:2,ace:1 �Ž�ƽ�壺2,
    	//��ʼ�ɼ�����
//    	----------------------------------------------------------------------><    	
/*    	loader = new CollectDataLoader(); 
    	loader.start();*/
//    	----------------------------------------------------------------------><
 //   	this.setOnTouchListener(new PicOnTouchListener());
 //   	Log.i("initial", pos[0][0]+"");   	    	
    }
 
    public void surfaceCreated(SurfaceHolder holder) {
    	super.surfaceCreated(holder);    	    	
//    	--------------------------------------------------------------------------------><
/*    	PatternFactory.widthPixels = this.getWidthPixels();
    	PatternFactory.heightPixels = this.getHeightPixels();
    	rects = PatternFactory.getRects(patternId, detailIndex);
    	for(int i=0;i<rects.size();i++){
    		PatternRectFill rect = rects.get(i);
    		this.addChild(rect);
    		this.addChild(rect.getDstRect());
    	}*/
//    	--------------------------------------------------------------------------------><
    	this.typeCG=type;
    	touchCollecting = true;
	   	if(handlor.equals("right")){
			pos[0][0] = 50;		pos[0][1] = 50;		pos[0][2] = 200;		pos[0][3] = 150;
			pos[1][0] = 50;		pos[1][1] = 250;		pos[1][2] = 200;		pos[1][3] = 350;
			pos[2][0] = 50;		pos[2][1] = 450;		pos[2][2] = 200;		pos[2][3] = 550;
		}else {
			pos[0][0] = 1080;		pos[0][1] = 50;		pos[0][2] = 1230;		pos[0][3] = 150;
			pos[1][0] = 1080;		pos[1][1] = 250;		pos[1][2] = 1230;		pos[1][3] = 350;
			pos[2][0] = 1080;		pos[2][1] = 450;		pos[2][2] = 1230;		pos[2][3] = 550;
		}
	   	trainedNumCG=UserInfo.getTrainedNum(username);
	   	remainTrainNumCG=UserInfo.getTotalTrainNum()-trainedNumCG;
    	this.startTimer();
    }
    // ��������
    private void collectDidComplete() throws Exception{  	
    	saveDataStr.append(-1+" "+-1+" "+System.currentTimeMillis()+" "+0+" "+0+" "+0+" "+0+"\n");
    	Log.i("sds",saveDataStr.toString()+"");    	
    	//saveDataStr.append(-1+" "+-1+" "+System.currentTimeMillis()+"\n");
    	// �ɼ�������� �Ƴ�����
//    	thien();
		// �ر�
/*		UnInit();
		loader.interrupt();*/
		File destFileDir = new File(UserInfo.destDir); 
/*		try {

			String command = "chmod 660 " + destFileDir.getAbsolutePath();
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();    		
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		touchCollecting = false;
		// �ϴ�����		
		if (multifingers == true && sumfingers == true){
			prepareUpload();
		}else if(multifingers == false) {
			Toast.makeText
			(this.getContext(), "������ָ���ݣ������²ɼ���", Toast.LENGTH_SHORT).show();
/*			UnInit();
			loader.interrupt();*/
			restart();
		}else if(sumfingers == false) {
			Toast.makeText
			(this.getContext(), "������ָ���ݣ������²ɼ���", Toast.LENGTH_SHORT).show();
/*			UnInit();
			loader.interrupt();*/
			restart();
		}
    }
    
    public void restart() throws Exception{
//		rootNode.removeAllChildren();
//    	------------------------------------------------------------------------------><
/*		patternId = 0;
		detailIndex = 0;
		rects = PatternFactory.getRects(patternId, detailIndex);
    	for(int i=0;i<rects.size();i++){
    		PatternRectFill rect = rects.get(i);
    		this.addChild(rect);
    		this.addChild(rect.getDstRect());
    	}*/
//    	------------------------------------------------------------------------------><
    	touchCollecting = true;
    	for(int fresh=0;fresh<maxcount;fresh++)
    	{
    		touchfingertimes[fresh]=0;
    	}
        for(int i=0;i<maxcount;i++)
        {
        mPath[i].reset();	
        }
    	this.doDraw();
    	init();
	}
       
    @Override  
    public boolean onTouchEvent(MotionEvent event) {
    	Log.i("touchtype",event.getAction()+"");
    	Log.i("CGNode", "start");    	
    	super.onTouchEvent(event);
    	Log.i("touchxy:","����");
    	this.stopTimer();
//    	Log.i("fplot", MultiTouchView.fplot+"");    	
    	if(true){
    		if(event.getAction() == MotionEvent.ACTION_DOWN&&
    				event.getX()<200&&event.getX()>0&&
    				event.getY()<170&&event.getY()>0){
    			Log.i("fh","ɾ��");
        		((Activity) MultiTouchView.this.getContext()).finish();
    			System.exit(0);
    		}
    		if(event.getAction() == MotionEvent.ACTION_DOWN&&
    				event.getX()<MultiTouchView.pos[0][2]&&event.getX()>MultiTouchView.pos[0][0]&&
    				event.getY()<MultiTouchView.pos[0][3]&&event.getY()>MultiTouchView.pos[0][1]){
    			try {
    				Log.i("sensor2","���2");
//    				this.removeAllChildren();
/*    				UnInit();
    				loader.interrupt();*/
					//restart();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			return false;
    		} 	
	    	if(event.getAction() == MotionEvent.ACTION_DOWN&&
					event.getX()<MultiTouchView.pos[1][2]&&event.getX()>MultiTouchView.pos[1][0]&&
					event.getY()<MultiTouchView.pos[1][3]&&event.getY()>MultiTouchView.pos[1][1]){	
	    		//��ǰС�ڽ�����
	    		try {
	    			Log.i("sensor2","ȷ��2");
					//collectDidComplete();					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		//�������ÿ�ε�����
//	    		saveDataStr.append(-1+" "+-1+" "+System.currentTimeMillis()+" "+0+" "+0+"\n");
	    		return false;
	    	}
	    	if(event.getActionMasked()== MotionEvent.ACTION_UP)
	    	{
	    		try {
	    			Log.i("�Զ������ɼ�","ȷ��2");
	    			Thread.sleep(300);
	    			MultiTouchView.fplot.clear();
				    smcir.clear();
					collectDidComplete();
					 //restart();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		return false;
	    	}	    	
//------------------------------------------------------------------------------------------><	    	
	    	int ptn = event.getPointerCount();	
	    	if(ptn>maxcount)
	    	{
	    		maxcount=ptn;
	    	}
	    	this.fingercount=ptn;
	    	Log.i("touchxy+",ptn+"");
	    	for (int i = 0;i<ptn;i++){
	    		String tmp2 = (getHeightPixels()-(int)event.getY(i))+" "+(int)(event.getX(i))+" "+event.getEventTime()+" "+event.getPressure(i)+" "+
	    				event.getPointerId(i)+" "+(int)event.getTouchMajor(i)+" "+(int)event.getToolMajor(i)+"\n";
	    		saveDataStr.append(tmp2);	
	    		Log.i("posxy",getHeightPixels()+"");
				Log.i("posxy",(getHeightPixels()-(int)event.getY(i))+" "+(int)(event.getX(i)));
	    		
	    		String tmp = (int)event.getX(i)+" "+(int)event.getY(i)+" "+event.getEventTime()+" "+event.getPressure(i)+" "+
	    				event.getPointerId(i)+" "+(int)event.getTouchMajor(i)+" "+(int)event.getToolMajor(i)+"\n";
				Log.i("directtouch",tmp);							
				String [] temp = tmp.split(" ");
				ArrayList<Float> tempxy = new ArrayList<Float>();
				tempxy.add(Float.parseFloat(temp[0]));
				tempxy.add(Float.parseFloat(temp[1]));
				tempxy.add(Float.parseFloat(temp[4]));
				Log.i("touchxy+",tempxy+"");
				fplot.add(tempxy);	
	    	
				 if(event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN||event.getActionMasked()==MotionEvent.ACTION_DOWN)
				 {      touchfingertimes[event.getPointerId(i)]++; 
				     if (touchfingertimes[event.getPointerId(i)]==1)
				     { this.mPath[event.getPointerId(i)]=new Path();
					    mPath[event.getPointerId(i)].reset();				  
						this.mPath[event.getPointerId(i)].moveTo(event.getX(i),event.getY(i)-20);
						float cX = event.getX(i);
						float cY = (2*event.getY(i)-30)/2;						 
						this.mPath[event.getPointerId(i)].quadTo(event.getX(i),event.getY(i)-20,cX,cY);
						float dX = event.getX(i);
						float dY = (2*event.getY(i)-10)/2;						 
						this.mPath[event.getPointerId(i)].quadTo(event.getX(i),event.getY(i)-10,dX,dY);
						
						this.startX[event.getPointerId(i)]=event.getX(i);
					    this.startY[event.getPointerId(i)]=event.getY(i);
					    }
						Log.i("begin", "begin");
						
						Log.i("number",startY[i]+"");
					     
			      }
				 if(event.getAction()==MotionEvent.ACTION_MOVE)
					 
				 { 
					Log.i("process", "process");				
					final float dx = Math.abs(this.startX[event.getPointerId(i)]-event.getX(i));  
					final float dy = Math.abs(this.startY[event.getPointerId(i)]-event.getY(i));  
					if (dx >= 3 || dy >= 3)  
				    {  
				         //���ñ��������ߵĲ�����Ϊ�����յ��һ��  
				    float cX = (startX[event.getPointerId(i)]+event.getX(i)) / 2;  
				    float cY = (startY[event.getPointerId(i)]+event.getY(i)) / 2;	
				   // this.mPath[i].moveTo(startX[i], startY[i]);
				    this.mPath[event.getPointerId(i)].quadTo(startX[event.getPointerId(i)],startY[event.getPointerId(i)],cX,cY);	
				   // this.mPath[i].lineTo(event.getX(i), event.getY(i));
				    this.startX[event.getPointerId(i)]=event.getX(i);
				    this.startY[event.getPointerId(i)]=event.getY(i);
				    Log.i("number",startY[i]+"");
				    }
				 }				
				 				
				}
			
//------------------------------------------------------------------------------------------><
	    	/*if(event.getAction() == MotionEvent.ACTION_DOWN&&
					event.getX()<MultiTouchView.pos[2][2]&&event.getX()>MultiTouchView.pos[2][0]&&
					event.getY()<MultiTouchView.pos[2][3]&&event.getY()>MultiTouchView.pos[2][1]){
	    		Log.i("sensor2","�˳�2");
	    		((Activity) MultiTouchView.this.getContext()).finish();
//	        	System.exit(0);
	    	}*/
	    	this.doDraw();
    	}
    	Log.i("CGNode", "end");
        return true;  
    }    

    // �ϴ������ݵĴ���
    public void uploadDidEnd(){
    	if(type.equals("train")){
			// ������ϴ���ѵ���ļ�
			if(uploadRes){
				int totalTrainNum = UserInfo.getTotalTrainNum();
				int trainedNum = UserInfo.getTrainedNum(username);
				int remainTrainNum = totalTrainNum-trainedNum;
				this.trainedNumCG=trainedNum;
				this.remainTrainNumCG=remainTrainNum;
				String infoStr = "����ɣ�"+trainedNum+"�Σ�  ����ɣ�"+remainTrainNum+"��";
				Toast.makeText(this.getContext(), infoStr, Toast.LENGTH_SHORT).show();
				
				AlertDialog.Builder builder;
				if(trainedNum<totalTrainNum){

					try {
						restart();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(trainedNum==totalTrainNum){
					//�Ѿ���ɹ涨������ѵ��
					File AllUsersFile = new File(dataSave.systemDir+"/trainData_fixed");
				    String []AllUserslist = AllUsersFile.list();
				    if (AllUserslist.length>=4) {
				    	builder = new AlertDialog.Builder(this.getContext());
				    	builder.setTitle(R.string.info_uploadsuccess)
				    	.setCancelable(false)
				    	.setIcon(android.R.drawable.ic_dialog_info)
				    	.setPositiveButton("��������������", new DialogInterface.OnClickListener() {
				    		
				    		//ѵ����������
				    		public void onClick(DialogInterface dialog, int id) {
				    			NSTimer.scheduledTimerWithTimeInterval(0.1, MultiTouchView.this, "prepareTrain", null, false);
				    		}
				    		
				    	})
				    	.setNegativeButton("�˳�", new DialogInterface.OnClickListener(){
				    		
				    		//����������
				    		public void onClick(DialogInterface dialog, int which) {
				    			((Activity) MultiTouchView.this.getContext()).finish();
				    		}
				    		
				    	})
				    	.show();
				    }else {
				    	builder = new AlertDialog.Builder(this.getContext());
				    	builder.setTitle("����"+(4-AllUserslist.length)+"���û�����ѵ����")
				    	.setCancelable(false)
				    	.setIcon(android.R.drawable.ic_dialog_info)
				    	.setNegativeButton("�˳�", new DialogInterface.OnClickListener(){
				    		
				    		//����������
				    		public void onClick(DialogInterface dialog, int which) {
				    			((Activity) MultiTouchView.this.getContext()).finish();
				    		}
				    		
				    	})
				    	.show();
					}						
				}
			}
			else{
				AlertDialog.Builder builder2 = new AlertDialog.Builder(this.getContext());
				builder2.setTitle(R.string.info_uploadfail)
			       .setCancelable(false)
			       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			    	   
			    	   //����ѵ��
			           public void onClick(DialogInterface dialog, int id) {
			        	   NSTimer.scheduledTimerWithTimeInterval(0.1, MultiTouchView.this, "restart", null, false);
			        	   //restart();
			           }
			           
			        }).show();					
				}
		}
		else if(type.equals("test")){
			// �ϴ�������֤�ļ�
			AlertDialog.Builder builder;
			if(!uploadRes){
				builder = new AlertDialog.Builder(this.getContext());
				builder.setTitle(R.string.info_testuploadfail)
			       .setCancelable(false)
			       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			    	   
			    	   //����
			    	   public void onClick(DialogInterface dialog, int id) {
//--------------------------------------------------------------------------------------------------><			        	   
			    		   /*((Activity) MultiTouchView.this.getContext()).finish();
			        	   System.exit(0);*/
/*			    		   UnInit();
			    		   loader.interrupt();*/
			    		   try {
			    			   restart();
			    		   } catch (Exception e) {
			    			   // TODO Auto-generated catch block
			    			   e.printStackTrace();
			    		   }
//--------------------------------------------------------------------------------------------------><						
			    	   }
			       }).show();
			}else{ 
				prepareCertify();
			}
			
		}
    }
	
    // �ϴ�����
    public void prepareUpload(){
		progressDialog = ProgressDialog.show(this.getContext(), "���ڼ���...", "�����ϴ���");
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Log.i("test","run");
					Log.i("test",username+" "+type+"");
					uploadRes = MultiTouchUtil.uploadData(saveDataStr.toString(),username,type);
					Log.i("uploadRes",uploadRes+"");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Activity) MultiTouchView.this.getContext()).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						 progressDialog.dismiss();
						 uploadDidEnd();
					}
				});				
			}
		});
		t.start();		
		
//		uploadRes = MultiTouchUtil.uploadData(saveDataStr.toString(),username,type);
//		uploadDidEnd();
	}
    
    // ѵ�������ļ�
    public void prepareTrain(){
		progressDialog = ProgressDialog.show(this.getContext(), "���ڼ���...", "���ݴ�����");
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					trainRes = UserInfo.trainAllFiles(username);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Activity) MultiTouchView.this.getContext()).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {						
						progressDialog.dismiss();						
						System.exit(0);			     			        	 
					}
				});
			}
		});
		t.start();	
	}
    
    // ��֤
    public void prepareCertify(){
		progressDialog = ProgressDialog.show(this.getContext(), "���ڼ���...", "�����֤��");
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					testRes = UserInfo.certify();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				((Activity) MultiTouchView.this.getContext()).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						progressDialog.dismiss();
						int title = R.string.info_testsuccess;
						int Icon = R.drawable.suc;
						if(testRes){
							sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
							music = sp.load(MultiTouchView.this.getContext(), R.raw.song, 1);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							sp.play(music, 1, 1, 0, 0, 1);
							title = R.string.info_testsuccess;
							Icon = R.drawable.suc;
							LayoutInflater inflater = LayoutInflater.from(((Activity) MultiTouchView.this.getContext()));
							View layout=inflater.inflate(R.layout.alertdialog_success,null);
							Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
							Button negative_btn=(Button) layout.findViewById(R.id.negativeButton);
							AlertDialog.Builder builder = new AlertDialog.Builder(MultiTouchView.this.getContext());					
							builder.setView(layout);
							builder.setCancelable(false);
							
							final AlertDialog alg=builder.create();
								
							alg.show();	
							//alg.setContentView(layout);
						     positive_btn.setOnClickListener(new OnClickListener(){
						    	   
						    	   //����
						           public void onClick(View v) {
/*						        	   UnInit();
						        	   loader.interrupt();*/
						        	    long time = System.currentTimeMillis();
						        	    String oldPath = sdcard+"/CertSystemData4/CDataPocessing/TrainTxT";
						        	    String newPath=" ";
						        	    if(MainActivity.practicename==UserInfo.getDefautUser())
										{
						        	    	 newPath = sdcard+"/CertSystemData4/SaveData/"+MainActivity.practicename+"/�Ϸ��û���������+"+time;						
										}
						        	    else
						        	    {
						        	    	 newPath = sdcard+"/CertSystemData4/SaveData/"+MainActivity.practicename+"/�Ƿ��û��쳣����"+UserInfo.getDefautUser()+"��ģ��+"+time;	
										}
						        	    copyFolder(oldPath, newPath);
						        	   try {
						        		   
						        	    alg.cancel();
										restart();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						           }
						           
						        });
							        negative_btn.setOnClickListener(new OnClickListener(){
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										alg.cancel();
										AlertDialog.Builder builder2;
										wrongUserNameInput = new EditText(MultiTouchView.this.getContext());
										wrongUserNameInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
										builder2 = new AlertDialog.Builder(MultiTouchView.this.getContext());
								    	builder2.setTitle(R.string.username_empty)
						                .setIcon(android.R.drawable.ic_dialog_info)  
						                .setView(wrongUserNameInput)
						                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												long time = System.currentTimeMillis();
												String input = wrongUserNameInput.getText().toString();									
												String oldPath = sdcard+"/CertSystemData4/CDataPocessing/TrainTxT";
												String newPath = sdcard+"/CertSystemData4/SaveData/"+UserInfo.getDefautUser()+"/�Ƿ��û�"+input+"����+"+time;
												copyFolder(oldPath, newPath);
												try {
													restart();
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
						                })
						                .setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
						                	@Override
											public void onClick(DialogInterface dialog, int which){
						                		try {
													restart();
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						                	}
						                })
						                .show();
									}
								});
								
						}else{
							title = R.string.info_testfail;
							Icon = R.drawable.err;
							
							Vibrator vibrator = (Vibrator)MultiTouchView.this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
							long [] pattern = {100,500,600,1000};
							vibrator.vibrate(pattern,-1);
							LayoutInflater inflater = LayoutInflater.from(((Activity) MultiTouchView.this.getContext()));
							View layout=inflater.inflate(R.layout.activity_fail,null);
							Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
							Button negative_btn=(Button) layout.findViewById(R.id.negativeButton);
							AlertDialog.Builder builder = new AlertDialog.Builder(MultiTouchView.this.getContext());					
							builder.setView(layout);
							builder.setCancelable(false);
							
							final AlertDialog alg=builder.create();
								
							alg.show();	
							 positive_btn.setOnClickListener(new OnClickListener(){
						    	   
						    	   //����
						           public void onClick(View v) {
/*						        	   UnInit();
						        	   loader.interrupt();*/
						        	   long time = System.currentTimeMillis();
						        	    String oldPath = sdcard+"/CertSystemData4/CDataPocessing/TrainTxT";
						        	    String newPath=" ";
						        	    if(MainActivity.practicename==UserInfo.getDefautUser())
										{
						        	    	 newPath = sdcard+"/CertSystemData4/SaveData/"+MainActivity.practicename+"/�Ϸ��û��޷�����+"+time;						
										}
						        	    else
						        	    {
						        	    	 newPath = sdcard+"/CertSystemData4/SaveData/"+MainActivity.practicename+"/�Ƿ��û������ܾ�"+UserInfo.getDefautUser()+"��ģ��+"+time;	
										}
						        	    copyFolder(oldPath, newPath);
						        	   try {
						      		   
						        		   alg.dismiss();
										   restart();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						           }
						           
						        });
							 negative_btn.setOnClickListener(new OnClickListener(){
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										alg.dismiss();
										AlertDialog.Builder builder2;
										wrongUserNameInput = new EditText(MultiTouchView.this.getContext());
										builder2 = new AlertDialog.Builder(MultiTouchView.this.getContext());
								    	builder2.setTitle(R.string.username_empty)
						                .setIcon(android.R.drawable.ic_dialog_info)  
						                .setView(wrongUserNameInput)
						                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												long time = System.currentTimeMillis();
												String input = wrongUserNameInput.getText().toString();
												String oldPath = sdcard+"/CertSystemData4/CDataPocessing/TrainTxT";
												String newPath = sdcard+"/CertSystemData4/SaveData/"+input+"/�Ϸ��û�����+"+time;
												String oldPath1 = sdcard+"/CertSystemData4/CDataPocessing/TrainTxT/train.txt";
												String newPath1 = sdcard+"/CertSystemData4/trainData_fixed/"+input+"/train.txt";
												copyFolder(oldPath, newPath);
												try {
													restart();
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}//CopySingleFile(oldPath1, newPath1);
											}
						                })
						                .setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub					
												try {
													restart();
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}//CopySingleFile(oldPath1, newPath1);
											}
						                })
						                .show();
									}
								});
								
						}
						
					}
				});
			}
		});
		t.start();
		
	}
    
    public static void setmultifingers(boolean mf){
    	multifingers = mf;
    }
    
    public static void setsumfingers(boolean sf){
    	sumfingers = sf;
    }
    
    public static boolean getmultifingers(){
    	return multifingers;
    }    
    
    // �ɼ����ݵ��߳���
	class CollectDataLoader extends Thread{		
		public void run(){
			try {
				while(!this.isInterrupted())
				{
					String tempRes = CollectData();
					Log.i("touchxy:",tempRes);
					if(tempRes==null||tempRes.equals(""))
					{
						continue; 
					}else
					{
						saveDataStr.append(tempRes);
						String [] temp2 = tempRes.split("\n");
						
						for (int i = 0;i<temp2.length;i++){
							String [] temp = temp2[i].split(" ");
							ArrayList<Float> tempxy = new ArrayList<Float>();
							tempxy.add(Float.parseFloat(temp[0]));
							tempxy.add(Float.parseFloat(temp[1]));
							tempxy.add(Float.parseFloat(temp[4]));
							Log.i("touchxy+",tempxy+"");
							fplot.add(tempxy);
							Log.i("fplot2",tempxy+"");
						}
					}
				} 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ){			
//			this.removeAllChildren();
/*			UnInit();
			loader.interrupt();*/
			Log.i("fh","ɾ��");
    		((Activity) MultiTouchView.this.getContext()).finish();
			System.exit(0);
		    return false;
		}
		return super.onKeyDown(keyCode, event);
	}
		public void copyFolder(String oldPath, String newPath){
		try{
			(new File(newPath)).mkdirs();
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for(int i = 0; i<file.length; i++){
				if(oldPath.endsWith(File.separator)){
					temp = new File(oldPath+file[i]);
				}
				else{
					temp = new File(oldPath+File.separator+file[i]);
				}
				
				if(temp.isFile()){
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024*5];
					int len;
					while((len = input.read(b)) != -1){
						output.write(b,0,len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if(temp.isDirectory()){//��������ļ���   
	                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);   
	            }
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void CopySingleFile(String oldPath,String newPath){
		try{
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()){
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ( (byteread = inStream.read(buffer)) != -1) { 
					bytesum += byteread;   //�ֽ��� �ļ���С 
					System.out.println(bytesum); 
					fs.write(buffer, 0, byteread); 
					}
				inStream.close();
				fs.close();
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
		Toast.makeText
		(this.getContext(), "����������ݣ�", Toast.LENGTH_SHORT).show();
	}

}  




