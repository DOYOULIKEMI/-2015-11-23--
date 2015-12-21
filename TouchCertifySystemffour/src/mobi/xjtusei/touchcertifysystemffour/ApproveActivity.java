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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ApproveActivity extends Activity {

    private String username;
    private String handor;
    private ProgressDialog progressDialog;
    private String listpath = Environment.getExternalStorageDirectory().getPath()+"/CertSystemData4/list.txt";
    
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
        handor = UserInfo.getRightLeft();
                
        if(!UserInfo.hasTrained()){
           //如果没有训练过
           Toast.makeText(ApproveActivity.this, getString(R.string.error_gettrainedinfo), Toast.LENGTH_SHORT).show();
        }else{
        	enterTest();
        } 
    }
    
    // 进入认证 测试界面
    public void enterTest(){
        Intent intent = new Intent();
        intent.setClass(ApproveActivity.this, MultiTouchCollectorActivity.class);
        intent.putExtra("username", username);
        Log.i("test",username+"");
        intent.putExtra("type", "test");
        intent.putExtra("direction", handor);
        startActivity(intent);
        finish();//---------------------------------------------------------------------------------------------------><
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
                    Toast.makeText(ApproveActivity.this, "\n\n"+"姓名:   "+name+"\n\n"+"学号: "+id+"\n\n", Toast.LENGTH_SHORT).show();
//                  return name;
                    return id;
                }
            }
            if(id.equals("")){}
            else {
                Toast.makeText(ApproveActivity.this, "\n\n"+"姓名:  未知的"+"\n\n"+"学号："+id+"\n\n", Toast.LENGTH_SHORT).show();
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
        	Intent intent = new Intent();
            intent.setClass(ApproveActivity.this, MainActivity.class);
            startActivity(intent);
        return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
