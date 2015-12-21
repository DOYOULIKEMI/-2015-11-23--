package dataprocessing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class checkFormat {
	public static ArrayList<ArrayList<DataType>> FFData = new ArrayList<ArrayList<DataType>>();
	public static boolean checkData(String filePath) throws IOException{
		boolean sgn = true;
		int finnum = 0;
		BufferedReader reader = null;
		ArrayList<DataType> DataAll = new ArrayList<DataType>();
		ArrayList<DataType> DataAllafDel = new ArrayList<DataType>();
		ArrayList<ArrayList<DataType>> RawDataFingers = new ArrayList<ArrayList<DataType>>() ;
		ArrayList<ArrayList<DataType>> ModifiedDataFingers = new ArrayList<ArrayList<DataType>>() ;
		ArrayList<DataType>  fin0 = new ArrayList<DataType>();
		ArrayList<DataType>  fin1 = new ArrayList<DataType>();
		ArrayList<DataType>  fin2 = new ArrayList<DataType>();
		ArrayList<DataType>  fin3 = new ArrayList<DataType>();
		File rawdata = new File(filePath);
		try {
			reader = new BufferedReader(new FileReader(rawdata));
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				String []ss = tempString.split(" ");
				DataType d = new DataType(
						Integer.parseInt(ss[0].trim()),
						Integer.parseInt(ss[1].trim()),
						Long.parseLong(ss[2].trim()),
						Float.parseFloat(ss[3].trim()),
						Integer.parseInt(ss[4].trim()),
						Integer.parseInt(ss[5].trim()),
						Integer.parseInt(ss[6].trim())
						);
				DataAll.add(d);				
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.i("test","�ļ��򿪴���");
			System.out.println();
		}finally {
            if (reader != null) {
                try {
                    reader.close();
                	} catch (IOException e1) {
                }
            }
        }
		Log.i("test1",""+"ȥ����6����");
		//ȥ����6����
		if (DataAll.size()<20){
			System.out.println("����������20��");
			return false;			
		}else{
			for(int i = 0;i<6;i++)
				DataAll.remove(DataAll.size()-1);		
			for(int i = 0;i<5;i++)
				DataAll.remove(1);		
		}	
//		ֻ����һ�β������������ĵ㣨3��4ָ���ݣ�
//		int count4del = 0; 
		Log.i("test1",""+"������������");
		for(int i = 0;i<DataAll.size() - 3;i++){
			if ((DataAll.get(i).id == 0) && (DataAll.get(i+1).id == 1) &&
					(DataAll.get(i+2).id == 2) && (DataAll.get(i+3).id == 3)){
				DataAllafDel.add(DataAll.get(i));
				DataAllafDel.add(DataAll.get(i+1));
				DataAllafDel.add(DataAll.get(i+2));
				DataAllafDel.add(DataAll.get(i+3));
				i = i + 4;
			}			
		}
		Log.i("test1",""+"���ݸ�ֵ");
		//��ȫ��������DataAll��ֵ������fin
		for(int i = 0;i<DataAllafDel.size();i++){
			if (DataAllafDel.get(i).id == 0) {
				fin0.add(DataAllafDel.get(i));
			} else if(DataAllafDel.get(i).id == 1) {
				fin1.add(DataAllafDel.get(i));
			}else if(DataAllafDel.get(i).id == 2) {
				fin2.add(DataAllafDel.get(i));
			}else {
				fin3.add(DataAllafDel.get(i));
				finnum = 4;
			}
		}
		if (finnum != 4){
			System.out.println("����4����ָ����");
			return false;
		}
		RawDataFingers.add(fin0);
		RawDataFingers.add(fin1);
		RawDataFingers.add(fin2);
		RawDataFingers.add(fin3);
		//����ָ���
		Log.i("test1",""+"����ָ���");
		for(int i = 0;i<4;i++){
			ArrayList<DataType> tempArray = new ArrayList<DataType>();
			ArrayList<DataType> modArray = new ArrayList<DataType>();
			tempArray = RawDataFingers.get(i);
			//ɾ���ظ��ĵ�
//			tempArray = deleteRedundancy(tempArray);
			if(tempArray.size()<5){
				System.out.println("ɾ���ظ��ĵ�����ݹ���");
				return false;
			}
			if(tempArray.get(0).x-tempArray.get(tempArray.size()-1).x >-100)
				{System.out.println("�����켣����");
			    return false;}
			if(tempArray.get(0).y-tempArray.get(tempArray.size()-1).y <100)
				{System.out.println("�����켣����");
			    return false;}
			for(int j = 0;j<tempArray.size()-3;j++){
				if (dataJump(tempArray.get(j),tempArray.get(j+1))){
					System.out.println("������Ծ1");
					if (dataJump(tempArray.get(j),tempArray.get(j+2))) {
						System.out.println("������Ծ2");
						if (dataJump(tempArray.get(j),tempArray.get(j+3))) {
							System.out.println("������Ծ3");
							return false;
						}else {
							tempArray.remove(j+1);
							tempArray.remove(j+1);
						}						
					}else {
						tempArray.remove(j+1);
					}					
				}
			}
//			for(int k = 0;k<tempArray.size();k++){
//				System.out.println(tempArray.get(k).x+" "+tempArray.get(k).y+" "+tempArray.get(k).id);
//			}
			//�ȶ���ʼλ��
			modArray = stablestart(tempArray);
			if(modArray.size()<5){
				System.out.println("�ȶ���ʼλ�õĵ�����ݹ���");
				return false;
			}
			ModifiedDataFingers.add(modArray);
		}
		FFData = ModifiedDataFingers;
		Log.i("test1",""+sgn);
		return sgn;
	}
	//�ж������Ƿ�����Ծ
	private static boolean dataJump(DataType a, DataType b){
		boolean con = false;
		if ((Math.abs(a.x - b.x)>200 && Math.abs(a.y - b.y)>100) ||
			(Math.abs(a.x - b.x)>100 && Math.abs(a.y - b.y)>200))
			con = true;		
		return con;
	}
	//ɾ���ظ��ĵ�
	private static ArrayList<DataType> deleteRedundancy(ArrayList<DataType> a){
		int i;
		for (i = 0; i < a.size()-1; i++) {
			if((int)(a.get(i).x) == (int)a.get(i+1).x && (int)a.get(i).y == (int)a.get(i+1).y){
//				System.out.println(a.get(i).x+" "+a.get(i).y+" "+3.7);
				a.remove(i);
				i = i - 1;
			}
		}
		return a;	
	}
	//���ʼ���ȶ��ĵ��޳�
	private static ArrayList<DataType> stablestart(ArrayList<DataType> a){
		int i,k ;int fl = 0;double tan;
		for(i = 0;i<a.size()-2;i++){
			if((a.get(i+1).x - a.get(i).x) == 0){
				fl = 1;
				break;
			}else {
				tan = (a.get(i+1).y - a.get(i).y)/Math.abs(a.get(i+1).x - a.get(i).x);
				if(tan<-1){
					fl = 1;
					break;
				}
			}
		}
		if(fl == 1){
			for (k = 0;k<i-1;k++)
				a.remove(1);
		}
		return a;
	} 
	
}
