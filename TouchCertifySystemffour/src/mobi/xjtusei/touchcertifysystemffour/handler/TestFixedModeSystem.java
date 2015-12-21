package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.util.ArrayList;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;

public class TestFixedModeSystem extends Test{
	/*
	 * ���캯��
	 */
	public TestFixedModeSystem(){
		systemDirPath = UserInfo.systemDir;
		testDataDirPath = UserInfo.testFileDir;
		trainDataDirPath = UserInfo.trainFileDir;
		correctTrainedUsers = new String[]{"syp"};//������ѵ���û���
		correctTestUsers = new String[]{"syp"};//�����Ĳ����û���
		wrongTestUsers = new String[]{"wangjialin","wangm","liuxiaomei","tongli"};//�쳣�Ĳ����û���
		trainFeas = new ArrayList<ArrayList<ArrayList<Double>>>();//�洢�����û����������ڲ����û��Ƚ�
		distCount = 23;//�����ά��
		trainedCount = 0;//������ѵ��������
		refIndex = 0;//ƽ��ֵ����Ĳο������±�
		extFea = new ExtractFeatureFixedMode();
		offIndex = new int[]{4,5,22};
		isSystem = true;
	}
	
	//��д
	public void test() throws Exception{		
		//ɾ��֮ǰ�Ĳ����ļ�
		String filePath = testDataDirPath+"/"+UserInfo.testFileName;
		File f = new File(filePath);
		if(f.exists()){
			f.delete();
		}
		/*
		//���������û�ѵ�����ݵ�����
		getCorrectTrainedFea();
		
		//��ȡƽ��ֵ����Ĳο������±�
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
