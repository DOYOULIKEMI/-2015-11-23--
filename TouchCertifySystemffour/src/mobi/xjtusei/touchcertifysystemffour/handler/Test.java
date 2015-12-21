package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Test {
	protected String systemDirPath;
	protected String testDataDirPath;
	protected String trainDataDirPath;
	protected String[] correctTrainedUsers;//正常的训练用户名
	protected String[] correctTestUsers;//正常的测试用户名
	protected String[] wrongTestUsers;//异常的测试用户名
	protected ArrayList<ArrayList<ArrayList<Double>>> trainFeas;//存储正常用户的特征便于测试用户比较
	protected int distCount;//距离的维数
	protected int trainedCount;//正常的训练样本数
	protected int refIndex;//平均值距离的参考数据下标
	protected ExtractFeature extFea;
	protected int[] offIndex;//偏移量特征在特征向量中的index
	protected boolean isSystem;//标志是否是认证系统
	ArrayList<ArrayList<Double>> paraFea = new ArrayList<ArrayList<Double>>();
	
	public static double testDTW(){
		ArrayList<Double> a = new ArrayList<Double>();
		ArrayList<Double> b = new ArrayList<Double>();
		a.add(1.0);
		a.add(1.0);
		a.add(1.0);
		a.add(10.0);
		a.add(2.0);
		a.add(3.0);
		b.add(1.0);
		b.add(1.0);
		b.add(1.0);
		b.add(2.0);
		b.add(10.0);
		b.add(3.0);
		double res= Distance.dtw(a, b);
		return res;
	}
	public static double[][] testAxis(){
		double[][] res = new double[10][2];
		double[] d = AnalystUtil.axisrotate(299,25);
		res[0][0] = d[0];
		res[0][1] = d[1];
		d = AnalystUtil.axisrotate(299,28);
		res[1][0] = d[0];
		res[1][1] = d[1];
		d = AnalystUtil.axisrotate(297,33);
		res[2][0] = d[0];
		res[2][1] = d[1];
		d = AnalystUtil.axisrotate(294,40);
		res[3][0] = d[0];
		res[3][1] = d[1];
		d = AnalystUtil.axisrotate(289,48);
		res[4][0] = d[0];
		res[4][1] = d[1];
		d = AnalystUtil.axisrotate(283,56);
		res[5][0] = d[0];
		res[5][1] = d[1];
		d = AnalystUtil.axisrotate(277,65);
		res[6][0] = d[0];
		res[6][1] = d[1];
		d = AnalystUtil.axisrotate(270,74);
		res[7][0] = d[0];
		res[7][1] = d[1];
		d = AnalystUtil.axisrotate(263,85);
		res[8][0] = d[0];
		res[8][1] = d[1];
		d = AnalystUtil.axisrotate(254,97);
		res[9][0] = d[0];
		res[9][1] = d[1];
		return res;
	}
	//计算正常用户训练数据的特征
	/*
	public void getCorrectTrainedFea(){
		File dir = new File(trainDataDirPath);
		if(dir.isDirectory()){
			//如果是目录
			for(int d = 0;d<correctTrainedUsers.length;d++){
				dir = new File(trainDataDirPath+"/"+correctTrainedUsers[d]);
				if(dir.isDirectory()){
					String[] filelist = dir.list();
					trainedCount += filelist.length;
					for(int i = 0;i<filelist.length;i++){
						ArrayList<ArrayList<Double>> fea = extFea.extractFea(trainDataDirPath+"/"+correctTrainedUsers[d]+"/"+filelist[i]);
			            trainFeas.add(fea);
					}
				}
			}
		}
	}        */
	
	//提取测试用户的特征距离并存入文件
	public void test() throws Exception{		
		//删除之前的测试文件
		String filePath = testDataDirPath+"/"+"test.txt";
		File f = new File(filePath);
		if(f.exists()){
			f.delete();
		}
		
		String fileName = testDataDirPath+"/"+"testData.txt";
		ArrayList<ArrayList<Double>> fea = extFea.extractFea(fileName);
		calDist(fea);
		
		/*
		//计算正常用户训练数据的特征
		getCorrectTrainedFea();
		
		//获取平均值距离的参考数据下标
		refIndex = getRefData();
		if(refIndex<0){
			return;
		}
		
		File file = new File(testDataDirPath);
		if(file.isDirectory()){
			//如果是目录
			//处理正常用户
			for(int i = 0;i<correctTestUsers.length;i++){
				File file2 = new File(testDataDirPath+"/"+correctTestUsers[i]);
				if(file2.isDirectory()){
					String[] filelist = file2.list();
					int count = filelist.length;
					for(int j = 0;j<count;j++){
						String fileName = testDataDirPath+"/"+correctTestUsers[i]+"/"+filelist[j];
						ArrayList<ArrayList<Double>> fea = extFea.extractFea(fileName);
						calDist(fea);
					}
				}				
			}
			//处理异常用户
			for(int i = 0;i<wrongTestUsers.length;i++){
				File file2 = new File(testDataDirPath+"/"+wrongTestUsers[i]);
				if(file2.isDirectory()){
					String[] filelist = file2.list();
					int count = filelist.length;
					for(int j = 0;j<count;j++){
						String fileName = testDataDirPath+"/"+wrongTestUsers[i]+"/"+filelist[j];
						ArrayList<ArrayList<Double>> fea = extFea.extractFea(fileName);
						calDist(fea);
					}
				}				
			}
		}       */
	}
	
	public ArrayList<ArrayList<Double>> getFeafromtxt() throws Exception{
		FileInputStream fin = new FileInputStream(trainDataDirPath+"/"+"para.txt");
		InputStreamReader inR = new InputStreamReader(fin);
		BufferedReader bfR  = new BufferedReader(inR);
		
		ArrayList<Double> partFea = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> tempFea = new ArrayList<ArrayList<Double>>();
		String temp1 = "";
		String[] temp2 = {};		
		while((temp1=bfR.readLine())!=null){
			temp2=temp1.split(" ");
			for (int i=0;i<temp2.length;i++)
			{
				partFea.add(Double.parseDouble(temp2[i]));
			}
			tempFea.add(partFea);
			partFea = new ArrayList<Double>();			
		}
		bfR.close();
		return(tempFea);
	}

	
	//计算距离并写入文件
	public void calDist(ArrayList<ArrayList<Double>> a) throws Exception{
		/*
		//平均距离
		ArrayList<Double> d = new  ArrayList<Double>();
        for(int t = 0;t<distCount;t++){
        	d.add(0.0);
        }
		for(int t = 0;t<trainFeas.size();t++){
			ArrayList<ArrayList<Double>> b = (ArrayList<ArrayList<Double>>) trainFeas.get(t);
			ArrayList<Double> dis = Distance.distance(a, b);
			for(int t1 = 0;t1<dis.size();t1++){
            	double oldValue = d.get(t1);
            	double changeValule = (double)dis.get(t1);
            	d.set(t1, oldValue+changeValule);
            }							
		}
		for(int t = 0;t<distCount;t++){
        	d.set(t,  d.get(t)/trainedCount);
        }
		AnalystUtil.writeDistToFile(d,testDirPath,"test.txt",false,true);
		*/
		
		//平均值距离
		paraFea = getFeafromtxt();
		ArrayList<Double> d = Distance.distance(a, paraFea,offIndex);
		AnalystUtil.writeDistToFile(d,testDataDirPath,"test.txt",false,true,this.isSystem);
	}
	
	/*
	//计算平均值距离的参考数据下标
	public int getRefData(){
		double minValue = Double.MAX_VALUE;//距离之和的最小值
		int refDataIndex = 0;//参考数据的下标
		for(int i = 0;i<trainedCount;i++)
        {
			double totalDistance = 0;//和其他样本的距离之和
            for(int j = 0;j<trainedCount;j++)
            {
                if(i!=j)
                {
                	ArrayList<Double> dis = Distance.distance(
                			(ArrayList<ArrayList<Double>>)trainFeas.get(i), (ArrayList<ArrayList<Double>>)trainFeas.get(j),offIndex);
                    double d = Distance.euclidean(dis);
                    totalDistance += d;
                }
            } 
            if(totalDistance<minValue){
            	//更小的值
            	refDataIndex = i;
            	minValue = totalDistance;
            }
        }
		return refDataIndex;
	}                 */
	
	
	//进行PCA降维处理
	public boolean pca(){
		try {
			String url = "http://localhost:8011/PCATestFile.aspx";
			//String url = "http://localhost:3507/MultiTouchServer2/PCATestFile.aspx";
			// 建立HTTP Post连线
			HttpPost post = new HttpPost(url);
			//List <NameValuePair> params=new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair("correctNum",Integer.toString(correctNum)));
			// 发出HTTP request
			//post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		    // 取得HTTP response
			HttpResponse  httpResponse=new DefaultHttpClient().execute(post);
			// 若状态码为200 ok 
		    if(httpResponse.getStatusLine().getStatusCode()==200){
		    	// 取出回应字串
		    	//String strResult=
		    		EntityUtils.toString(httpResponse.getEntity());
		    }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}  		
		return true;
	}
}
