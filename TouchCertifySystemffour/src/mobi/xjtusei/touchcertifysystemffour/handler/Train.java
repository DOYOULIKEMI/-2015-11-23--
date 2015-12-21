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
	protected String feaDirPath;//����������Ŀ¼·��
	protected ArrayList<ArrayList<ArrayList<Double>>> trainFeas;//�洢�����û������������쳣�û��Ƚ�
	protected int distCount;//�����ά��
	protected int count;//�����û���������
	protected ArrayList<Double>[][] dists;//��������û���������
	protected ArrayList<ArrayList<Double>> refFea;//ƽ��ֵ����Ĳο�����ֵ
	protected ExtractFeature extFea;
	protected int feaCount;//������ά��
	protected int[] offIndex;//����ά���̶������������������е�index
	protected boolean isSystem;//��־�Ƿ�����֤ϵͳ
	
	//ѵ�������û�
	public void trainCorrectUsers(){
		//ɾ�������ļ�
		File f = new File(trainDataDirPath+"/train.txt");
		if(f.exists()){
			f.delete();
		}		
				
		File file = new File(trainDataDirPath);
		if(file.isDirectory()){
			//�����Ŀ¼
			for(int i = 0;i < correctUsers.length;i++){
				//����ÿһ���û�������
				File file2 = new File(trainDataDirPath+"/"+correctUsers[i]);
				if(file2.isDirectory()){
					//�����Ŀ¼
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
		
		//�õ��ο����ݼ����±�
		int refIndex = getRefData();
		if(refIndex<0){
			return;
		}
		refFea = (ArrayList<ArrayList<Double>>) trainFeas.get(refIndex);
		UploadData.delFile(trainDataDirPath+"/para.txt");
		AnalystUtil.saveFeature(trainDataDirPath,"para",refFea);

		//�ҳ���ÿ��ѵ���������Ե�ƽ��ֵ����
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
		
		//�ҳ���ÿ��ѵ���������Ե���С����
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
        //�ҳ���ÿ��ѵ���������Ե�ƽ������
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
	
	//ѵ���쳣�û�
	public  void trainWrongUsers(){
		File file = new File(trainDataDirPath);
		if(file.isDirectory()){
			//�����Ŀ¼
			for(int i = 0;i < wrongUsers.length;i++){
				//����ÿһ���û�������
				File file2 = new File(trainDataDirPath+"/"+wrongUsers[i]);
				if(file2.isDirectory()){
					//�����Ŀ¼
					String[] filelist = file2.list();
					int count2 = filelist.length;
					for (int j=0;j<count2;j++){
						//��С����
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
						//ƽ������
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
						
						//ƽ��ֵ����
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

	//����ƽ��ֵ����Ĳο�����
	public int getRefData(){
		
		long startTime=System.currentTimeMillis();
		Log.i("time", "getref:start");
		double minValue = Double.MAX_VALUE;//����֮�͵���Сֵ
		int refDataIndex = 0;//�ο����ݵ��±�
		for(int i = 0;i<count;i++)
        {
			double totalDistance = 0;//�����������ľ���֮��
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
                    	System.out.println("�����û�������"+(i+1)+"������"+(j+1)+"֮��ľ��볬��double���ֵ");
                    	return -1;
                    }
                    totalDistance += d/(count*10000);
                }
            } 
            if(totalDistance<minValue){
            	//��С��ֵ
            	refDataIndex = i;
            	minValue = totalDistance;
            }
        }
		long endTime=System.currentTimeMillis(); 
		Log.i("time", "getref:end"+(endTime-startTime)+"ms");
		return refDataIndex;
	}
	
	
	
	
	
	//����PCA��ά����
	public boolean pca(int correctNum){
		try {
			String url = "http://localhost:8011/PCATrainFile.aspx";
			// ����HTTP Post����
			HttpPost post = new HttpPost(url);
			List <NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("correctNum",Integer.toString(correctNum)));
			// ����HTTP request
			post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		    // ȡ��HTTP response
			HttpResponse  httpResponse=new DefaultHttpClient().execute(post);
			// ��״̬��Ϊ200 ok 
		    if(httpResponse.getStatusLine().getStatusCode()==200){
		    	// ȡ����Ӧ�ִ�
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
