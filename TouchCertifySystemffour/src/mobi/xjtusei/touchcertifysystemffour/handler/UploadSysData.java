package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.io.IOException;

import android.util.Log;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;


/*
 * 存储训练数据到本地文件
 */
public class UploadSysData extends UploadData{
	//创建文件目录
	public static boolean upload(String data,String username,String mode,String type) throws IOException{
		systemDirPath = UserInfo.systemDir;
		
		//创建目录
		File rootFile = new File(systemDirPath);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }

        if(type.equals("train")){
        	if(MultiTouchView.handlor.equals("right")){
        		dataDirPath = systemDirPath+"/trainData_fixed";
        	}else {
        		dataDirPath = systemDirPath+"/trainData_fixed_l";
			}
        }else if(type.equals("test")){
        	dataDirPath = systemDirPath+"/CDataPocessing/TrainTxT";
        }
		
//		extFea = new ExtractFeatureFixedMode();
//		feaCount = 23;
		//创建目录
		File dirFile = new File(dataDirPath);
        if (!dirFile.isDirectory()) {
        	if (!dirFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }	
        if(type.equals("train")){
        	return saveTrain(data,username,dataDirPath);
        }else if(type.equals("test")){
        	Log.i("test","test2");
        	return saveTest(data,dataDirPath);
        }
        return true;
	}
}
