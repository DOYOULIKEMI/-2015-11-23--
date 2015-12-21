package mobi.xjtusei.touchcertifysystemffour;

import java.text.AttributedCharacterIterator.Attribute;

import mobi.xjtusei.touchcertifysystemffour.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RegisterActivity extends Activity{
	
	private ImageButton submitBtn;
	private EditText ipEditText;
	private SeekBar trainNumBar;
	private TextView currTrainNumTextView;
	private int currTrainNum;
	private int minTrainNum = UserInfo.minTrainNum;
	private int maxTrainNum = UserInfo.maxTrainNum;
	private String hand = "right";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置成全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        WindowManager.LayoutParams.FLAG_FULLSCREEN);  
       	setContentView(R.layout.activity_register);
		ImageButton btn = (ImageButton)findViewById(R.id.submitBtn_register);
		ImageButton btn_back = (ImageButton)findViewById(R.id.back_register);
		btn.setOnClickListener(new OnClickListener() {	
			@Override
             public void onClick(View v) {
				Intent intent = new Intent();
           	 	intent.setClass(RegisterActivity.this, MainActivity.class);
           	 	startActivity(intent);}
                   });
		btn_back.setOnClickListener(new OnClickListener() {	
			@Override
             public void onClick(View v) {
				Intent intent = new Intent();
           	 	intent.setClass(RegisterActivity.this, MainActivity.class);
           	 	startActivity(intent);}
                   });
		}
		

}
