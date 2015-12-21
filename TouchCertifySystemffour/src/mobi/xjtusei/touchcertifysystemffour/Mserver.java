package mobi.xjtusei.touchcertifysystemffour;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class Mserver extends Service {


	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		//ע���ͦϵͳ������Ϣ
		registerReceiver(br, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	private BroadcastReceiver br = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
			{ //�յ��㲥����
				Log.i("bc","screen off");
				KeyguardManager.KeyguardLock kk;
				KeyguardManager km = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
			    kk = km.newKeyguardLock("Tag For Debug");
			    kk.disableKeyguard();
			    showLockView(context);
			}
			
		}
	};
	
	public void showLockView(Context context)
	{//��������activity
		Intent intent = new Intent(context,LockView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	

}
