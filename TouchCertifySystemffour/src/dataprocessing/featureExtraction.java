package dataprocessing;


import java.util.ArrayList;

import android.util.Log;


public class featureExtraction {
	public static ArrayList<ArrayList<Double>> fea4check = new ArrayList<ArrayList<Double>>();
	public static ArrayList<ArrayList<Double>> feature = new ArrayList<ArrayList<Double>>();
	public static ArrayList<ArrayList<DataType>> rotatedData = new ArrayList<ArrayList<DataType>>();
	public static ArrayList<ArrayList<Double>> tempclone = new ArrayList<ArrayList<Double>>();
//	计算用于一些辅助特征
	public static ArrayList<ArrayList<Double>> extractcheckedFea(ArrayList<ArrayList<DataType>> rawfourdata){		
		ArrayList<Double> segpoint = new ArrayList<Double>();
		  
		int idS = 0; double idx = 2000;
		fea4check.clear();
		for(int i = 0;i<rawfourdata.size();i++){
			segpoint = section(rawfourdata.get(i));
			if(segpoint.get(1)<idx){
				idS = segpoint.get(3).intValue();
				idx = segpoint.get(1);
			}
			fea4check.add(segpoint);
		}
		rotatedData = dataRotating(rawfourdata, idS);
//		rotatedData = rawfourdata;
		return fea4check;
	}
//	计算特征
	public static ArrayList<ArrayList<Double>> extractFea(ArrayList<ArrayList<DataType>> modifieddata){		
		ArrayList<DataType> modsegpoint = new ArrayList<DataType>();	
		ArrayList<ArrayList<Double>> midfea = new ArrayList<ArrayList<Double>>();
		feature.clear();
//		（一定范围内点的密度？？）
//		特征：指长差（没用绝对值），指间距,
		feature = basicfea(fea4check);
		midfea = ratioofld(fea4check, rotatedData);
		for (int j = 0; j < midfea.size(); j++) {
			feature.add(midfea.get(j));
		}
		for (int i = 0; i < modifieddata.size(); i++) {
			modsegpoint = modifieddata.get(i);			
		}
/*		for (int i = 0; i < feature.size(); i++) {
			Log.i("featureExtraction",feature.get(i)+"");
		}*/
		return feature;
	}
//	返回分段点信息和经过处理的原始数据；
	public static ArrayList<Double> section(ArrayList<DataType> singleFingerData){
		ArrayList<Double> keypt = new ArrayList<Double>();
		int windowlength = 7;
		int datalength = singleFingerData.size();
		double [][] angle = new double[datalength] [3];
		double [][] sfdata = new double [datalength][4];
		int i;
		for(i = 0;i<datalength;i++){
			sfdata[i][0] = singleFingerData.get(i).x;
			sfdata[i][1] = singleFingerData.get(i).y;
			sfdata[i][2] = singleFingerData.get(i).time;
			sfdata[i][3] = singleFingerData.get(i).id;
		}
		angle[0][0] = -90;
		angle[0][1] = 1;
		angle[0][2] = 1;
		for(i = 1;i<datalength;i++){
			double dx = sfdata[i][0] - sfdata[i-1][0];
			double dy = sfdata[i][1] - sfdata[i-1][1];
			if (dx == 0){
				angle[i][0] = -90;
			}else {
				if (dx<0 && dy<0) {
					angle[i][0] = Math.toDegrees(Math.atan(dy/dx));;
				}else {
					angle[i][0] = Math.toDegrees(Math.atan(dy/dx));
				}
			}
			if(angle[i][0]<44 && angle[i][0]>-44){
				angle[i][1] = 2;
			}else {
				angle[i][1] = 1;
			}
		}
		for(i = 0;i<datalength;i++){
			//最初几个赋值为1
			if(i-(windowlength/2)<0){
				angle[i][2] = 1;
			}
			//最后几个赋值为2
			else if(i+(windowlength/2)>datalength-1){
				angle[i][2] = 2;
			}
			//中间部分用窗口平滑
			//考虑是否使用30%的界限！
			else {
				if (i+10<datalength) {
					if (windowsum(angle,i-(windowlength/2),i+(windowlength/2),1)>=11 && 
							windowsum(angle,i+4,i+10,1)>=10 ){
						angle[i][2] = 2;
					}else{
						angle[i][2] = 1;
					}					
				}else {
					if (windowsum(angle,i-(windowlength/2),i+(windowlength/2),1)>=11){
						angle[i][2] = 2;
					}else{
						angle[i][2] = 1;
					}
				}
			}

		}
		for(i = 0;i<datalength;i++){
			if(angle[i][2] == 2){
				break;
			}
		}
		if(i<=5 || i>=datalength-5){
		}
		keypt.add((double)i-2); keypt.add( sfdata[i-2][0]); keypt.add(sfdata[i-2][1]);	 keypt.add(sfdata[i-2][3]);	
		return keypt;
	}
	
