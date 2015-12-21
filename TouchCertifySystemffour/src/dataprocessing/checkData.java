package dataprocessing;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import mobi.xjtusei.touchcertifysystemffour.MainActivity;


import android.util.Log;


public class checkData {
	public static String CorrectUser = "";
//	--------------------------------------------------------------------------------------------><
	public static String [] WrongUsers;
//	--------------------------------------------------------------------------------------------><
	@SuppressWarnings("unused")
	public static String traindata() throws Exception {
		ArrayList <ArrayList<DataType>> fourFingerData = new ArrayList <ArrayList<DataType>> ();
		ArrayList <ArrayList<DataType>> tempData = new ArrayList <ArrayList<DataType>> ();
		ArrayList<ArrayList<Double>> checkingfeatureData = new ArrayList <ArrayList<Double>> ();
		ArrayList<ArrayList<Double>> featureData = new ArrayList <ArrayList<Double>> ();
		String rescat = "suc";
		File AllUsersFile = new File(dataSave.systemDir+"/trainData_fixed");
		String []AllUserslist = AllUsersFile.list();
//	    WrongUsers = WrongUserslist;
		CorrectUser = MainActivity.username;
//		CorrectUser = uString;
//		Log.i("cuser", CorrectUser);
		boolean res = true;
//		����Ϸ��û��ڷǷ��û����У�������Ӧ�ķǷ��û�
//		--------------------------------------------------------------------------------------------><
		if (AllUserslist.length<4) {
			rescat = "����ѵ��"+(4 - AllUserslist.length)+"���û�";			
		}else {
			String [] tmpwrong = new String[AllUserslist.length-1];
			int wunt = 0;
			for (int i = 0; i < AllUserslist.length; i++) {
				if (CorrectUser.equalsIgnoreCase(AllUserslist[i])) {

				}
				else {
					tmpwrong[wunt] = AllUserslist[i];
					wunt++;						
				}
			}
			WrongUsers = tmpwrong;
//		--------------------------------------------------------------------------------------------><
			Log.i("cuser", "r "+CorrectUser);
			for (int i = 0; i < WrongUsers.length; i++) {
				Log.i("cuser", "w "+WrongUsers[i]);
			}
			for(int mm = 0;mm<(1+WrongUsers.length);mm++){
				String file = dataSave.systemDir+"/trainData_fixed";
				String name;
				int nnnum=0;
				if (mm == 0) {
					name = CorrectUser;
				}else {
					name = WrongUsers[mm-1];
				}
				File userone = new File(file+"/"+name);
				String [] datalist = userone.list();
				if (mm == 0) {
					nnnum = datalist.length;
				}else {
//					nnnum = 5>datalist.length?datalist.length:5;
					nnnum = datalist.length;
				}
				Log.i("train",file+"/"+name);
				File TrainedName = new File(dataSave.featureDataDir+"/"+name);  
				Log.i("AlreadyName",TrainedName+"");
				if (TrainedName.isDirectory()){
					String [] TrainedArr = TrainedName.list();
					Log.i("AlreadyName",TrainedName+" "+TrainedArr.length);
					if (TrainedArr.length>=nnnum){
						Log.i("Alreadyexist",TrainedArr.length+" "+nnnum);
						continue;
					}
				}

				Log.i("AlreadyexistNOT",nnnum+"");
				for(int nn = 0;nn<nnnum;nn++){
					String txt = datalist[nn];
					Log.i("train",file+"/"+name+"/"+txt);
					res = checkFormat.checkData(file+"/"+name+"/"+txt);
					Log.i("trainresult",res+"");
					if(res){
						try {
							fourFingerData = checkFormat.FFData;  //�����޸ĺ������
						} catch (Exception e) {
							rescat = file+"/"+name+"/"+txt+"\n��ʽ������ɾ���ļ�����";
							return rescat;
						}
						try {
							boolean savedata = dataSave.fingerSaving(fourFingerData, name, txt);
						} catch (Exception e) {
							e.printStackTrace();
							Log.i("train","���ݱ������");
							System.out.println("���ݱ������");
						}
						//-------------------------------------------------------------------------------------------------						
						try {
							checkingfeatureData = featureExtraction.extractcheckedFea(fourFingerData); //�ֶε�
						} catch (Exception e) {
							rescat = file+"/"+name+"/"+txt+"\n��ʽ������ɾ���ļ�����";
							return rescat;
						}
						try {
							boolean savefeature4check = dataSave.feature4checkSaving(checkingfeatureData, name, txt);
						} catch (Exception e) {
							e.printStackTrace();
							Log.i("train","���������������");
							System.out.println("���������������");
						}
						//	-------------------------------------------------------------------------------------------------						
						try {
							tempData = featureExtraction.rotatedData; //��ת�������							
						} catch (Exception e) {
							rescat = file+"/"+name+"/"+txt+"\n��ʽ������ɾ���ļ�����";
							return rescat;
						}
						try {
							boolean savetemp = dataSave.tempSaving(tempData, name, txt);
						} catch (Exception e) {
							e.printStackTrace();
							Log.i("train","�м�����������");
							System.out.println("�м�����������");
						}
						//	-------------------------------------------------------------------------------------------------					
						try {
							featureData = featureExtraction.extractFea(tempData);  //��������							
						} catch (Exception e) {
							rescat = file+"/"+name+"/"+txt+"\n��ʽ������ɾ���ļ�����";
							return rescat;
						}		
						try {
							boolean savefeature = dataSave.featureSaving(featureData, name, txt);
						} catch (Exception e) {
							e.printStackTrace();
							Log.i("train","�����������");
							System.out.println("�����������");
						}
						// -------------------------------------------------------------------------------------------------

					}else {
						Log.i("train","�ļ���ʽ����");
						System.out.println("�ļ���ʽ����");
						rescat = file+"/"+name+"/"+txt+"\n��ʽ������ɾ���ļ�����";
						return rescat;
					}
				}
			}		
			return trainmodel.TrainSave(CorrectUser, WrongUsers,"ALL");
		}

		return rescat;
	}

}
