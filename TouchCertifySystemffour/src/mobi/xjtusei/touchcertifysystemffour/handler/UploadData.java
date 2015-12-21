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
	
	//训练的文件保存到本地
	public static boolean saveTrain(String data, String username, String dirPath) throws IOException{
		/*         获取数据                */
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
		
		/*         保存到文件                */
		
		//创建新目录
		File rootFile = new File(dirPath+"/"+username);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }
//        int fileCount = AnalystUtil.fileCount(rootFile)+1;
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        String year = Integer.toString(t.year);
        String month = Integer.toString(t.month);
        String date = Integer.toString(t.monthDay);
        String hour = Integer.toString(t.hour);
        String minute = Integer.toString(t.minute);
        String second = Integer.toString(t.second);
        
        String fileName = username+"_"+year+month+date+hour+minute+second+".txt";
        Log.i("wtw", fileName);
        /*
        //获取当前时间
        Calendar ca = Calendar.getInstance();
        String year = Integer.toString(ca.get(Calendar.YEAR));//获取年份
        String month = Integer.toString(ca.get(Calendar.MONTH));//获取月份
        String day = Integer.toString(ca.get(Calendar.DATE));//获取日
        String minute = Integer.toString(ca.get(Calendar.MINUTE));//分
        String hour = Integer.toString(ca.get(Calendar.HOUR));//小时
        String second = Integer.toString(ca.get(Calendar.SECOND));//秒
        String currentTime = year+month+day+hour+minute+second;
        //写入新文件
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
	
	//认证的文件保存到本地
	public static boolean saveTest(String data, String dirPath) throws IOException{
		// 先删除之前的文件
		String filePath = dirPath+"/"+UserInfo.testDataFileName;
		File f = new File(filePath);
		if(f.exists()){
			f.delete();
		}	
		/*         获取数据                */
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
		
		/*         保存到文件                */
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

	//检测文件特征是否合格
//	public static boolean checkFea(String filePath){
//		//检测特征是否符合要求
//		ArrayList<ArrayList<Double>> fea = extFea.extractFea(filePath);	
//		if(fea==null){
//			System.out.println("特征为空");
//			delFile(filePath);	
//			return false;
//		}
//		else if(fea.size()!=feaCount){
//			System.out.println("特征维数错误: "+fea.size());
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
//					System.out.println("速度特征量: "+"维度- "+t+" 大小- "+size);
//					if(size<yuzhi1){
//						System.out.println("速度特征量不够: "+"维度- "+t+" 大小- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=4&&t<=7){
//					System.out.println("加速度特征量: "+"维度- "+t+" 大小- "+size);
//					if(size<yuzhi1){
//						System.out.println("加速度特征量不够: "+"维度- "+t+" 大小- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=9&&t<=12){
//					System.out.println("压力值特征量: "+"维度- "+t+" 大小- "+size);
//					if(size<yuzhi1){
//						System.out.println("压力值特征量不够: "+"维度- "+t+" 大小- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=13&&t<=20){
//					System.out.println("两指间距离特征量: "+"维度- "+t+" 大小- "+size);
//					if(size<yuzhi2){
//						System.out.println("两指间距离特征量不够: "+"维度- "+t+" 大小- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=21&&t<=28){
//					System.out.println("两指间角度特征量: "+"维度- "+t+" 大小- "+size);
//					if(size<yuzhi2){
//						System.out.println("两指间角度特征量不够: "+"维度- "+t+" 大小- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//				if(t>=29&&t<=36){
//					System.out.println("两指间压力差特征量: "+"维度- "+t+" 大小- "+size);
//					if(size<yuzhi2){
//						System.out.println("两指间压力差特征量不够: "+"维度- "+t+" 大小- "+size);
//						delFile(filePath);
//						return false;
//					}
//				}
//			}
//		}
//		return true;
//	}
	
	//创建文件目录
	public static boolean upload(String data,String username,String mode) throws IOException{
//		Save.saveAsOutputStreamWriter(data);
//		return true;
		systemDirPath = UserInfo.systemDir;;
		//创建目录
		File rootFile = new File(systemDirPath);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }
		if(mode.equals("free")){
			//自由模式
//			dataDirPath = systemDirPath+"\\data_free_new";
//			extFea = new ExtractFeatureFreeMode();
//			feaCount = 37;//需要修改
		}else{
			//固定模式
//			dataDirPath = systemDirPath+"\\data_fixed_new";
//			extFea = new ExtractFeatureFixedMode();
//			feaCount = 23;
		}
		//创建目录
		File dirFile = new File(dataDirPath);
        if (!dirFile.isDirectory()) {
        	if (!dirFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }		
        return saveTrain(data,username,dataDirPath);
	}
	
	//删除文件
	public static void delFile(String file){
		File fi = new File(file);
		if(fi.exists()){
//			fi.delete();
		}
	}
}