	public static double windowsum(double a[][],int startp,int endp,int col){
		double sumwin = 0;
		for(int i = startp;i<=endp;i++){
			sumwin += a[i][col];  
		}
		return sumwin;
	}
//	找到左起第一条轨迹并以其为标准旋转(无平移)
	public static ArrayList<ArrayList<DataType>> dataRotating (ArrayList<ArrayList<DataType>> data4rotate,int idleft){
		ArrayList<ArrayList<DataType>>  DataRotated = new ArrayList<ArrayList<DataType>>();
		ArrayList<DataType> leftFinger = data4rotate.get(idleft);
		double []sumxy = {0,0};
		double []avgxy = {0,0};
		double angle4rotate = 0;
		int i;
		if(fea4check.get(idleft).get(0)<10){		
			for(i = 0;i<fea4check.get(idleft).get(0);i++){
				sumxy[0] += leftFinger.get(i).x;
				sumxy[1] += leftFinger.get(i).y;
			}	
			avgxy[0] = sumxy[0]/i;
			avgxy[1] = sumxy[1]/i;
		} else{
			for(i = (int)(0.2*fea4check.get(idleft).get(0));i<(int)(0.8*(fea4check.get(idleft).get(0)));i++){
				sumxy[0] += leftFinger.get(i).x;
				sumxy[1] += leftFinger.get(i).y;
			}
			avgxy[0] = sumxy[0]/(i-(int)(0.2*fea4check.get(idleft).get(0)));
			avgxy[1] = sumxy[1]/(i-(int)(0.2*fea4check.get(idleft).get(0)));
		}
		angle4rotate = Math.atan((avgxy[0]-fea4check.get(idleft).get(1))/(avgxy[1]-fea4check.get(idleft).get(2)));
		for (int j = 0; j < data4rotate.size(); j++) {
			ArrayList<DataType> tempFinger = rotate4theta(data4rotate.get(j),angle4rotate,fea4check.get(idleft).get(1),fea4check.get(idleft).get(2));
//			ArrayList<DataType> tempFinger = rotate4theta(data4rotate.get(j),0.7);
			DataRotated.add(tempFinger);
		}
		return DataRotated;
	}
//	旋转——>选取一个基准dx，dy，按照角度theta进行
	public static ArrayList<DataType> rotate4theta(ArrayList<DataType> oldxy,double theta,double dx,double dy){
		ArrayList<DataType> newxy = new ArrayList<DataType>();		
		for(int k = 0;k<oldxy.size();k++){
			DataType tempd = new DataType(oldxy.get(k).x,oldxy.get(k).y,oldxy.get(k).time,
					oldxy.get(k).pressure,oldxy.get(k).id,oldxy.get(k).tool,oldxy.get(k).touch);
			tempd.setX((int)((oldxy.get(k).x-dx)*Math.cos(theta) - (oldxy.get(k).y-dy)*Math.sin(theta))+dx);
			tempd.setY((int)((oldxy.get(k).x-dx)*Math.sin(theta) + (oldxy.get(k).y-dy)*Math.cos(theta))+dy) ;
			newxy.add(tempd);
		}		
		return newxy;
	}
//	计算指长差(没用绝对值)，指间距
	public static ArrayList<ArrayList<Double> >basicfea(ArrayList<ArrayList<Double>> tempfea){
		ArrayList<ArrayList<Double>> bf = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> bfte0 = new ArrayList<Double>();
		ArrayList<Double> bfte1 = new ArrayList<Double>();
		ArrayList<Double> bfte2 = new ArrayList<Double>();
		ArrayList<Double> bfte3 = new ArrayList<Double>();
		ArrayList<Double> bfte4 = new ArrayList<Double>();
		ArrayList<Double> bfte5 = new ArrayList<Double>();
		ArrayList<Double> bfte6 = new ArrayList<Double>();
		ArrayList<Double> bfte7 = new ArrayList<Double>();
		ArrayList<Double> bfte8 = new ArrayList<Double>();
		ArrayList<Double> bfte9 = new ArrayList<Double>();
		
		ArrayList<Double> bfte10up = new ArrayList<Double>();ArrayList<Double> bfte10rt = new ArrayList<Double>();
		ArrayList<Double> bfte11up = new ArrayList<Double>();ArrayList<Double> bfte11rt = new ArrayList<Double>();
		ArrayList<Double> bfte12up = new ArrayList<Double>();ArrayList<Double> bfte12rt = new ArrayList<Double>();
		ArrayList<Double> bfte13up = new ArrayList<Double>();ArrayList<Double> bfte13rt = new ArrayList<Double>();
		ArrayList<Double> bfte14up = new ArrayList<Double>();ArrayList<Double> bfte14rt = new ArrayList<Double>();
		ArrayList<Double> bfte15up = new ArrayList<Double>();ArrayList<Double> bfte15rt = new ArrayList<Double>();		
		
		ArrayList<Double> bfte10tup = new ArrayList<Double>();ArrayList<Double> bfte10trt = new ArrayList<Double>();
		ArrayList<Double> bfte11tup = new ArrayList<Double>();ArrayList<Double> bfte11trt = new ArrayList<Double>();
		ArrayList<Double> bfte12tup = new ArrayList<Double>();ArrayList<Double> bfte12trt = new ArrayList<Double>();
		ArrayList<Double> bfte13tup = new ArrayList<Double>();ArrayList<Double> bfte13trt = new ArrayList<Double>();
		ArrayList<Double> bfte14tup = new ArrayList<Double>();ArrayList<Double> bfte14trt = new ArrayList<Double>();
		ArrayList<Double> bfte15tup = new ArrayList<Double>();ArrayList<Double> bfte15trt = new ArrayList<Double>();
		
		ArrayList<Double> bfte16up = new ArrayList<Double>();ArrayList<Double> bfte16rt = new ArrayList<Double>();
		ArrayList<Double> bfte17up = new ArrayList<Double>();ArrayList<Double> bfte17rt = new ArrayList<Double>();
		ArrayList<Double> bfte18up = new ArrayList<Double>();ArrayList<Double> bfte18rt = new ArrayList<Double>();
		ArrayList<Double> bfte19up = new ArrayList<Double>();ArrayList<Double> bfte19rt = new ArrayList<Double>();
		
		ArrayList<Double> bfte16tup = new ArrayList<Double>();ArrayList<Double> bfte16trt = new ArrayList<Double>();
		ArrayList<Double> bfte17tup = new ArrayList<Double>();ArrayList<Double> bfte17trt = new ArrayList<Double>();
		ArrayList<Double> bfte18tup = new ArrayList<Double>();ArrayList<Double> bfte18trt = new ArrayList<Double>();
		ArrayList<Double> bfte19tup = new ArrayList<Double>();ArrayList<Double> bfte19trt = new ArrayList<Double>();
		
		tempclone.clear();
		for(int i = 0;i<tempfea.size();i++){
			tempclone.add(tempfea.get(i));
		}
		for(int i = 0;i<tempclone.size();i++){
			for(int j = i+1;j<tempclone.size();j++){
				if(tempclone.get(i).get(1)>tempclone.get(j).get(1)){
					swap(tempclone,i,j);
				}
			}
		}
		
		double []x = new double[4];
		double []y = new double[4];
		for (int t = 0; t < x.length; t++) {
			x[t] = tempclone.get(t).get(1);
			y[t] = tempclone.get(t).get(2);
		}

/*		bfte4.add();
		bf.add(bfte4);
		bfte5.add();
		bf.add(bfte5);*/
		bfte4.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(tempclone.get(0).get(0).intValue()).y
				-rotatedData.get(tempclone.get(1).get(3).intValue()).get(tempclone.get(1).get(0).intValue()).y);
		bf.add(bfte4);
		bfte5.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(tempclone.get(0).get(0).intValue()).y
				-rotatedData.get(tempclone.get(2).get(3).intValue()).get(tempclone.get(2).get(0).intValue()).y);
		bf.add(bfte5);
		bfte6.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(tempclone.get(0).get(0).intValue()).y
				-rotatedData.get(tempclone.get(3).get(3).intValue()).get(tempclone.get(3).get(0).intValue()).y);
		bf.add(bfte6);
		bfte7.add(rotatedData.get(tempclone.get(1).get(3).intValue()).get(tempclone.get(1).get(0).intValue()).x
				-rotatedData.get(tempclone.get(0).get(3).intValue()).get(tempclone.get(0).get(0).intValue()).x);
		bf.add(bfte7);
		bfte8.add(rotatedData.get(tempclone.get(2).get(3).intValue()).get(tempclone.get(2).get(0).intValue()).x
				-rotatedData.get(tempclone.get(1).get(3).intValue()).get(tempclone.get(1).get(0).intValue()).x);
		bf.add(bfte8);
		bfte9.add(rotatedData.get(tempclone.get(3).get(3).intValue()).get(tempclone.get(3).get(0).intValue()).x
				-rotatedData.get(tempclone.get(2).get(3).intValue()).get(tempclone.get(2).get(0).intValue()).x);
		bf.add(bfte9);		
		
		bfte0.add(Math.sqrt((x[0]-x[1])*(x[0]-x[1])+(y[0]-y[1])*(y[0]-y[1])));
		bf.add(bfte0);
		bfte1.add(Math.sqrt((x[1]-x[2])*(x[1]-x[2])+(y[1]-y[2])*(y[1]-y[2])));
		bf.add(bfte1);
		bfte2.add(Math.sqrt((x[2]-x[3])*(x[2]-x[3])+(y[2]-y[3])*(y[2]-y[3])));
		bf.add(bfte2);
		bfte3.add(Math.sqrt((x[3]-x[0])*(x[3]-x[0])+(y[3]-y[0])*(y[3]-y[0])));
		bf.add(bfte3);		
		
		int segid = tempclone.get(0).get(0).intValue();
