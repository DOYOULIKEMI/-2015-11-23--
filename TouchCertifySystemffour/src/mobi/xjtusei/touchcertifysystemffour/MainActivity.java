package mobi.xjtusei.touchcertifysystemffour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.cocoa4android.ns.NSTimer;

import mobi.xjtusei.touchcertifysystemffour.R;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchCollectorActivity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText nameInput;
    private int totalTrainNum;//�ܹ���Ҫѵ���Ĵ���
    public static String username;
    private String handor;
    private String trainResult;//��������������Ľ��
    private boolean delResult;//ɾ��ѵ���ļ��Ľ��
    private int trainedNum;//�Ѿ�ѵ���Ĵ���
    private ProgressDialog progressDialog;
    private String listpath = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/list.txt";
    private AutoCompleteTextView autoTv;
    @SuppressLint("NewApi")
    @Override
    protect11;
    protected void onCreate(Bundle savedInstanceState) {
        int version = UserInfo.getAndroidSDKVersion();
        if (version>=15) 
        {           
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()                   
            .detectDiskReads()                   
            .detectDiskWrites()                   
            .detectNetwork()                 
            .penaltyLog()                   
            .build());           
             StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()                   
            .detectLeakedSqlLiteObjects()                                   
            .penaltyLog()                   
            .penaltyDeath()                  
            .build());       
        }      
        
        super.onCreate(savedInstanceState);
        UserInfo.setContext(this);
        
        //���ر�����  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //���ó�ȫ��  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        setContentView(R.layout.activity_main);
        handor = UserInfo.getRightLeft();
        
