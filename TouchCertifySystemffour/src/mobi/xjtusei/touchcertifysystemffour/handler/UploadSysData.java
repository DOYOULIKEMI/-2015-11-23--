package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.io.IOException;

import android.util.Log;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;


/*
 * �洢ѵ�����ݵ������ļ�
 */
public class UploadSysData extends UploadData{
	//�����ļ�Ŀ¼
	public static boolean upload(String data,String username,String mode,String type) throws IOException{
		systemDirPath = UserInfo.systemDir;
		
		//����Ŀ¼
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
		//����Ŀ¼
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
