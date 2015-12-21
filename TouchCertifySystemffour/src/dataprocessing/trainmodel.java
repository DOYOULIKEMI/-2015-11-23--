package dataprocessing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import mobi.xjtusei.touchcertifysystemffour.MainActivity;
import mobi.xjtusei.touchcertifysystemffour.UserInfo;

import android.R.string;
import android.util.Log;


public class trainmodel {
	public static String  TrainSave(String correctUsers,String [] wrongUsers,String num4train) throws Exception{
		String useraddr = dataSave.featureDataDir;
		File correctUserFile = new File(useraddr+"/"+correctUsers);
		String [] corlist = correctUserFile.list();
		ArrayList<ArrayList<String>> fea4correct = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> fea4write = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> fea4writeTMP = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Double>> minMax4write = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> normalizedfeature = new ArrayList<ArrayList<Double>>();
//		int legalnum = corlist.length;
		String res = "已将 "+correctUsers+" 确定为合法用户";
		int legalnum = 0;
		if(num4train.equals("ALL"))
			legalnum = corlist.length;
		else{ 
			try {
				legalnum = Integer.parseInt(num4train);				
				if(legalnum>corlist.length)
					legalnum = corlist.length;				
			} catch (Exception e) {
				legalnum =	corlist.length;
			}
		}
		for (int i = 0; i < corlist.length; i++) {
			Log.i("u4t",corlist[i]+"");
		}
		int illegalnumper = 0;
		if(wrongUsers.length!=0)
			illegalnumper	= (int) Math.ceil((double)legalnum/wrongUsers.length);
//		生成合法用户特征集
		for (int i = 0; i < legalnum; i++) {
			File corsinglefea = new File(correctUserFile+"/"+corlist[i]);
			FileInputStream fin = new FileInputStream(corsinglefea);
			InputStreamReader inR = new InputStreamReader(fin);
			BufferedReader bfR  = new BufferedReader(inR);
			String temp1 = "";
			ArrayList<String> tempfea = new ArrayList<String>();
			try {
				while((temp1 = bfR.readLine()) != null){
					tempfea.add(temp1); 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			fea4correct.add(tempfea);
			bfR.close();
			inR.close();
			fin.close();
		}
//		System.out.println("---------------------");
//		for (int i = 0; i < fea4correct.size(); i++) {
//			System.out.println(fea4correct.get(i).toString());
//			Log.i("train2",fea4correct.get(i).toString());
//		}
//		生成非法用户特征集	
		fea4write.addAll(fea4correct);
		Log.i("sortbefore",fea4write.size()+"");
		for (int k = 0;k<wrongUsers.length;k++){
			File wrongUserFile = new File(useraddr+"/"+wrongUsers[k]);						
			if (wrongUserFile.isDirectory()) {
				String [] wrolist = wrongUserFile.list();
				if (wrolist.length >= illegalnumper) {
					for (int i = 0; i < wrolist.length; i++) {
						File worsinglefea = new File(wrongUserFile+"/"+wrolist[i]);
						FileInputStream fin = new FileInputStream(worsinglefea);
						InputStreamReader inR = new InputStreamReader(fin);
						BufferedReader bfR  = new BufferedReader(inR);
						String temp1 = "";
						ArrayList<String> tempfea = new ArrayList<String>();
						try {
							while((temp1 = bfR.readLine()) != null){
								tempfea.add(temp1); 
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						fea4writeTMP.add(tempfea);
						bfR.close();
						inR.close();
						fin.close();
					}					
				}else {
					res = "用户 " + wrongUsers[k]+" 样本数据量不够，请再次训练";
					//UserInfo.delALLTrainFiles(new File(dataSave.systemProDir));					return res;		
				}				
			}else {
				res = "用户 " + wrongUsers[k]+" 样本数据量不够，请再次训练";
				//UserInfo.delALLTrainFiles(new File(dataSave.systemProDir));
				return res;		
			}
		}
		Log.i("sortbefore",fea4writeTMP.size()+"");
		int []seriesList = ArrayListToAry(fea4writeTMP.size());		
		int startnum = fea4write.size();
		for(int j = 0; j<startnum; j++){
			int t = seriesList[j];				
			Log.i("sortbefore",t+"");
			fea4write.add(fea4writeTMP.get(t));
		}
//		System.out.println("---------------------");
//		for (int i = 0; i < fea4write.size(); i++) {
////			System.out.println(fea4write.get(i).toString());
//			Log.i("train3",fea4write.get(i).toString());
//		}

		minMax4write = minMax(fea4correct);
		normalizedfeature = normalize(fea4write, minMax4write);
		boolean rest = dataSave.trainTxTSaving(normalizedfeature,  legalnum);
		boolean resm = dataSave.minMaxValueSaving(minMax4write);
		return res;
	}
//	求归一化参量
	public static ArrayList<ArrayList<Double>> minMax(ArrayList<ArrayList<String>> trainsample){
		ArrayList<ArrayList<Double>> mM = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> M = new ArrayList<Double>();
		ArrayList<Double> m = new ArrayList<Double>();
		double [] temp = new double[trainsample.size()];
		for (int j = 0; j < trainsample.get(0).size(); j++) {
			for(int i = 0;i<trainsample.size();i++){
				temp[i] = Double.parseDouble(trainsample.get(i).get(j));
			}
			Arrays.sort(temp);
			m.add(temp[0]);
			M.add(temp[temp.length-1]);
		}
		mM.add(m);
		mM.add(M);
//		System.out.println("---------------------");
//		for (int i = 0; i < mM.size(); i++) {
//			System.out.println(mM.get(i).toString());
//		}		
//		System.out.println("---------------------");
		return mM;
	}
//	归一化（原始特征，归一化参量）
	public static ArrayList<ArrayList<Double>> normalize(ArrayList<ArrayList<String>> trainsample,ArrayList<ArrayList<Double>> mM){
		ArrayList<ArrayList<Double>> normalizedFea = new ArrayList<ArrayList<Double>>();
		ArrayList<Double>[] normalizedSingle = new ArrayList [trainsample.size()];
		double maxscale,minval;
		double [][]nor = new double[trainsample.size()][trainsample.get(0).size()];
		for (int j = 0; j < trainsample.get(0).size(); j++) {
//			[0 1]之间
//			maxscale = mM.get(1).get(j) - mM.get(0).get(j);
//			minval = mM.get(0).get(j);
//			[-1 1]之间
			maxscale = 0.5*(mM.get(1).get(j) - mM.get(0).get(j));
			minval = 0.5*(mM.get(1).get(j) + mM.get(0).get(j));

			for (int i = 0; i < trainsample.size(); i++) {
//				Log.i("test",Double.parseDouble(trainsample.get(i).get(j))+"");
				nor[i][j] = (Double.parseDouble(trainsample.get(i).get(j))-minval)/maxscale;
//				Log.i("test",nor[i][j]+"");
			}
		}
		for(int i = 0;i<trainsample.size();i++){
			ArrayList<Double> temp = new ArrayList<Double>();
			for (int j = 0; j < trainsample.get(0).size(); j++) {
				temp.add(nor[i][j]);
			}
			normalizedSingle[i] = temp;
			normalizedFea.add(normalizedSingle[i]);
		}
//		for (int i = 0; i < normalizedFea.size(); i++) {
//			System.out.println(normalizedFea.get(i).toString());
//		}
		return normalizedFea;
	}
	
	public static void TestSave() throws Exception{
		String useraddr = dataSave.trainTxTDataDir;
		boolean res = checkFormat.checkData(useraddr+"/"+"testData.txt");
		if(res){
			ArrayList<ArrayList<DataType>> fourFingerData = checkFormat.FFData;
			ArrayList<ArrayList<Double>> checkingfeatureData = featureExtraction.extractcheckedFea(fourFingerData);
			ArrayList<ArrayList<DataType>> tempData = featureExtraction.rotatedData;
			ArrayList<ArrayList<Double>> featureData = featureExtraction.extractFea(tempData);
			try {		
				boolean savecheckdatatrain=  dataSave.featureforcheckTxTSaving(checkingfeatureData,"checkingfeatureData");
				boolean savefeaturedatatrain=  dataSave.featureforcheckTxTSaving(featureData,"featureData");
				boolean  checkingfeaturetrain=dataSave.checkedfeatureforcheckTxTSaving(tempData,"tempData");
				boolean  fourfeaturetrain=dataSave.checkedfeatureforcheckTxTSaving(fourFingerData,"fourFingerData");
				boolean savefeature = dataSave.TestfeatureSaving(featureData);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("train","特征保存错误！");
				System.out.println("特征保存错误！");
			}
		}
	}
	
	public static int[] ArrayListToAry(int num) {
		
		int[] ary = new int[num];
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			temp.add(i);
		}
		int index = 0;
		while (true) {
			if(list.size()==num){
				break;
			}
			int it = temp.get((int) (Math.random() * num));
			if (list.contains(it)) {
				continue;
			} else {
				ary[index] = it;
				list.add(it);
				index++;
			}
		}
		for (int i = 0; i < ary.length; i++) {
			Log.i("sort",ary[i] + " ");
		}
		return ary;
	}
	
}