//        startService();                 ------------------------------------------------------------------------------><

        ImageButton practiceBtn = (ImageButton)findViewById(R.id.practiceBtn);
        ImageButton approveBtn = (ImageButton)findViewById(R.id.approveBtn);
        ImageButton registerBtn = (ImageButton)findViewById(R.id.registerBtn);
        ImageButton setBtn = (ImageButton)findViewById(R.id.setBtn);
         
        //ѵ��������ת
        
        practiceBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
                
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.practice_touch));
                nameInput = new EditText(MainActivity.this);
           	    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
           	    autoTv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
			    View layout=inflater.inflate(R.layout.input_alertdialog,null);					
			    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    nameInput=(EditText)layout.findViewById(R.id.editText1);
			    Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
				Button negative_btn=(Button) layout.findViewById(R.id.negativeButton);
			    nameInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			    builder.setView(layout);
           	    builder.setCancelable(false);
				final AlertDialog alg=builder.create();
			    alg.show();	
			    positive_btn.setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                        	alg.cancel();
                            String username1 = nameInput.getText().toString();
                            try {
                                username = idtoname(username1);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if(username.equals("")){
                                Toast.makeText(MainActivity.this, getString(R.string.username_empty), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                totalTrainNum = UserInfo.getTotalTrainNum();
                                trainedNum = UserInfo.getTrainedNum(username);
                                
                                if(trainedNum<0){
                                    Toast.makeText(MainActivity.this, getString(R.string.error_getuserinfo), Toast.LENGTH_SHORT).show();
                                }else{
                                    int remainTrainNum = totalTrainNum-trainedNum;
                                    if(remainTrainNum<=0){
                                        // ����Ѿ����ѵ������
                                        showIfTrain();
                                    }else{
                                        // ���û�����ѵ������
                                        String infoStr = "����ɣ�"+trainedNum+"�Σ�  ����ɣ�"+remainTrainNum+"��";
                                        Toast.makeText(MainActivity.this, infoStr, Toast.LENGTH_SHORT).show();
                                        enterTrain();
                                    }
    
                                }
                                
                            }
                        }
                    }
                )  ;
			    negative_btn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						alg.cancel();
					}});
                
                
                
            }
        });
        //��֤������ת
        approveBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
            	
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.approve_touch));
//            	finish();//------------------------------------------------------------------------------><
            	if(!UserInfo.hasTrained()){
                    //���û��ѵ����
                    Toast.makeText(MainActivity.this, getString(R.string.error_gettrainedinfo), Toast.LENGTH_SHORT).show();
                 }else{   
                	 LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					 View layout=inflater.inflate(R.layout.testhint,null);					
					 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                	 TextView hintname=(TextView)layout.findViewById(R.id.textView2);
                	 hintname.setText("��ǰ�Ϸ��û�Ϊ��"+UserInfo.getDefautUser());
                	 Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
                	 builder.setView(layout);
                	 builder.setCancelable(false);
	 				final AlertDialog alg=builder.create();
				     alg.show();	
                     positive_btn.setOnClickListener(new OnClickListener(){
                             @Override
                             public void onClick(View v) {
                            	 alg.cancel();
                            	 Intent intent = new Intent();
                            	 intent.setClass(MainActivity.this, ApproveActivity.class);
                            	 startActivity(intent);                             
                             }
                         }
                     ) ; 
                                     	 
                 }                 
            }
        });
        //���ý�����ת
        setBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
                
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.set_touch));
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
                
            }
        });
        //ע�������ת
        registerBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
                
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.register_touch));
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                
            }
        });
    }
    
    // ��ʾ�û��Ƿ�ѵ�������ļ�����������
    public void showIfTrain(){
   	    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
	    View layout=inflater.inflate(R.layout.trained_hint,null);					
	    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);	    
	    Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
		Button negative_btn=(Button) layout.findViewById(R.id.negativeButton);
	    builder.setView(layout);
     	builder.setCancelable(false);
	    final AlertDialog alg=builder.create();
		alg.show();	
     
            positive_btn.setOnClickListener(new OnClickListener(){
               
               //ѵ����������
               public void onClick(View v) {
            	   alg.cancel();
                   NSTimer.scheduledTimerWithTimeInterval(0.1, MainActivity.this, "prepareTrain", null, false);
               }
               
            });
        negative_btn.setOnClickListener(new OnClickListener(){

                //����ѵ�� ɾ�����е��ļ�
                public void onClick(View v) {
                	alg.cancel();
                    delResult = UserInfo.delTrainFiles(username);
                    if(delResult){
                        enterTrain();
                    }else{
                        Toast.makeText(MainActivity.this, getString(R.string.error_deluserinfo), Toast.LENGTH_SHORT).show();
                    }
                    
                }
                
            });
            
    }
    public void prepareTrain(){
        progressDialog = ProgressDialog.show(MainActivity.this, "���ڼ���...", "���ݴ�����");
        Thread t = new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    trainResult = UserInfo.trainAllFiles(username);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        progressDialog.dismiss();                        

                        if (trainResult.equals("suc")) {
			    			Toast.makeText
			    			(UserInfo.getContext(), "�ѽ� "+MainActivity.username+" ȷ��Ϊ�Ϸ��û�", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err0")) {
			    			Toast.makeText(UserInfo.getContext(), "����ѵ��4���û���", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err1")) {
			    			Toast.makeText(UserInfo.getContext(), "����ѵ��3���û���", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err2")) {
			    			Toast.makeText(UserInfo.getContext(), "����ѵ��2���û���", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err3")) {
			    			Toast.makeText(UserInfo.getContext(), "����ѵ��1���û���", Toast.LENGTH_SHORT).show();
			    		}else {
			    			Toast.makeText(UserInfo.getContext(),trainResult +"\n��ʽ������ɾ���ļ�����", Toast.LENGTH_LONG).show();
			    		}		
                    }
                });
            }
        });
        t.start();
        
    }
    // ����ѵ������
    public void enterTrain(){
//      finish();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MultiTouchCollectorActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("type", "train");
        intent.putExtra("direction", handor);
        startActivity(intent);
    }
    
    // ������֤ ���Խ���
    public void enterTest(){
//      finish();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MultiTouchCollectorActivity.class);
        intent.putExtra("username", username);
        Log.i("test",username+"");
        intent.putExtra("type", "test");
        intent.putExtra("direction", handor);
        startActivity(intent);      
    }
    
    public String idtoname(final String id) throws Exception{
        String name="";
        String aline;
        ArrayList<String>tlList = new ArrayList<String>();
        File listname = new File(listpath);
        if(!listname.exists())return id;
        else{           
            FileInputStream fin = new FileInputStream(listname);
            InputStreamReader inR = new InputStreamReader(fin);
            BufferedReader bfR  = new BufferedReader(inR);
            while((aline=bfR.readLine())!= null)
            {
                String []tem = aline.split(" ");
                for (int i = 0; i < tem.length; i++) {
                    tlList.add(tem[i]);
                    Log.i("name",tem[i]);
                }
            }
            bfR.close();
            inR.close();
            fin.close();
            for (int i = 0; i < tlList.size()-1; i++) {
                if(tlList.get(i).equals(id))
                {                       
                    name = tlList.get(i+1);
                    Toast.makeText(MainActivity.this, "\n\n"+"����:   "+name+"\n\n"+"ѧ��: "+id+"\n\n", Toast.LENGTH_SHORT).show();
//                  return name;
                    return id;
                }
            }
            if(id.equals("")){}
            else {
                Toast.makeText(MainActivity.this, "\n\n"+"����:  δ֪��"+"\n\n"+"ѧ�ţ�"+id+"\n\n", Toast.LENGTH_SHORT).show();
            }           
            return id;
        }               
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){          
            finish();
            System.exit(0);
        return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void startService(){
		Intent intent = new Intent(this,Mserver.class);
		this.startService(intent);
	}
}