//		第一段均值、第二段均值(指长差*3、指间距*3)
		for (int j = 0; j < segid+1; j++) {
			bfte10tup.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y
					-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).y);
			bfte11tup.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y
					-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).y);
			bfte12tup.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y
					-rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).y);
			bfte13tup.add(rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x
					-rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).x);
			bfte14tup.add(rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x
					-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x);
			bfte15tup.add(rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).x
					-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x);
		}	
		
		for (int j = segid+1; j < rotatedData.get(0).size(); j++) {
			bfte10trt.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y
					-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).y);
			bfte11trt.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y
					-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).y);
			bfte12trt.add(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y
					-rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).y);
			bfte13trt.add(rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x
					-rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).x);
			bfte14trt.add(rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x
					-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x);
			bfte15trt.add(rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).x
					-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x);
		}

		double sum0up = 0;double sum1up = 0;double sum2up = 0;double sum3up = 0;double sum4up = 0;double sum5up = 0;
		double sum0rt = 0;double sum1rt = 0;double sum2rt = 0;double sum3rt = 0;double sum4rt = 0;double sum5rt = 0;
		
		for(int i = 0; i < bfte10tup.size(); i++){
			sum0up += bfte10tup.get(i);
			sum1up += bfte11tup.get(i);	
			sum2up += bfte12tup.get(i);	
			sum3up += bfte13tup.get(i);	
			sum4up += bfte14tup.get(i);	
			sum5up += bfte15tup.get(i);
		}
		
		for (int i = 0; i < bfte10trt.size(); i++) {
			sum0rt += bfte10trt.get(i);
			sum1rt += bfte11trt.get(i);	
			sum2rt += bfte12trt.get(i);	
			sum3rt += bfte13trt.get(i);	
			sum4rt += bfte14trt.get(i);	
			sum5rt += bfte15trt.get(i);	
		}
		
		bfte10up.add(sum0up/bfte10tup.size());
		bfte11up.add(sum1up/bfte10tup.size());
		bfte12up.add(sum2up/bfte10tup.size());
		bfte13up.add(sum3up/bfte10tup.size());
		bfte14up.add(sum4up/bfte10tup.size());
		bfte15up.add(sum5up/bfte10tup.size());
		
		bfte10rt.add(sum0rt/bfte10trt.size());
		bfte11rt.add(sum1rt/bfte10trt.size());
		bfte12rt.add(sum2rt/bfte10trt.size());
		bfte13rt.add(sum3rt/bfte10trt.size());
		bfte14rt.add(sum4rt/bfte10trt.size());
		bfte15rt.add(sum5rt/bfte10trt.size());
		
