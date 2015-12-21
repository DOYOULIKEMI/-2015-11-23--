package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.util.ArrayList;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;

public class TestFixedModeSystem extends Test{
	/*
	 * 构造函数
	 */
	public TestFixedModeSystem(){
		systemDirPath = UserInfo.systemDir;
		testDataDirPath = UserInfo.testFileDir;
		trainDataDirPath = UserInfo.trainFileDir;
		correctTrainedUsers = new String[]{"syp"};//正常的训练用户名
		correctTestUsers = new String[]{"syp"};//正常的测试用户名
		wrongTestUsers = new String[]{"wangjialin","wangm","liuxiaomei","tongli"};//异常的测试用户名
		trainFeas = new ArrayList<ArrayList<ArrayList<Double>>>();//存储正常用户的特征便于测试用户比较
		distCount = 23;//距离的维数
		trainedCount = 0;//正常的训练样本数
		refIndex = 0;//平均值距离的参考数据下标
		extFea = new ExtractFeatureFixedMode();
		offIndex = new int[]{4,5,22};
		isSystem = true;
	}
	
	//重写
	public void test() throws Exception{		
		//删除之前的测试文件
		String filePath = testDataDirPath+"/"+UserInfo.testFileName;
		File f = new File(filePath);
		if(f.exists()){
			f.delete();
		}
		/*
		//计算正常用户训练数据的特征
		getCorrectTrainedFea();
		
		//获取平均值距离的参考数据下标
		refIndex = getRefData();
		if(refIndex<0){
			return;
		}            */
		
		String filepath = testDataDirPath+"/"+UserInfo.testDataFileName;
		File file = new File(filepath);
		if(file.exists()){				
			ArrayList<ArrayList<Double>> fea = extFea.extractFea(filepath);
			calDist(fea);
		}	
	}
}
