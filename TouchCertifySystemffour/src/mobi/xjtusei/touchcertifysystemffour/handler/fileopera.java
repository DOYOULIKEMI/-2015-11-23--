package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;



public class fileopera {

	public static ArrayList<Double> fea_max = new ArrayList<Double>();
	
	public static void fileop( ) throws Exception {
		FileInputStream fin = new FileInputStream(UserInfo.trainFileDir+"/"+UserInfo.trainFileName);
		InputStreamReader inR = new InputStreamReader(fin);
		BufferedReader bfR  = new BufferedReader(inR);
		FileOutputStream fout = new FileOutputStream(UserInfo.trainFileDir+"/"+UserInfo.trainFileName1,true);
		OutputStreamWriter outW = new OutputStreamWriter(fout);
		BufferedWriter bfW = new BufferedWriter(outW);
		
		ArrayList<ArrayList<Double>> dist_fea = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> dist_new = new ArrayList<ArrayList<Double>>(); 
		ArrayList<Double> fea_chan = new ArrayList<Double>();

		TrainFixedModeSystem train = new TrainFixedModeSystem();
		int illegalnum = train.getIllegalNum();
		int legaltotalnum = UserInfo.getTotalTrainNum();
		Log.i("fop",illegalnum+"");
		int sepillegalnum = legaltotalnum/illegalnum;
		String temp1 = "";
		String[] temp2 = {};
		String[] temp3 = {};
		ArrayList<String> temp4 =new ArrayList<String>();
		int i,j,k;
		int usage=1;

		while((temp1=bfR.readLine())!=null)
		{	
			if ((usage<=legaltotalnum)||(((usage-1-legaltotalnum)%100)/sepillegalnum<1))
			{
				Log.i("fop","read"+usage);	
			temp2=temp1.split(" ");
			temp4.add(temp2[0]);
			for (i=0;i<temp2.length-1;i++)
			{
				temp3 = temp2[i+1].split(":");
				fea_chan.add(Double.parseDouble(temp3[1]));
			}
			dist_fea.add(fea_chan);
			fea_chan = new ArrayList<Double>();
			}
			usage=usage+1;
			
		 }
		Double fea_temp[] = new Double[dist_fea.size()];
    	for (i=0;i<dist_fea.get(0).size();i++)
		{	
			
			for (j=0;j<dist_fea.size();j++)
			{
				fea_temp[j]=(dist_fea.get(j).get(i));	
			}
			Arrays.sort(fea_temp);
			fea_max.add(fea_temp[fea_temp.length-1]);
		}
		
		for (i=0;i<dist_fea.get(0).size();i++)
		{
			for (j=0;j<dist_fea.size();j++)
			{
				fea_chan.add(dist_fea.get(j).get(i)/fea_max.get(i));
			}
			dist_new.add(fea_chan);
			fea_chan = new ArrayList<Double>();
		}

		for (j=0;j<dist_fea.size();j++)
		{	
			bfW.write(temp4.get(j));
			for (i=0;i<dist_fea.get(0).size();i++)
			{
				k=i+1;
				bfW.write(" "+Integer.toString(k)+":"+Double.toString(dist_new.get(i).get(j)));
				
			}
			Log.i("fop","write"+j);
			bfW.newLine();
		}                   
		bfR.close();
		bfW.close();// TODO Auto-generated method stub

	}
	
	
	
	public static void filetest( ) throws Exception{
		ArrayList<Double> fea_test = new ArrayList<Double>();
		ArrayList<Double> fea_test1 = new ArrayList<Double>();
		String temp1 = "";
		String[] temp2 = {};
		String[] temp3 = {};
		ArrayList<String> temp4 =new ArrayList<String>();
		int i,j,k;
		
		FileInputStream fin2 = new FileInputStream(UserInfo.testFileDir+"/"+UserInfo.testFileName);
		InputStreamReader inR2 = new InputStreamReader(fin2);
		BufferedReader bfR2  = new BufferedReader(inR2);
		FileOutputStream fout2 = new FileOutputStream(UserInfo.testFileDir+"/"+UserInfo.testFileName1,true);
		OutputStreamWriter outW2 = new OutputStreamWriter(fout2);
		BufferedWriter bfW2 = new BufferedWriter(outW2);
		
		while((temp1=bfR2.readLine())!=null)
		{
			temp2=temp1.split(" ");
			temp4.add(temp2[0]);
			for (i=0;i<temp2.length-1;i++)
			{
				temp3 = temp2[i+1].split(":");
				fea_test.add(Double.parseDouble(temp3[1]));
			}
		 }
		
		for (j=0;j<fea_test.size();j++)
		{
			fea_test1.add(fea_test.get(j)/fea_max.get(j));
		}
		
		bfW2.write(temp4.get(0));
		for (i=0;i<fea_test1.size();i++)
		{
			k=i+1;
			bfW2.write(" "+Integer.toString(k)+":"+Double.toString(fea_test1.get(i)));			
		}
		
		bfR2.close();
		bfW2.close();

	}

}