//		第一段均值、第二段均值(距离*4)		
		for (int j = 0; j < segid+1; j++) {
			bfte16tup.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x,2)));
			bfte17tup.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x,2)));
			bfte18tup.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).x,2)));
			bfte19tup.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).x,2)));
		}
		
		for (int j = segid+1; j < rotatedData.get(0).size(); j++) {
			bfte16trt.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x,2)));
			bfte17trt.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(1).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x,2)));
			bfte18trt.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(2).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).x,2)));
			bfte19trt.add(Math.sqrt(Math.pow(rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).y-rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).y,2)+
					Math.pow(rotatedData.get(tempclone.get(3).get(3).intValue()).get(j).x-rotatedData.get(tempclone.get(0).get(3).intValue()).get(j).x,2)));
		}
		
		double sum6up = 0;double sum7up = 0;double sum8up = 0;double sum9up = 0;
		double sum6rt = 0;double sum7rt = 0;double sum8rt = 0;double sum9rt = 0;
		
		for(int i = 0; i < bfte16tup.size(); i++){
			sum6up += bfte16tup.get(i);
			sum7up += bfte17tup.get(i);	
			sum8up += bfte18tup.get(i);	
			sum9up += bfte19tup.get(i);	
		}
		
		for (int i = 0; i < bfte16trt.size(); i++) {
			sum6rt += bfte16trt.get(i);
			sum7rt += bfte17trt.get(i);	
			sum8rt += bfte18trt.get(i);	
			sum9rt += bfte19trt.get(i);	
		}
		
		bfte16up.add(sum6up/bfte16tup.size());
		bfte17up.add(sum7up/bfte16tup.size());
		bfte18up.add(sum8up/bfte16tup.size());
		bfte19up.add(sum9up/bfte16tup.size());
		
		bfte16rt.add(sum6rt/bfte16trt.size());
		bfte17rt.add(sum7rt/bfte16trt.size());
		bfte18rt.add(sum8rt/bfte16trt.size());
		bfte19rt.add(sum9rt/bfte16trt.size());
		
		bf.add(bfte10up);bf.add(bfte11up);bf.add(bfte12up);bf.add(bfte13up);bf.add(bfte14up);bf.add(bfte15up);bf.add(bfte16up);bf.add(bfte17up);bf.add(bfte18up);bf.add(bfte19up);
		bf.add(bfte10rt);bf.add(bfte11rt);bf.add(bfte12rt);bf.add(bfte13rt);bf.add(bfte14rt);bf.add(bfte15rt);bf.add(bfte16rt);bf.add(bfte17rt);bf.add(bfte18rt);bf.add(bfte19rt);
		return bf;
	}
