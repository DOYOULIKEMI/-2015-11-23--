package dataprocessing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.R.string;
import android.util.Log;


public class dataSave {
	public static String sdcard = android.os.Environment.getExternalStorageDirectory().toString(); 
	public static String systemProDir = sdcard+"/CertSystemData4/CDataPocessing" ;
	public static String systemDir = sdcard+"/CertSystemData4" ;
	public static String featureDataDir = systemProDir+"/"+"featureDataSave";
	public static String fingerDataDir = systemProDir+"/"+"fingerDataSave";
	public static String tempDataDir = systemProDir+"/"+"tempDataSave";
	public static String feature4checkDataDir = systemProDir+"/"+"feature4checkDataSave";
	public static String trainTxTDataDir = systemProDir+"/"+"TrainTxT";
	public static boolean featureSaving(ArrayList<ArrayList<Double>> fea,String user,String usernum) throws Exception{
		boolean res = true;
		File rootfile = new File(featureDataDir+"/"+user);
		rootfile.delete();
		if(!rootfile.exists())
			rootfile.mkdirs();
		File singlefea = new File(rootfile+"/"+usernum);
		FileOutputStream op;
		op = new FileOutputStream(singlefea);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<fea.size();i++){
			ArrayList<Double> feaSeg  = fea.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j) + " ");
			}
			pw.println();
			//pw.println("the end of one feature");
		}
		pw.close();
		op.close();

		return res;
	}
	
	public static boolean fingerSaving(ArrayList<ArrayList<DataType>> finger,String user,String usernum) throws Exception{
		boolean res = true;
		File rootfile = new File(fingerDataDir+"/"+user);
		rootfile.delete();
		if(!rootfile.exists()){
			rootfile.mkdirs();
		}
		File singlefea = new File(rootfile+"/"+usernum);
		FileOutputStream op;
		op = new FileOutputStream(singlefea);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<finger.size();i++){
			ArrayList<DataType> feaSeg  = finger.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j).x + " "+feaSeg.get(j).y + " "+feaSeg.get(j).time + " "+feaSeg.get(j).pressure 
						+ " "+feaSeg.get(j).id + " "+feaSeg.get(j).tool + " "+feaSeg.get(j).touch + " ");
				pw.println();
			}
//			pw.println();
			//pw.println("the end of one feature");
		}
		pw.close();
		op.close();
		
		return res;
	}
	
	public static boolean tempSaving(ArrayList<ArrayList<DataType>> finger,String user,String usernum) throws Exception{
		boolean res = true;
		File rootfile = new File(tempDataDir+"/"+user);
		rootfile.delete();
		if(!rootfile.exists()){
			rootfile.mkdirs();
		}
		File singlefea = new File(rootfile+"/"+usernum);
		FileOutputStream op;
		op = new FileOutputStream(singlefea);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<finger.size();i++){
			ArrayList<DataType> feaSeg  = finger.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j).x + " "+feaSeg.get(j).y + " "+feaSeg.get(j).time + " "+feaSeg.get(j).pressure 
						+ " "+feaSeg.get(j).id + " "+feaSeg.get(j).tool + " "+feaSeg.get(j).touch + " ");
				pw.println();
			}
