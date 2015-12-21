package mobi.xjtusei.touchcertifysystemffour;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
import mobi.xjtusei.touchcertifysystemffour.handler.Certify;
import dataprocessing.checkData;
import dataprocessing.dataSave;
import dataprocessing.trainmodel;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * ���û���Ϣ��صĴ�����
 */
public class UserInfo {
	private static SharedPreferences sp;
	private static SharedPreferences sphand;
	public static SharedPreferences spuser;
	private static Context context;
	public static String serverIP = "202.117.61.41";//������IP
	public static int minTrainNum = 10;//��Ҫѵ������С����
	public static int maxTrainNum = 200;//��Ҫѵ����������
	public static int defaultTrainNum = 20;//��Ҫѵ����ȱʡ����
	//����֤ϵͳ�û���Ϣ�йصĴ�����
	public static String sdcard = android.os.Environment.getExternalStorageDirectory().toString(); 
	public static String systemDir = sdcard+"/CertSystemData4" ;
	public static String destDir = "/dev"+"/input/event2" ;
	public static String processDir  = sdcard+"/CertSystemData4/CDataPocessing/TrainTxT";
	public static String trainFileDir  = UserInfo.systemDir+"/trainData_fixed";
	public static String trainFileDirL  = UserInfo.systemDir+"/trainData_fixed_l";
	public static String testFileDir  = UserInfo.systemDir+"/testData_fixed";
	public static String testDataFileName = "testData.txt";
	public static String trainFileName = "train.txt";
	public static String testFileName = "test.txt";
	public static String modelFileName = "model.txt";
	public static String resultFileName = "out.txt";
	public static String testFileName1 = "test1.txt";
	public static String trainFileName1 = "train1.txt";
	public static String defaultrol =  "right";
	
	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		UserInfo.context = context;
	}
	
	//�ж�Android�豸�汾
	public static int getAndroidSDKVersion() { 
	   int version = 0; 
	   try { 
		   version = Integer.valueOf(android.os.Build.VERSION.SDK); 
	   } catch (NumberFormatException e) { 
		   System.out.println(e.toString());
	   } 
	   return version; 
	}
		
	//�ܹ���Ҫѵ���Ĵ���
	public static int getTotalTrainNum() {
		sp = context.getSharedPreferences("UserInfo", 0);
		return sp.getInt("totalTrainNum", UserInfo.defaultTrainNum);
	}

	public static void setTotalTrainNum(int totalTrainNum) {
		sp = context.getSharedPreferences("UserInfo", 0);
	    sp.edit().putInt("totalTrainNum", totalTrainNum).commit();
	}
	
	
	public static String getRightLeft(){
		sphand =	context.getSharedPreferences("UserInfohand", 0);
		return sphand.getString("hand", defaultrol);
	}
	public static void setRightLeft(String hand){
		sphand = context.getSharedPreferences("UserInfohand", 0);
		sphand.edit().putString("hand", hand).commit();
	}
	
	public static String getDefautUser(){
		spuser=	context.getSharedPreferences("dftuser", 0);
		return spuser.getString("user", "null");
	}
	
	public static void setDefaultuser(String user) {
		spuser = context.getSharedPreferences("dftuser", 0);
		spuser.edit().putString("user", user).commit();
	}	
	
	//�ж��Ƿ��Ѿ�ѵ���������ļ� ���ɹ�ģ���ļ�  
	
	/*
	 *  fixed with localizing
	 */
	public static boolean hasTrained(){
		// start localizing
		String modelFilePath;
		if (getRightLeft().equals("right")){
			modelFilePath = UserInfo.processDir+"/"+UserInfo.trainFileName;
		}else {
			modelFilePath = UserInfo.processDir+"/"+UserInfo.trainFileName;
		}
		File modelFile = new File(modelFilePath);
		if(modelFile.exists()){
			return true;
		}else{
			return false;
		}
	}
		
	//��֤�û����
	/*
	 *  fixed with localizing
	 */
	public static boolean certify() throws Exception{
		// start localizing
		trainmodel.TestSave(); 
		boolean res = Certify.certify("","");
		return res;

	}
	
	//ѵ���û������ϴ����ļ�
	public static String trainAllFiles(String username) throws Exception{
		String result;
		result = checkData.traindata();
		Log.i("trainh","here"+result+"");
		setDefaultuser(username);
		Log.i("trainh",result+"");
		return result;		
	}
	
	//��ȡ�û���ѵ���Ĵ���
	public static int getTrainedNum(String username){
		int number = 0;String userTrainFileDir;
		if(getRightLeft().equals("right")){
			userTrainFileDir = UserInfo.trainFileDir+"/"+username;
		}else {
			userTrainFileDir = UserInfo.trainFileDirL+"/"+username;
		}
		File file = new File(userTrainFileDir);
		if(file.exists()&&file.isDirectory()){
			number = file.list().length;
		}
		Log.i("name",userTrainFileDir);
		Log.i("number:"," "+number );
		return number;
	}
	
	//ɾ���û���ѵ���������ļ�
	public static boolean delTrainFiles(String username){
		String filePath;
		String filePath2;
		String filePath3;
		String filePath4;
		String filePath5;
		boolean res;
		
		if(getRightLeft().equals("right")){
			filePath = UserInfo.trainFileDir+"/"+username;
		}else {
			filePath = UserInfo.trainFileDirL+"/"+username;
		}
		filePath2 = dataSave.featureDataDir+"/"+username;
		filePath3 = dataSave.fingerDataDir+"/"+username;
		filePath4 = dataSave.tempDataDir+"/"+username;
		filePath5 =dataSave.feature4checkDataDir+"/"+username;

		res = delDirectory(filePath2);
		res = delDirectory(filePath3);
		res = delDirectory(filePath4);
		res = delDirectory(filePath5);
		return delDirectory(filePath);
	}
	
	/*�Զ����ַ����滻����*/
	public static String eregi_replace(String strFrom, String strTo,String strTarget)
	{
		String strPattern = "(?i)"+strFrom;
		Pattern p = Pattern.compile(strPattern);
		Matcher m=p.matcher(strTarget);
		if(m.find())
		{
		  return strTarget.replaceAll(strFrom,strTo);
		}
		else
		{
		  return strTarget;
		}
	}

	public static boolean delDirectory(String filepath){
		try{
			File f = new File(filepath);//�����ļ�·��
			//org.apache.commons.io.FileUtils.deleteDirectory(f);//ɾ��Ŀ¼ 	
			if(f.exists() && f.isDirectory()){//�ж����ļ�����Ŀ¼  
			   if(f.listFiles().length==0){//��Ŀ¼��û���ļ���ֱ��ɾ��  
			      f.delete();  
			   }else{//��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼  
			       File delFile[]=f.listFiles();  
			       int i =f.listFiles().length;  
			       for(int j=0;j<i;j++){  
			           if(delFile[j].isDirectory()){  
			        	   delTrainFiles(delFile[j].getAbsolutePath());//�ݹ����del������ȡ����Ŀ¼·��  
			            }  
			           delFile[j].delete();//ɾ���ļ�  
			        }  
			    }  
			}    
		}catch(Exception e){
			return false;
		}
		return true;
	}
	

}
