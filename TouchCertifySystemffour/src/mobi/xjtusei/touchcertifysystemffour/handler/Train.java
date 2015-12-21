package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import mobi.xjtusei.touchcertifysystemffour.handler.AnalystUtil;

public class Train {
	protected String[] correctUsers;
	protected String[] wrongUsers;
	protected String systemDirPath;
	protected String trainDataDirPath;
	protected String feaDirPath;//保存特征的目录路径
	protected ArrayList<ArrayList<ArrayList<Double>>> trainFeas;//存储正常用户的特征便于异常用户比较
	protected int distCount;//距离的维数
	protected int count;//正常用户的样本数
	protected ArrayList<Double>[][] dists;//存放正常用户样本距离
	protected ArrayList<ArrayList<Double>> refFea;//平均值距离的参考特征值
	protected ExtractFeature extFea;
	protected int feaCount;//特征的维数
	protected int[] offIndex;//特征维数固定的特征在特征向量中的index
	protected boolean isSystem;//标志是否是认证系统
	
	//训练正常用户
	public void trainCorrectUsers(){
		//删除特征文件
		File f = new File(trainDataDirPath+"/train.txt");
		if(f.exists()){
			f.delete();
		}		
				
		File file = new File(trainDataDirPath);
		if(file.isDirectory()){
			//如果是目录
			for(int i = 0;i < correctUsers.length;i++){
				//遍历每一个用户的数据
				File file2 = new File(trainDataDirPath+"/"+correctUsers[i]);
				if(file2.isDirectory()){
					//如果是目录
					String[] filelist = file2.list();
					count = filelist.length;
					dists = new ArrayList[count][count];
					for(int t = 0;t<count;t++){
						String filepath = trainDataDirPath+"/"+correctUsers[i]+"/"+filelist[t];
						ArrayList<ArrayList<Double>> fea = extFea.extractFea(filepath);						
						if(fea==null){
							System.out.println("feature of "+filepath+" is null");
							return;
						}
						if(fea.size()!=feaCount){
							System.out.println("count of the features of "+filepath+" is wrong");
							return;
						}
						AnalystUtil.saveFeature(feaDirPath,correctUsers[i],fea);
			            trainFeas.add(fea);			             
					}
					long startTime1=System.currentTimeMillis(); 
					Log.i("time", "caldist:start"+startTime1+"ms");
					for (int j=0;j<count-1;j++)
			        {
		               ArrayList<ArrayList<Double>> a = (ArrayList<ArrayList<Double>>) trainFeas.get(j);
		               for(int jj = j+1;jj<count;jj++)
		               { 
		            	   ArrayList<ArrayList<Double>> b = (ArrayList<ArrayList<Double>>) trainFeas.get(jj);
		            	   try{
		            		   ArrayList<Double> dist = Distance.distance(a, b, offIndex);
		            		   dists[j][jj] = dist;
		            	   }catch(Exception e){
		            		   System.out.println("the distance between "+j+" and "+jj+" is wrong");
		            	   }
		            	   
		               }
			        }
					long endTime1=System.currentTimeMillis(); 
					Log.i("time", "caldist:end"+(endTime1-startTime1)+"ms");
				}
			}
		}
		
		//得到参考数据及其下标
		int refIndex = getRefData();
		if(refIndex<0){
			return;
		}
		refFea = (ArrayList<ArrayList<Double>>) trainFeas.get(refIndex);
		UploadData.delFile(trainDataDirPath+"/para.txt");
		AnalystUtil.saveFeature(trainDataDirPath,"para",refFea);

		//找出对每个训练样本而言的平均值距离
		for(int i = 0;i<count;i++){
			ArrayList<Double> d = new ArrayList<Double>();
			if(i==refIndex){
//				for(int j=0;j<dists[0][1].size();j++){
//					d.add(0.0);
//				}
				ArrayList<ArrayList<Double>> obj = (ArrayList<ArrayList<Double>>) trainFeas.get(i);
				d = Distance.distance(obj, obj, offIndex);
			}
			else{
				d = dists[i][refIndex]==null?dists[refIndex][i]:dists[i][refIndex];				
			}
			AnalystUtil.writeDistToFile(d,trainDataDirPath,"train.txt",true,true,this.isSystem);
		}
		
		//找出对每个训练样本而言的最小距离
		/*
        for(int i = 0;i<count;i++)
        {
            double t = Double.MAX_VALUE;
            int min = 0;
            for(int j = 0;j<count;j++)
            {
                if(i!=j)
                {
                    min = j;
                    if(dists[i][j]==null)
                    {
                    	dists[i][j] = dists[j][i];
                    }
                    double d = Distance.euclidean(dists[i][j]);
                    if (d < t)
                    {
                        t = d;
                        min = j;
                    }
                }
            }
            AnalystUtil.writeDistToFile(dists[i][min],"D:\\feaData",true);
            
        }
        */
		
		/*
        //找出对每个训练样本而言的平均距离
        for(int i = 0;i<count;i++)
        {
            ArrayList<Double> d = new  ArrayList<Double>();
            for(int t = 0;t<distCount;t++){
            	d.add(0.0);
            }
            for(int j = 0;j<count;j++)
            {
                if(i!=j)
                {
                    if(dists[i][j]==null)
                    {
                    	dists[i][j] = dists[j][i];
                    }
                    for(int t1 = 0;t1<dists[i][j].size();t1++){
                    	double oldValue = d.get(t1);
                    	double changeValule = (double)dists[i][j].get(t1);
                    	d.set(t1, oldValue+changeValule);
                    }
                    
                }
            }
            for(int t = 0;t<distCount;t++){
            	double temp = d.get(t)/(count-1);
            	d.set(t,temp);
            }
            AnalystUtil.writeDistToFile(d,"D:\\trainData","train.txt",true,true);           
        }
        */
	}
	