//			pw.println();
			//pw.println("the end of one feature");
		}
		pw.close();
		op.close();
		
		return res;
	}
	
	public static boolean feature4checkSaving(ArrayList<ArrayList<Double>> fea,String user,String usernum) throws Exception{
		boolean res = true;
		File rootfile = new File(feature4checkDataDir+"/"+user);
		rootfile.delete();
		if(!rootfile.exists())
			rootfile.mkdirs();
		File singlefea = new File(rootfile+"/"+usernum);
		FileOutputStream op;
		op = new FileOutputStream(singlefea);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<fea.size();i++){
			ArrayList<Double> feaSeg  = fea.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j) + " ");
			}
			pw.println();
			//pw.println("the end of one feature");
		}
		pw.close();
		op.close();

		return res;
	}
	
	public static boolean trainTxTSaving(ArrayList<ArrayList<Double>> nor,int num) throws Exception{
		boolean res = true;
		File rootfile = new File(trainTxTDataDir);
		if(!rootfile.exists())
			rootfile.mkdirs();
		File usertraintxt = new File(rootfile+"/"+"train"+".txt");
		FileOutputStream op;
		op = new FileOutputStream(usertraintxt);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<nor.size();i++){
			ArrayList<Double> feaSeg  = nor.get(i);
			if(i<num)pw.print("+1 ");
			else pw.print("-1 ");
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(j+1+":"+feaSeg.get(j) + " ");
			}
			pw.println();
		}
		pw.close();
		op.close();
		return res;
	}
	
	public static boolean minMaxValueSaving(ArrayList<ArrayList<Double>> fea) throws Exception{
		boolean res = true;
		File rootfile = new File(trainTxTDataDir+"/");
		if(!rootfile.exists())
			rootfile.mkdirs();
		File singlefea = new File(rootfile+"/"+"minMaxValue"+".txt");
		FileOutputStream op;
		op = new FileOutputStream(singlefea);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<fea.size();i++){
			ArrayList<Double> feaSeg  = fea.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j) + " ");
			}
			pw.println();
		}
		pw.close();
		op.close();

		return res;
	}	
	
	public static boolean testTxTSaving(ArrayList<ArrayList<Double>> nor) throws Exception{
		boolean res = true;
		File rootfile = new File(trainTxTDataDir);
		if(!rootfile.exists())
			rootfile.mkdirs();
		File usertraintxt = new File(rootfile+"/"+"test"+".txt");
		FileOutputStream op;
		op = new FileOutputStream(usertraintxt);
		PrintWriter pw = new PrintWriter(op);
		pw.print("+1 ");
		for(int i = 0;i<nor.size();i++){
			ArrayList<Double> feaSeg  = nor.get(i);
			for (int j = 0; j < feaSeg.size(); j++) {
				pw.print(j+1+":"+feaSeg.get(j) + " ");
			}
		}
		pw.close();
		op.close();
		return res;
	}
	public static boolean featureforcheckTxTSaving(ArrayList<ArrayList<Double>> nor,String fileniame) throws Exception{
		boolean res = true;
		File rootfile = new File(trainTxTDataDir);
		if(!rootfile.exists())
			rootfile.mkdirs();
		File usertraintxt = new File(rootfile+"/"+fileniame+".txt");
		FileOutputStream op;
		op = new FileOutputStream(usertraintxt);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<nor.size();i++){
			ArrayList<Double> feaSeg  = nor.get(i);
			for (int j = 0; j < feaSeg.size(); j++) {
				pw.print(feaSeg.get(j) + " ");
			}
			pw.print("\n");
		}
		pw.close();
		op.close();
		return res;
	}
	public static boolean TestfeatureSaving(ArrayList<ArrayList<Double>> fea) throws Exception{
		boolean res = true;
//		----------------------------------------------------------------------------------><
		double bd = 10.0;
//		----------------------------------------------------------------------------------><
		ArrayList<ArrayList<Double>> minMaxRead = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<String>> StringFea = new ArrayList<ArrayList<String>>();
		String useraddr = dataSave.trainTxTDataDir;
		File rootfile = new File(trainTxTDataDir);
		if(!rootfile.exists())
			rootfile.mkdirs();
		File mMFile = new File(useraddr+"/"+"minMaxValue.txt");
		FileInputStream fin2 = new FileInputStream(mMFile);
		InputStreamReader inR2 = new InputStreamReader(fin2);
		BufferedReader bfR2  = new BufferedReader(inR2);	
		String temp1;
		try {
			while((temp1 = bfR2.readLine()) != null){
				ArrayList<Double> tempmM = new ArrayList<Double>();
				String []s = temp1.split(" ");
				for (int i = 0; i < s.length; i++) {
					Double ss = Double.parseDouble(s[i]);
					tempmM.add(ss);
				}
				minMaxRead.add(tempmM);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		bfR2.close();
		inR2.close();
		fin2.close();
		
		for (int i = 0; i < fea.size(); i++) {
			ArrayList<String> tempStringFea = new ArrayList<String> ();
			for (int j = 0; j < fea.get(i).size(); j++) {
				tempStringFea.add(fea.get(i).get(j).toString());
			}
			StringFea.add(tempStringFea);
//			Log.i("StringFea",tempStringFea.toString());
		}
		ArrayList<ArrayList<String>> newonelinefea = orgTestfeatureSaving(StringFea);
		ArrayList<ArrayList<Double>> normalizedfeature = trainmodel.normalize(newonelinefea, minMaxRead);
		ArrayList<ArrayList<Double>> normalizedfeature_afterbd = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> tmp_afterbd = new ArrayList<Double>();
		for (int i = 0; i < normalizedfeature.get(0).size(); i++) {
			if (normalizedfeature.get(0).get(i)>bd||normalizedfeature.get(0).get(i)<-1*bd) {
				tmp_afterbd.add(10.0);
			}else {
				tmp_afterbd.add(normalizedfeature.get(0).get(i));
			}			
		}
		normalizedfeature_afterbd.add(tmp_afterbd);
		Log.i("stringFea",normalizedfeature.toString());
//		res = testTxTSaving(normalizedfeature);
		res = testTxTSaving(normalizedfeature_afterbd);

		return res;
	}
	public static boolean checkedfeatureforcheckTxTSaving(ArrayList<ArrayList<DataType>> nor,String fileniame) throws Exception{
		boolean res = true;
		File rootfile = new File(trainTxTDataDir);
		if(!rootfile.exists())
			rootfile.mkdirs();
		File usertraintxt = new File(rootfile+"/"+fileniame+".txt");
		FileOutputStream op;
		op = new FileOutputStream(usertraintxt);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<nor.size();i++){
			ArrayList<DataType> feaSeg  = nor.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j).x + " "+feaSeg.get(j).y + " "+feaSeg.get(j).time + " "+feaSeg.get(j).pressure 
						+ " "+feaSeg.get(j).id + " "+feaSeg.get(j).tool + " "+feaSeg.get(j).touch + " ");
				pw.println();
			}
//			pw.println();
			//pw.println("the end of one feature");
		}
		pw.close();
		op.close();
		
		return res;
	}
	public static ArrayList<ArrayList<String>> orgTestfeatureSaving(ArrayList<ArrayList<String>> fea) throws Exception{
		boolean res = true;
		File rootfile = new File(trainTxTDataDir+"/"+"testFeature.txt");
		FileOutputStream op;
		op = new FileOutputStream(rootfile);
		PrintWriter pw = new PrintWriter(op);
		for(int i = 0;i<fea.size();i++){
			ArrayList<String> feaSeg  = fea.get(i);
			for(int j = 0;j<feaSeg.size();j++){
				pw.print(feaSeg.get(j) + " ");
			}
			pw.println();
			//pw.println("the end of one feature");
		}
		pw.close();
		op.close();
		
		ArrayList<ArrayList<String>> onelinefea  = new ArrayList<ArrayList<String>>();
		ArrayList<String> temponelinefea  = new ArrayList<String>();
		for(int i = 0;i<fea.size();i++){
			temponelinefea.add(fea.get(i).get(0));			
		}
//		Log.i("stringFea",temponelinefea.toString());
		onelinefea.add(temponelinefea);
		return onelinefea;
	}
}
