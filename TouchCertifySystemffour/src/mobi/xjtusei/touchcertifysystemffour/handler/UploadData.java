package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import dataprocessing.checkFormat;

import android.text.format.Time;
import android.util.Log;

import mobi.xjtusei.touchcertifysystemffour.UserInfo;
import mobi.xjtusei.touchcertifysystemffour.mtcollector.MultiTouchView;

public class UploadData {
	protected static String systemDirPath;
	protected static String dataDirPath;
	protected static ExtractFeature extFea;
	protected static int feaCount;
	
	//ѵ�����ļ����浽����
	public static boolean saveTrain(String data, String username, String dirPath) throws IOException{
		/*         ��ȡ����                */
		LinkedList<Integer> x = new LinkedList<Integer>();
		LinkedList<Integer> y = new LinkedList<Integer>();
		LinkedList<Long> time = new LinkedList<Long>();
		LinkedList<Float> pressure = new LinkedList<Float>();
		LinkedList<Integer> id = new LinkedList<Integer>();
		LinkedList<Integer> tool = new LinkedList<Integer>();
		LinkedList<Integer> touch = new LinkedList<Integer>();
//		LinkedList<Integer> distance = new LinkedList<Integer>();
//		LinkedList<Integer> toolx = new LinkedList<Integer>();
		
		String[] s = data.split("\n");
		
		for(int i = 0;i < s.length;i++){
			String[] ss = s[i].split(" ");
			x.add(Integer.parseInt(ss[0].trim()));
			y.add(Integer.parseInt(ss[1].trim()));
			time.add(Long.parseLong(ss[2].trim()));
			pressure.add(Float.parseFloat(ss[3].trim()));
			id.add(Integer.parseInt(ss[4].trim()));
			tool.add(Integer.parseInt(ss[5].trim()));
			touch.add(Integer.parseInt(ss[6].trim()));
//			distance.add(Integer.parseInt(ss[5].trim()));
//			toolx.add(Integer.parseInt(ss[6].trim()));
		}
		
		/*         ���浽�ļ�                */
		
		//������Ŀ¼
		File rootFile = new File(dirPath+"/"+username);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }
//        int fileCount = AnalystUtil.fileCount(rootFile)+1;
        Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone����
        t.setToNow(); // ȡ��ϵͳʱ�䡣
        String year = Integer.toString(t.year);
        String month = Integer.toString(t.month);
        String date = Integer.toString(t.monthDay);
        String hour = Integer.toString(t.hour);
        String minute = Integer.toString(t.minute);
        String second = Integer.toString(t.second);
        
        String fileName = username+"_"+year+month+date+hour+minute+second+".txt";
        Log.i("wtw", fileName);
        /*
        //��ȡ��ǰʱ��
        Calendar ca = Calendar.getInstance();
        String year = Integer.toString(ca.get(Calendar.YEAR));//��ȡ���
        String month = Integer.toString(ca.get(Calendar.MONTH));//��ȡ�·�
        String day = Integer.toString(ca.get(Calendar.DATE));//��ȡ��
        String minute = Integer.toString(ca.get(Calendar.MINUTE));//��
        String hour = Integer.toString(ca.get(Calendar.HOUR));//Сʱ
        String second = Integer.toString(ca.get(Calendar.SECOND));//��
        String currentTime = year+month+day+hour+minute+second;
        //д�����ļ�
        String fileName = currentTime+".txt";
        */
        FileOutputStream op;
		try {
			op = new FileOutputStream(rootFile + "/" + fileName);
			PrintWriter pw = new PrintWriter(op);
	        for (int i = 0; i < x.size(); i++) {
	            pw.println(x.get(i) + " " + y.get(i) + " " + time.get(i) + " " + pressure.get(i) + " " +
	            		id.get(i) + " " + tool.get(i) + " " + touch.get(i) + " " );
//	           Log.i("xiugai",(x.get(i) + " " + y.get(i) + " " + time.get(i) + " " + pressure.get(i) + " " +
//	            		id.get(i) + " " +tool.get(i) + " " + touch.get(i) + " " )); 
	           // pw.println(x.get(i) + " " + y.get(i) + " " + time.get(i));
	        }
	        pw.close();
	        op.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
//		return true;
//		------------------------------------------------------------------------------------------><
		boolean checkres = checkFormat.checkData(rootFile + "/" + fileName);
//		boolean checkres = true;
//		------------------------------------------------------------------------------------------><
		File delf = new File(rootFile + "/" + fileName);
		if(!checkres)delf.delete();
		return checkres;
	}
	
