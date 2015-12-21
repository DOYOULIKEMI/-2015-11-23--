package mobi.xjtusei.touchcertifysystemffour.handler;

import java.util.ArrayList;

public class ExtractFeatureFixedMode extends ExtractFeature{
	/*
	 * ���캯��
	 */
	public ExtractFeatureFixedMode(){
		fileRootPath = "D://featureData_fixed";//���������ļ���Ŀ¼
		count = 12;//�����ܶ���
		count4singTouch = new int[]{0,1,2,3};//���㴥���Ķ���
		count4multiTouch = new int[]{4,5,6,7,8,9,10,11};//��㴥���Ķ���
		axisrArray = new int[]{2,3};//��Ҫ����任�Ķ�
	}
	
	/*
	 *  ��ȡ����
	 */
	public ArrayList<ArrayList<Double>> extractFea(String fileName){
		ArrayList<ArrayList<Double>> feature = new ArrayList<ArrayList<Double>>();//��������
		ArrayList<ArrayList<DataType>> data = init(fileName);
		if(data.size()!=count){
			return null;
		}
		axisrot(data);
		// ��ȡ���е��㴥��������
		ArrayList<ArrayList<DataType>> singTouchData = new ArrayList<ArrayList<DataType>>();
		for(int i = 0;i<count4singTouch.length;i++){
			singTouchData.add(data.get(count4singTouch[i]));
		}	
		// ��ȡ���ж�㴥��������
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