//	交换list两个元素的位置
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void swap(ArrayList li,int i,int j){
		Object obj =  li.get(i);
		li.set(i,li.get(j));
		li.set(j,obj);
	}	
//	各个轨迹长宽比、平均速度比、分段压力均值
	public static ArrayList<ArrayList<Double> >ratioofld(ArrayList<ArrayList<Double>> segpoint,ArrayList<ArrayList<DataType>> rotateddata){
		ArrayList<ArrayList<Double>> bf = new ArrayList<ArrayList<Double>>();
//		分段位移
		ArrayList<ArrayList<Double>> templength = new ArrayList<ArrayList<Double>>();
//		分段距离
		ArrayList<ArrayList<Double>> tempdist = new ArrayList<ArrayList<Double>>();
//		分段数据点密度
		ArrayList<ArrayList<Double>> tempdensity = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> tempvelocity = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> temp1pr = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> temp2pr = new ArrayList<ArrayList<Double>>();
		double seg1length,seg2length,seg1x,seg1y,seg2x,seg2y,segx,segy,seg1sumpr,seg2sumpr,seg1avgpr,seg2avgpr;
		double seg1dist,seg2dist,seg1den,seg2den;
		long seg1t,seg2t,segt,seg1lasttime,seg2lasttime;		
		int segid;
		for (int i = 0; i < rotateddata.size(); i++) {
			ArrayList<Double> ratiolength1 = new ArrayList<Double>();
			ArrayList<Double> ratiolength2 = new ArrayList<Double>();
			ArrayList<Double> ratiodist1 = new ArrayList<Double>();
			ArrayList<Double> ratiodist2 = new ArrayList<Double>();
			ArrayList<Double> ratiodensity1 = new ArrayList<Double>();
			ArrayList<Double> ratiodensity2 = new ArrayList<Double>();
			ArrayList<Double> ratioavgvelocity = new ArrayList<Double>();
			ArrayList<DataType> singlefinger = new ArrayList<DataType>();
			ArrayList<Double> pr1 = new ArrayList<Double>();
			ArrayList<Double> pr2 = new ArrayList<Double>();
			singlefinger = rotateddata.get(tempclone.get(i).get(3).intValue());
			segid = tempclone.get(i).get(0).intValue();
			segx = singlefinger.get(tempclone.get(i).get(0).intValue()).x;
			segy = singlefinger.get(tempclone.get(i).get(0).intValue()).y;
			seg1x = singlefinger.get(0).x;
			seg1y = singlefinger.get(0).y;
			seg2x = singlefinger.get(singlefinger.size()-1).x;
			seg2y = singlefinger.get(singlefinger.size()-1).y;
//			第一段位移、第二段位移
			seg1length = Math.sqrt((seg1x-segx)*(seg1x-segx)+(seg1y-segy)*(seg1y-segy));
			seg2length = Math.sqrt((seg2x-segx)*(seg2x-segx)+(seg2y-segy)*(seg2y-segy));
//			第一段距离、第二段距离
			seg1dist = 0;seg2dist = 0;
			for(int j = 1;j < segid+1; j++) {
				seg1dist += Math.sqrt((singlefinger.get(j).x - singlefinger.get(j-1).x)*(singlefinger.get(j).x - singlefinger.get(j-1).x)+
						(singlefinger.get(j).y - singlefinger.get(j-1).y)*(singlefinger.get(j).y - singlefinger.get(j-1).y));
			}
			for(int j = segid+1;j < singlefinger.size(); j++){
				seg2dist += Math.sqrt((singlefinger.get(j).x - singlefinger.get(j-1).x)*(singlefinger.get(j).x - singlefinger.get(j-1).x)+
						(singlefinger.get(j).y - singlefinger.get(j-1).y)*(singlefinger.get(j).y - singlefinger.get(j-1).y));		
			}
			for(int j = 1;j<segid+1; j++){
				Log.i("dist1", singlefinger.get(j).x+" "+singlefinger.get(j).y);			
			}
			Log.i("dist1", "    ");			
			for(int j = segid+1;j < singlefinger.size(); j++){
				Log.i("dist1", singlefinger.get(j).x+" "+singlefinger.get(j).y);			
			}
			Log.i("dist1", "    ");	
//			第一段点密度、第二段点密度
			seg1den = seg1length/segid;
			seg2den = seg2length/(singlefinger.size()-segid);
			segt = singlefinger.get(tempclone.get(i).get(0).intValue()).time;
			seg1t = singlefinger.get(0).time;
			seg2t = singlefinger.get(singlefinger.size()-1).time;
//			第一段持续时间、第二段持续时间
			seg1lasttime = segt-seg1t;
			seg2lasttime = seg2t-segt;
			ratiolength1.add(seg1length);
			ratiolength2.add(seg2length);
			ratiodist1.add(seg1dist);
			ratiodist2.add(seg2dist);
			ratiodensity1.add(seg1den);
			ratiodensity2.add(seg2den);
			
			/*if(seg2length!=0)
				ratiolength.add(seg1length/seg2length);
			else 
				ratiolength.add(100.0);						
			if((seg2length*seg1lasttime)!=0)
				ratioavgvelocity.add((double) ((seg1length*seg2lasttime)/(seg2length*seg1lasttime)));
			else 
				ratioavgvelocity.add(100.0);	*/
			templength.add(ratiolength1);
			templength.add(ratiolength2);
			tempdist.add(ratiodist1);
			tempdist.add(ratiodist2);
			tempdensity.add(ratiodensity1);
			tempdensity.add(ratiodensity2);
//			tempvelocity.add(ratioavgvelocity);
			
			/*seg1sumpr = 0;
			seg2sumpr = 0;
			for (int n = 0; n < singlefinger.size(); n++) {				
				if (n<=segid) {
					seg1sumpr+=singlefinger.get(n).pressure;
				}else {
					seg2sumpr+=singlefinger.get(n).pressure;
				}				
			}
			seg1avgpr = seg1sumpr/(segid+1);
			if ((singlefinger.size()-segid-1)!=0) {
				seg2avgpr = seg2sumpr/(singlefinger.size()-segid-1);
			}else {
				seg2avgpr = 100.0;
			}
			pr1.add(seg1avgpr);
			pr2.add(seg2avgpr);
			temp1pr.add(pr1);
			temp2pr.add(pr2);*/
		}
		for (int j = 0; j < templength.size(); j++) {
			bf.add(templength.get(j));
			bf.add(tempdist.get(j));			
		}
		for (int j = 0; j < tempdensity.size(); j++) {
			bf.add(tempdensity.get(j));
		}
//		for (int k = 0; k < tempvelocity.size(); k++) {
//			bf.add(tempvelocity.get(k));
//		}
//		for (int p = 0; p < temp1pr.size(); p++) {
//			bf.add(temp1pr.get(p));
//		}for (int q = 0; q < temp2pr.size(); q++) {
//			bf.add(temp2pr.get(q));
//		}				
		
		return bf;
	}
	
	public static ArrayList<ArrayList<Double> >lasttimeofld(ArrayList<ArrayList<Double>> segpoint,ArrayList<ArrayList<DataType>> rotateddata){
		ArrayList<ArrayList<Double>> bf = new ArrayList<ArrayList<Double>>();
		double seg1length,seg2length,seg1x,seg1y,seg2x,seg2y,segx,segy;		
		for (int i = 0; i < rotateddata.size(); i++) {
			ArrayList<Double> ratio = new ArrayList<Double>();
			ArrayList<DataType> singlefinger = new ArrayList<DataType>();
			singlefinger = rotateddata.get(tempclone.get(i).get(3).intValue());
			segx = singlefinger.get(tempclone.get(i).get(0).intValue()).x;
			segy = singlefinger.get(tempclone.get(i).get(0).intValue()).y;
			seg1x = singlefinger.get(0).x;
			seg1y = singlefinger.get(0).y;
			seg2x = singlefinger.get(singlefinger.size()-1).x;
			seg2y = singlefinger.get(singlefinger.size()-1).y;
			seg1length = Math.sqrt((seg1x-segx)*(seg1x-segx)+(seg1y-segy)*(seg1y-segy));
			seg2length = Math.sqrt((seg2x-segx)*(seg2x-segx)+(seg2y-segy)*(seg2y-segy));
			ratio.add(seg1length/seg2length);
			bf.add(ratio);
		}
		return bf;
	}
	
}