	//��֤���ļ����浽����
	public static boolean saveTest(String data, String dirPath) throws IOException{
		// ��ɾ��֮ǰ���ļ�
		String filePath = dirPath+"/"+UserInfo.testDataFileName;
		File f = new File(filePath);
		if(f.exists()){
			f.delete();
		}	
		/*         ��ȡ����                */
		LinkedList<Integer> x = new LinkedList<Integer>();
		LinkedList<Integer> y = new LinkedList<Integer>();
		LinkedList<Long> time = new LinkedList<Long>();
		LinkedList<Float> pressure = new LinkedList<Float>();
		LinkedList<Integer> id = new LinkedList<Integer>();
		LinkedList<Integer> tool = new LinkedList<Integer>();
		LinkedList<Integer> touch = new LinkedList<Integer>();
//		LinkedList<Integer> distance = new LinkedList<Integer>();
//		LinkedList<Integer> toolx = new LinkedList<Integer>();
		
		String[] s = data.split("\n");
		
		for(int i = 0;i < s.length;i++){
			String[] ss = s[i].split(" ");
			x.add(Integer.parseInt(ss[0].trim()));
			y.add(Integer.parseInt(ss[1].trim()));
			time.add(Long.parseLong(ss[2].trim()));
			pressure.add(Float.parseFloat(ss[3].trim()));
			id.add(Integer.parseInt(ss[4].trim()));
			tool.add(Integer.parseInt(ss[5].trim()));
			touch.add(Integer.parseInt(ss[6].trim()));
//			distance.add(Integer.parseInt(ss[5].trim()));
//			toolx.add(Integer.parseInt(ss[6].trim()));
		}
		
		/*         ���浽�ļ�                */
		Log.i("test","savetest");
        FileOutputStream op;
		try {
			op = new FileOutputStream(dirPath+"/"+UserInfo.testDataFileName);
			PrintWriter pw = new PrintWriter(op);
	        for (int i = 0; i < x.size(); i++) {
	            pw.println(x.get(i) + " " + y.get(i) + " " + time.get(i) + " " + pressure.get(i) + " " +
	            		id.get(i) + " " + tool.get(i) + " " + touch.get(i) + " " );
	            
	           // pw.println(x.get(i) + " " + y.get(i) + " " + time.get(i));
	        }
	        pw.close();
	        op.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
//		boolean checkRes = checkFea(dirPath+"/"+UserInfo.testDataFileName);
		Log.i("test","checkData");
		Log.i("test",filePath);
//		------------------------------------------------------------------------------------------><		
		boolean checkRes = checkFormat.checkData(filePath);
//		boolean checkRes = true;
//		------------------------------------------------------------------------------------------><
		File delf = new File(filePath);
		if(!checkRes)delf.delete();
		Log.i("test2",checkRes+"");
		return checkRes;
	}

	//����ļ������Ƿ�ϸ�
//	public static boolean checkFea(String filePath){
//		//��������Ƿ����Ҫ��
//		ArrayList<ArrayList<Double>> fea = extFea.extractFea(filePath);	
//		if(fea==null){
//			System.out.println("����Ϊ��");
//			delFile(filePath);	
//			return false;
//		}
//		else if(fea.size()!=feaCount){
//			System.out.println("����ά������: "+fea.size());
//			delFile(filePath);
//			return false;
//		}
//		else{
//			int yuzhi1 = 3;
//			int yuzhi2 = 3;
//			for(int t = 0;t<feaCount;t++){
//				ArrayList<Double> f = fea.get(t);
//				int size = f.size();
//				
//				if(t>=0&&t<=3){
//					System.out.println("�ٶ�������: "+"ά��- "+t+" ��С- "+size);
//					if(size<yuzhi1){
//						System.out.println("�ٶ�����������: "+"ά��- "+t+" ��С- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=4&&t<=7){
//					System.out.println("���ٶ�������: "+"ά��- "+t+" ��С- "+size);
//					if(size<yuzhi1){
//						System.out.println("���ٶ�����������: "+"ά��- "+t+" ��С- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=9&&t<=12){
//					System.out.println("ѹ��ֵ������: "+"ά��- "+t+" ��С- "+size);
//					if(size<yuzhi1){
//						System.out.println("ѹ��ֵ����������: "+"ά��- "+t+" ��С- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=13&&t<=20){
//					System.out.println("��ָ�����������: "+"ά��- "+t+" ��С- "+size);
//					if(size<yuzhi2){
//						System.out.println("��ָ���������������: "+"ά��- "+t+" ��С- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=21&&t<=28){
//					System.out.println("��ָ��Ƕ�������: "+"ά��- "+t+" ��С- "+size);
//					if(size<yuzhi2){
//						System.out.println("��ָ��Ƕ�����������: "+"ά��- "+t+" ��С- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=29&&t<=36){
//					System.out.println("��ָ��ѹ����������: "+"ά��- "+t+" ��С- "+size);
//					if(size<yuzhi2){
//						System.out.println("��ָ��ѹ��������������: "+"ά��- "+t+" ��С- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//			}
//		}
//		return true;
//	}
	
	//�����ļ�Ŀ¼
	public static boolean upload(String data,String username,String mode) throws IOException{
//		Save.saveAsOutputStreamWriter(data);
//		return true;
		systemDirPath = UserInfo.systemDir;;
		//����Ŀ¼
		File rootFile = new File(systemDirPath);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }
		if(mode.equals("free")){
			//����ģʽ
//			dataDirPath = systemDirPath+"\\data_free_new";
//			extFea = new ExtractFeatureFreeMode();
//			feaCount = 37;//��Ҫ�޸�
		}else{
			//�̶�ģʽ
//			dataDirPath = systemDirPath+"\\data_fixed_new";
//			extFea = new ExtractFeatureFixedMode();
//			feaCount = 23;
		}
		//����Ŀ¼
		File dirFile = new File(dataDirPath);
        if (!dirFile.isDirectory()) {
        	if (!dirFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }		
        return saveTrain(data,username,dataDirPath);
	}
	
	//ɾ���ļ�
	public static void delFile(String file){
		File fi = new File(file);
		if(fi.exists()){
//			fi.delete();
		}
	}
}
