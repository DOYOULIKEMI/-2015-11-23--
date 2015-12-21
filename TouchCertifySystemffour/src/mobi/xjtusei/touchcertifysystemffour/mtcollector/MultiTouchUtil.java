package mobi.xjtusei.touchcertifysystemffour.mtcollector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.handler.UploadSysData;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class MultiTouchUtil {	
	
	public static String wrapData(ArrayList<DataType> dataList){
		StringBuffer data = new StringBuffer();
		for(int i = 0;i<dataList.size();i++){
			String temp = dataList.get(i).getPoint().x+" "+dataList.get(i).getPoint().y+" "+dataList.get(i).getTime()+"\n";
			data.append(temp); 
		}
		return data.toString();
	}
	
	// fixed with localizing
	public static boolean uploadData(String data,String username,String type) throws IOException{
		if(data.equals(null)||data.equals("")||(type.equals("train")&&username==null)) 
			return false;
		// start localizing
		if(type.equals("test")){
			Log.i("test","test");
			Boolean result = UploadSysData.upload(data,username,"fixed",type);
			return result;
		}
		// end localizing
		if(type.equals("train")){
			Boolean result = UploadSysData.upload(data,username,"fixed",type);
			return result;
		}
	
		return true;
	}
}
