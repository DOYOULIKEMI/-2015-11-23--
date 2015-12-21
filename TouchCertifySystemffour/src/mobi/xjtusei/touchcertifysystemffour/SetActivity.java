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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import mobi.xjtusei.touchcertifysystemffour.SeekArc;
import mobi.xjtusei.touchcertifysystemffour.SeekArc.OnSeekArcChangeListener;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;

public class SetActivity extends Activity {

	private SeekArc mSeekArc;
	private TextView mSeekArcProgress;
	private int currTrainNum;
	private ImageButton submitBtn;
	private ImageButton backbutton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置成全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_set);
		
		mSeekArc = (SeekArc) findViewById(R.id.seekArc);
		mSeekArcProgress = (TextView) findViewById(R.id.seekArcProgresses);
		
		
		mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekArc seekArc) {	
			}		
			@Override
			public void onStartTrackingTouch(SeekArc seekArc) {
			}
			
			@Override
			public void onProgressChanged(SeekArc seekArc, int progress,
					boolean fromUser) {
				mSeekArcProgress.setText(String.valueOf(progress));
			}
		});
		
		submitBtn = (ImageButton)findViewById(R.id.submitBtn);
		backbutton = (ImageButton)findViewById(R.id.back_set);
		submitBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				currTrainNum = Integer.parseInt(mSeekArcProgress.getText().toString());
				if(currTrainNum<=10){
					currTrainNum=10;
					UserInfo.setTotalTrainNum(currTrainNum);
				}
				else{
					UserInfo.setTotalTrainNum(currTrainNum);
				}
				Intent intent = new Intent();
				intent.setClass(SetActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		backbutton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent();
				intent.setClass(SetActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
	}
	 @Override 
	public boolean onTouchEvent(MotionEvent event){
		 if(event.getAction() == MotionEvent.ACTION_DOWN&&
 				event.getX()<150&&event.getX()>0&&
 				event.getY()<200&&event.getY()>0){
 			Log.i("fh","删除");
     		SetActivity.this.finish();
 			System.exit(0);
 		}
		  return true;  
	}
}