	//训练异常用户
	public  void trainWrongUsers(){
		File file = new File(trainDataDirPath);
		if(file.isDirectory()){
			//如果是目录
			for(int i = 0;i < wrongUsers.length;i++){
				//遍历每一个用户的数据
				File file2 = new File(trainDataDirPath+"/"+wrongUsers[i]);
				if(file2.isDirectory()){
					//如果是目录
					String[] filelist = file2.list();
					int count2 = filelist.length;
					for (int j=0;j<count2;j++){
						//最小距离
						/*
						ArrayList<ArrayList<Double>> a = ExtractFeature.extractFea(dirPath+"\\"+correctUsers[i]+"\\"+filelist[j]);
						double minimalValue = Double.MAX_VALUE;
						ArrayList<Double> minimalDis = null;
						for(int t = 0;t<trainFeas.size();t++){
							ArrayList<ArrayList<Double>> b = (ArrayList<ArrayList<Double>>) trainFeas.get(t);
							ArrayList<Double> dis = Distance.distance(a, b);
							double d = Distance.euclidean(dis);
							if(d<minimalValue){
								minimalValue = d;
								minimalDis = dis;
							}
						}
						AnalystUtil.writeDistToFile(minimalDis,"D:\\feaData",false);
						*/
						
						/*
						//平均距离
						ArrayList<ArrayList<Double>> a = ExtractFeature.extractFea(dirPath+"\\"+wrongUsers[i]+"\\"+filelist[j]);
						AnalystUtil.saveFeature("D:\\featureData",wrongUsers[i],a);
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
			            	d.set(t,  d.get(t)/count);
			            }
						AnalystUtil.writeDistToFile(d,"D:\\trainData","train.txt",true,false);
						*/
						
						//平均值距离
						String filepath = trainDataDirPath+"/"+wrongUsers[i]+"/"+filelist[j];
						ArrayList<ArrayList<Double>> a = extFea.extractFea(filepath);
						if(a==null){
							System.out.println("feature of "+filepath+" is null");
							return;
						}
						AnalystUtil.saveFeature(feaDirPath,wrongUsers[i],a);
						ArrayList<Double> d = Distance.distance(a, refFea, offIndex);
						AnalystUtil.writeDistToFile(d,trainDataDirPath,"train.txt",true,false,this.isSystem);
					}
				}
			}
		}
	}

	//计算平均值距离的参考数据
	public int getRefData(){
		
		long startTime=System.currentTimeMillis();
		Log.i("time", "getref:start");
		double minValue = Double.MAX_VALUE;//距离之和的最小值
		int refDataIndex = 0;//参考数据的下标
		for(int i = 0;i<count;i++)
        {
			double totalDistance = 0;//和其他样本的距离之和
            for(int j = 0;j<count;j++)
            {
                if(i!=j)
                {
                    if(dists[i][j]==null)
                    {
                    	dists[i][j] = dists[j][i];
                    }
                    double d = Distance.euclidean(dists[i][j]);
                    if(d>=Double.MAX_VALUE){
                    	System.out.println("正常用户的特征"+(i+1)+"和特征"+(j+1)+"之间的距离超过double最大值");
                    	return -1;
                    }
                    totalDistance += d/(count*10000);
                }
            } 
            if(totalDistance<minValue){
            	//更小的值
            	refDataIndex = i;
            	minValue = totalDistance;
            }
        }
		long endTime=System.currentTimeMillis(); 
		Log.i("time", "getref:end"+(endTime-startTime)+"ms");
		return refDataIndex;
	}
	
	
	
	
	
	//进行PCA降维处理
	public boolean pca(int correctNum){
		try {
			String url = "http://localhost:8011/PCATrainFile.aspx";
			// 建立HTTP Post连线
			HttpPost post = new HttpPost(url);
			List <NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("correctNum",Integer.toString(correctNum)));
			// 发出HTTP request
			post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
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
