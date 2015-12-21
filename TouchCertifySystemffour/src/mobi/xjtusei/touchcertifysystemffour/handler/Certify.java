package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import dataprocessing.dataSave;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;

public class Certify {
	static String trainFilePath = dataSave.trainTxTDataDir+"/"+UserInfo.trainFileName;
//	static String trainFilePath1 = UserInfo.trainFileDir+"/"+UserInfo.trainFileName1;
	static String testFilePath = dataSave.trainTxTDataDir+"/"+UserInfo.testFileName;
//	static String testFilePath1 = UserInfo.testFileDir+"/"+UserInfo.testFileName1;
	static String modelFilePath = dataSave.trainTxTDataDir+"/"+UserInfo.modelFileName;
	static String resultFilePath = dataSave.trainTxTDataDir+"/"+UserInfo.resultFileName;
	
	public static boolean certify(String trainFilePath2, String certFilePath2) throws Exception
	{		
		File trainFile = new File(trainFilePath);
//		File trainFile1 = new File(trainFilePath1);
//		UploadData.delFile(trainFilePath1);
		File testFile = new File(testFilePath);
//		UploadData.delFile(testFilePath1);
		
		if(!trainFile.exists()||!testFile.exists()){
			return false;
		}
		svm_train svmt = new svm_train();
		svm_predict svmp = new svm_predict();
//		fileopera.fileop();
//		fileopera.filetest();
		
		String[] argvTrain = {
//				"-t","2",
				trainFilePath, // 训练文件
				modelFilePath  // 模型文件           
		};
		String[] argvPredict = {
				testFilePath,  // 认证文件
				modelFilePath, // 模型文件
				resultFilePath // 结果文件
		};
		
	    try {
		   svmt.main(argvTrain);
		   svmp.main(argvPredict);
		} catch (IOException e) {
		   e.printStackTrace();
		}
//		double[] record = { -1, 12, 12, 78 };
//		svm_model model = svm.svm_load_model("D:\\CertSystemData\\trainData_fixed\\model.txt");
//		System.out.println(svmp.predictPerRecord(record, model));

		//读取结果
		float res;
		File file = new File(resultFilePath);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String str = reader.readLine().trim();
            res = Float.parseFloat(str);
		}catch(Exception e){
			return false;
		}
		if(res==1.0){
			return true;
		}else if(res==-1.0){
			return false;
		}
		return true;
	 }
}
