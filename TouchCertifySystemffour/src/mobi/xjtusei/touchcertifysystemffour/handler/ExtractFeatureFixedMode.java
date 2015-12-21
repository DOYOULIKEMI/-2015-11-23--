package mobi.xjtusei.touchcertifysystemffour.handler;

import java.util.ArrayList;

public class ExtractFeatureFixedMode extends ExtractFeature{
	/*
	 * 构造函数
	 */
	public ExtractFeatureFixedMode(){
		fileRootPath = "D://featureData_fixed";//保存特征文件的目录
		count = 12;//数据总段数
		count4singTouch = new int[]{0,1,2,3};//单点触摸的段数
		count4multiTouch = new int[]{4,5,6,7,8,9,10,11};//多点触摸的段数
		axisrArray = new int[]{2,3};//需要坐标变换的段
	}
	
	/*
	 *  提取特征
	 */
	public ArrayList<ArrayList<Double>> extractFea(String fileName){
		ArrayList<ArrayList<Double>> feature = new ArrayList<ArrayList<Double>>();//保存特征
		ArrayList<ArrayList<DataType>> data = init(fileName);
		if(data.size()!=count){
			return null;
		}
		axisrot(data);
		// 获取所有单点触摸的数据
		ArrayList<ArrayList<DataType>> singTouchData = new ArrayList<ArrayList<DataType>>();
		for(int i = 0;i<count4singTouch.length;i++){
			singTouchData.add(data.get(count4singTouch[i]));
		}	
		// 获取所有多点触摸的数据
		ArrayList<ArrayList<DataType>> multiTouchData = new ArrayList<ArrayList<DataType>>();
		for(int i = 0;i<count4multiTouch.length;i++){
			multiTouchData.add(data.get(count4multiTouch[i]));
		}
		if(!feature4singTouch(singTouchData,feature))
			return null;	
		if(!feature4multiTouch_interpolation(multiTouchData,feature))
			return null;
		System.out.println(fileName);
		return feature;
	}
}
