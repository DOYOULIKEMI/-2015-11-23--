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
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText nameInput;
    private String practicename;
    private int totalTrainNum;//总共需要训练的次数
    public static String username;
    private String handor;
    private String trainResult;//生成样本特征库的结果
    private boolean delResult;//删除训练文件的结果
    private int trainedNum;//已经训练的次数
    private ProgressDialog progressDialog;
    private String listpath = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/list.txt";
    private AutoCompleteTextView autoTv;
    @SuppressLint("NewApi")
    @Override
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
        
        //隐藏标题栏  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置成全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        setContentView(R.layout.activity_main);
        handor = UserInfo.getRightLeft();
        
//        startService();                 ------------------------------------------------------------------------------><

        ImageButton practiceBtn = (ImageButton)findViewById(R.id.practiceBtn);
        ImageButton approveBtn = (ImageButton)findViewById(R.id.approveBtn);
        ImageButton registerBtn = (ImageButton)findViewById(R.id.registerBtn);
        ImageButton setBtn = (ImageButton)findViewById(R.id.setBtn);
         
        //训练界面跳转
        
        practiceBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
                
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.practice_touch));
                nameInput = new EditText(MainActivity.this);
           	    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			    View layout=inflater.inflate(R.layout.input_alertdialog,null);					
			    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    //nameInput=(EditText)layout.findViewById(R.id.editText1);
           	    autoTv = (AutoCompleteTextView)layout.findViewById(R.id.autoCompleteTextView1);
           	    initAutoComplete("history",autoTv);  
			    Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
				Button negative_btn=(Button) layout.findViewById(R.id.negativeButton);
			   // nameInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			    builder.setView(layout);
           	    builder.setCancelable(false);
				final AlertDialog alg=builder.create();
			    alg.show();	
			    positive_btn.setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                        	alg.cancel();
                        	saveHistory("history",autoTv); 
                            String username1 = autoTv.getText().toString();
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
                                        // 如果已经完成训练次数
                                        showIfTrain();
                                    }else{
                                        // 如果没有完成训练次数
                                        String infoStr = "已完成："+trainedNum+"次；  待完成："+remainTrainNum+"次";
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
        //认证界面跳转
        approveBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
            	
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.approve_touch));
//                            
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.practice_touch));
                nameInput = new EditText(MainActivity.this);
           	    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			    View layout=inflater.inflate(R.layout.testhint,null);					
			    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    //nameInput=(EditText)layout.findViewById(R.id.editText1);
           	    autoTv = (AutoCompleteTextView)layout.findViewById(R.id.autoCompleteTextView1);
           	    initAutoComplete("history22",autoTv);  
			    Button positive_btn=(Button) layout.findViewById(R.id.positiveButton);
				Button negative_btn=(Button) layout.findViewById(R.id.negativeButton);
			   // nameInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			    builder.setView(layout);
           	    builder.setCancelable(false);
				final AlertDialog alg=builder.create();
			    alg.show();	
			    positive_btn.setOnClickListener(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                        	alg.cancel();
                        	saveHistory("history22",autoTv); 
                            String username1 = autoTv.getText().toString();
                            try {
                                practicename= idtoname(username1);
                                Intent intent = new Intent();
                           	    intent.setClass(MainActivity.this, ApproveActivity.class);
                           	    startActivity(intent);                  
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if(username.equals("")){
                                Toast.makeText(MainActivity.this, getString(R.string.username_empty), Toast.LENGTH_SHORT).show();
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
        //设置界面跳转
        setBtn.setOnClickListener(new View.OnClickListener(){
            
            @Override
            public void onClick(View v){
                
            	//((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.set_touch));
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
                
            }
        });
        //注册界面跳转
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
    
    // 提示用户是否训练所有文件生成特征库
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
               
               //训练所有样本
               public void onClick(View v) {
            	   alg.cancel();
                   NSTimer.scheduledTimerWithTimeInterval(0.1, MainActivity.this, "prepareTrain", null, false);
               }
               
            });
        negative_btn.setOnClickListener(new OnClickListener(){

                //重新训练 删除已有的文件
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
        progressDialog = ProgressDialog.show(MainActivity.this, "正在加载...", "数据处理中");
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
			    			(UserInfo.getContext(), "已将 "+MainActivity.username+" 确定为合法用户", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err0")) {
			    			Toast.makeText(UserInfo.getContext(), "请再训练4个用户！", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err1")) {
			    			Toast.makeText(UserInfo.getContext(), "请再训练3个用户！", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err2")) {
			    			Toast.makeText(UserInfo.getContext(), "请再训练2个用户！", Toast.LENGTH_SHORT).show();
			    		}else if (trainResult.equals("err3")) {
			    			Toast.makeText(UserInfo.getContext(), "请再训练1个用户！", Toast.LENGTH_SHORT).show();
			    		}else {
			    			Toast.makeText(UserInfo.getContext(),trainResult +"\n格式有误，请删除文件重试", Toast.LENGTH_LONG).show();
			    		}		
                    }
                });
            }
        });
        t.start();
        
    }
    // 进入训练界面
    public void enterTrain(){
//      finish();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MultiTouchCollectorActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("type", "train");
        intent.putExtra("direction", handor);
        startActivity(intent);
    }
    
    // 进入认证 测试界面
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
                    Toast.makeText(MainActivity.this, "\n\n"+"姓名:   "+name+"\n\n"+"学号: "+id+"\n\n", Toast.LENGTH_SHORT).show();
//                  return name;
                    return id;
                }
            }
            if(id.equals("")){}
            else {
                Toast.makeText(MainActivity.this, "\n\n"+"姓名:  未知的"+"\n\n"+"学号："+id+"\n\n", Toast.LENGTH_SHORT).show();
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
    private void saveHistory(String field,AutoCompleteTextView auto) {  
        String text = auto.getText().toString();  
        SharedPreferences sp = getSharedPreferences("network_url", 0);  
        String longhistory = sp.getString(field, "nothing");  
        if (!longhistory.contains(text + ",")) {  
            StringBuilder sb = new StringBuilder(longhistory);  
            sb.insert(0, text + ",");  
            sp.edit().putString("history", sb.toString()).commit();  
        }  
       }
    private void initAutoComplete(String field,AutoCompleteTextView auto) {  
        SharedPreferences sp = getSharedPreferences("network_url", 0);  
        String longhistory = sp.getString(field, "nothing");  
        String[]  hisArrays = longhistory.split(",");  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
                R.layout.line_list, hisArrays);  
        //只保留最近的50条的记录  
        if(hisArrays.length > 50){  
            String[] newArrays = new String[50];  
            System.arraycopy(hisArrays, 0, newArrays, 0, 50);  
            adapter = new ArrayAdapter<String>(this,  
            		 R.layout.line_list, newArrays);  
        }  
        auto.setAdapter(adapter);  
        auto.setDropDownHeight(350);  
        auto.setThreshold(1);  
        auto.setCompletionHint("最近的5条记录");  
        auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
                AutoCompleteTextView view = (AutoCompleteTextView) v;  
                if (hasFocus) {  
                        view.showDropDown();  
                }  
            }  
        });  
    }  
}
