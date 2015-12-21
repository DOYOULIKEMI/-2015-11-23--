package mobi.xjtusei.touchcertifysystemffour.handler;

import java.util.ArrayList;

import android.util.Log;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.handler.ExtractFeatureFixedMode;
import mobi.xjtusei.touchcertifysystemffour.handler.Train;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;

public class TrainFixedModeSystem extends Train{
	/*
	 * ���캯��
	 */
	public TrainFixedModeSystem(){
		//correctUsers = new String[]{"wangm","wjl","yangzhihai","liuxiaomei","zhuchangxu"};
		correctUsers = new String[]{"zuoxingcun"};//ͨ������ķ����ı�Ϸ��û�
		wrongUsers = new String[]{"zhangyong","liangchao","songyp"};
		systemDirPath = UserInfo.systemDir;
		trainDataDirPath = UserInfo.trainFileDir;
		feaDirPath = UserInfo.systemDir+"/trainFea_fixed";
		trainFeas = new ArrayList<ArrayList<ArrayList<Double>>>();//�洢�����û������������쳣�û��Ƚ�
		distCount = 23;//�����ά�� old
				       //1-4:���㴥�����ٶȾ��� 5-8�����㴥���ļ��ٶȾ��� 9-12�����㴥����ƫ��������
					   //13-16:���㴥����ѹ������
					   //17-24:��㴥�������������� 25-32����㴥���������ǶȾ���
					   //33-48����㴥��������ѹ����
		count = 0;//�����û���������
		dists = null;//��������û���������
		refFea = null;//ƽ��ֵ����Ĳο�����ֵ
		extFea = new ExtractFeatureFixedMode();
		feaCount = 23; //������ά��
					   //1-4:���㴥�����ٶ�  5�����㴥����ƫ����
					   //6:���㴥����ѹ����׼��
					   //7-14:��㴥����������� 15-22����㴥���������Ƕ�
					   //23����㴥����ѹ����׼��
		offIndex = new int[]{4,5,22};
		isSystem = true;
	}
	public void getWrongUsers(){		
//		Log.i("user",":"+correctUsers[0]);
		for (int i = 0;i<wrongUsers.length;i++)
		{
			if (correctUsers[0].equals(wrongUsers[i]))
				wrongUsers[i] = "zuoxingcun";
//				Log.i("user",":"+wrongUsers[i]);
		}
	}
	public void setCorrectUsers(String uname){
		correctUsers[0] = uname;
		Log.i("user",":"+correctUsers[0]);
	}
	public  int getIllegalNum(){
		return wrongUsers.length;
	}
}
