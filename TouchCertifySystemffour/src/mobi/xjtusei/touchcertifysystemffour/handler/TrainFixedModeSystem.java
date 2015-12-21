package mobi.xjtusei.touchcertifysystemffour.handler;

import java.util.ArrayList;

import android.util.Log;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.handler.ExtractFeatureFixedMode;
import mobi.xjtusei.touchcertifysystemffour.handler.Train;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;

public class TrainFixedModeSystem extends Train{
	/*
	 * 构造函数
	 */
	public TrainFixedModeSystem(){
		//correctUsers = new String[]{"wangm","wjl","yangzhihai","liuxiaomei","zhuchangxu"};
		correctUsers = new String[]{"zuoxingcun"};//通过后面的方法改变合法用户
		wrongUsers = new String[]{"zhangyong","liangchao","songyp"};
		systemDirPath = UserInfo.systemDir;
		trainDataDirPath = UserInfo.trainFileDir;
		feaDirPath = UserInfo.systemDir+"/trainFea_fixed";
		trainFeas = new ArrayList<ArrayList<ArrayList<Double>>>();//存储正常用户的特征便于异常用户比较
		distCount = 23;//距离的维数 old
				       //1-4:单点触摸的速度距离 5-8：单点触摸的加速度距离 9-12：单点触摸的偏移量距离
					   //13-16:单点触摸的压力距离
					   //17-24:多点触摸的两点距离距离 25-32：多点触摸的两点间角度距离
					   //33-48：多点触摸的两点压力差
		count = 0;//正常用户的样本数
		dists = null;//存放正常用户样本距离
		refFea = null;//平均值距离的参考特征值
		extFea = new ExtractFeatureFixedMode();
		feaCount = 23; //特征的维数
					   //1-4:单点触摸的速度  5：单点触摸的偏移量
					   //6:单点触摸的压力标准差
					   //7-14:多点触摸的两点距离 15-22：多点触摸的两点间角度
					   //23：多点触摸的压力标准差
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
