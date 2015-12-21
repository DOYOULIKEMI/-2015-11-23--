package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AnalystUtil {
	
	
	//将距离特征写入文件
	public static void writeDistToFile(ArrayList<Double> dist,String dirPath,String fileName,boolean isTrainedDist,boolean isCorrectUser,boolean isSystem){
		FileWriter writer;
		File file = new File(dirPath);
        if (!file.isDirectory())
        {
            file.mkdir();
        }
        String filePath = dirPath + "/"+fileName;
        file = new File(filePath);
                
        try {
        	 if (file.exists()) //如果文件存在,则创建AppendText对象
             {
             	writer = new  FileWriter(filePath,true);
             }
             else   //如果文件不存在,则创建File.CreateText对象
             {
             	writer = new  FileWriter(filePath,false);
             }
        	 if(isTrainedDist&&isCorrectUser){
        		 writer.write("+1" + " ");
        	 }else if(isTrainedDist&&(!isCorrectUser)){
        		 writer.write("-1" + " ");
        	 }else if(!isTrainedDist&&isSystem){
        		 writer.write("+1" + " ");
        	 }
        	 if(!isSystem){
        		 for (int i = 0; i < dist.size(); i++)
                 {
                     double temp = (double)dist.get(i);
                     writer.write(temp + " ");
                 }
        	 }else{
        		 // 认证系统的数据处理
        		 for (int i = 0; i < dist.size(); i++)
                 {
        			 writer.write((i+1)+":");
                     double temp = (double)dist.get(i);
                     writer.write(temp + " ");
                 }
        	 }
             
        	writer.write("\r\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取文件夹下文件个数
	public static int fileCount(File folder){
		int count = 0;
		if(folder.isDirectory()){
			File list[] = folder.listFiles();
			for(int i = 0; i < list.length; i++){
			    if(list[i].isFile()){
			    	count++;
			    }
			}
		}
		return count;
	}
	//计算方差
	public static double variance(ArrayList<Double> data){
		//double res = 0;
		double sum = 0;
		int num = data.size();
		for (int i=0;i<num;i++){
			sum+=data.get(i);
		}
		double average = sum/num;
		double temp=0;
		for(int i=0;i<num;i++)
		{
			temp+=Math.pow((data.get(i)-average), 2);
		}
		temp = temp/(num-1);
		//res =  Math.sqrt(temp);
		return temp;
	}
	//坐标变换
	public static double[] axisrotate(double x, double y)
    {
        double[] res = new double[2];
        final double h = 0.70710678118655;
        res[1] = h * (x + y);
        res[0] = h * (x - y);
        return res;
    }
	
	//特征保存到文件中
	public static boolean saveFeature(String fileRootPath,String username,ArrayList<ArrayList<Double>> feature){		
		File rootFile = new File(fileRootPath);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }
        rootFile = new File(fileRootPath);
        if (!rootFile.isDirectory()) {
        	if (!rootFile.mkdir()) {
                throw new RuntimeException("Cannot Create Directory");
            }
        }
        //int fileCount = AnalystUtil.fileCount(rootFile)+1;
        String fileName = username+".txt";
        //写入新文件
        FileOutputStream op;
		try {
			op = new FileOutputStream(rootFile + "/" + fileName);
			PrintWriter pw = new PrintWriter(op);
			for(int i = 0;i<feature.size();i++){
				ArrayList<Double> feaSeg  = feature.get(i);
				for(int j = 0;j<feaSeg.size();j++){
					pw.print(feaSeg.get(j) + " ");
				}
				pw.println();
				//pw.println("the end of one feature");
			}
//			pw.println("速度：");
//	        for (int i = 0; i < rate4s.size(); i++) {
//	        	ArrayList<Double> r = rate4s.get(i);
//	        	for(int j = 0;j<r.size();j++){
//	        		pw.print(r.get(j) + " ");
//	        	}
//	        	pw.println();
//	        }
//	        pw.println("加速度：");
//	        for (int i = 0; i < acce4s.size(); i++) {
//	        	ArrayList<Double> a = acce4s.get(i);
//	        	for(int j = 0;j<a.size();j++){
//	        		pw.print(a.get(j) + " ");
//	        	}
//	        	pw.println();
//	        }
//	        pw.println("偏移量：");
//	        for(int t = 0;t<offset4s.size();t++){
//	        	pw.print(offset4s.get(t) + " ");
//	        }
//	        pw.println();
//	        pw.println("两点间距离：");
//	        for (int i = 0; i < instance4m.size(); i++) {
//	        	ArrayList<Double> a = instance4m.get(i);
//	        	for(int j = 0;j<a.size();j++){
//	        		pw.print(a.get(j) + " ");
//	        	}
//	        	pw.println();
//	        }
//	        pw.println();
//	        pw.println("两点间角度：");
//	        for (int i = 0; i < degree4m.size(); i++) {
//	        	ArrayList<Double> a = degree4m.get(i);
//	        	for(int j = 0;j<a.size();j++){
//	        		pw.print(a.get(j) + " ");
//	        	}
//	        	pw.println();
//	        }
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
        return true;
	}
	
	/**
	 * 第一种边界条件的三次样条函数插值、微商与积分
	 * 
	 * @param n - 结点的个数
	 * @param x - 一维数组，长度为n，存放给定的n个结点的值x(i)
	 * @param y - 一维数组，长度为n，存放给定的n个结点的函数值y(i)，
	 *            y(i) = f(x(i)), i=0,1,...,n-1
	 * @param dy - 一维数组，长度为n，调用时，dy(0)存放给定区间的左端点处的一阶导数值，
	 *             dy(n-1)存放给定区间的右端点处的一阶导数值。返回时，存放n个给定点处的
	 *             一阶导数值y'(i)，i=0,1,...,n-1
	 * @param ddy - 一维数组，长度为n，返回时，存放n个给定点处的二阶导数值y''(i)，
	 *              i=0,1,...,n-1
	 * @param m - 指定插值点的个数
	 * @param t - 一维数组，长度为m，存放m个指定的插值点的值。
	 * @param z - 一维数组，长度为m，存放m个指定的插值点处的函数值
	 * @param dz - 一维数组，长度为m，存放m个指定的插值点处的一阶导数值
	 * @param ddz - 一维数组，长度为m，存放m个指定的插值点处的二阶导数值
	 * @return double 型，指定函数的x(0)到x(n-1)的定积分值
	 */
	public static double GetValueSpline1(int n, double[] x, double[] y, double[] dy, double[] ddy, 
		int m, double[] t, double[] z, double[] dz, double[] ddz)
	{ 
		int i,j;
		double h0,h1,alpha,beta,g;
    
		// 初值
		double[] s=new double[n];
		s[0]=dy[0]; 
		dy[0]=0.0;
		h0=x[1]-x[0];
    
		for (j=1;j<=n-2;j++)
		{ 
			h1=x[j+1]-x[j];
			alpha=h0/(h0+h1);
			beta=(1.0-alpha)*(y[j]-y[j-1])/h0;
			beta=3.0*(beta+alpha*(y[j+1]-y[j])/h1);
			dy[j]=-alpha/(2.0+(1.0-alpha)*dy[j-1]);
			s[j]=(beta-(1.0-alpha)*s[j-1]);
			s[j]=s[j]/(2.0+(1.0-alpha)*dy[j-1]);
			h0=h1;
		}
    
		for (j=n-2;j>=0;j--)
			dy[j]=dy[j]*dy[j+1]+s[j];
    
		for (j=0;j<=n-2;j++) 
			s[j]=x[j+1]-x[j];
    
		for (j=0;j<=n-2;j++)
		{ 
			h1=s[j]*s[j];
			ddy[j]=6.0*(y[j+1]-y[j])/h1-2.0*(2.0*dy[j]+dy[j+1])/s[j];
		}
    
		h1=s[n-2]*s[n-2];
		ddy[n-1]=6.0*(y[n-2]-y[n-1])/h1+2.0*(2.0*dy[n-1]+dy[n-2])/s[n-2];
		g=0.0;
    
		for (i=0;i<=n-2;i++)
		{ 
			h1=0.5*s[i]*(y[i]+y[i+1]);
			h1=h1-s[i]*s[i]*s[i]*(ddy[i]+ddy[i+1])/24.0;
			g=g+h1;
		}
    
		for (j=0;j<=m-1;j++)
		{ 
			if (t[j]>=x[n-1]) 
				i=n-2;
			else
			{ 
				i=0;
				while (t[j]>x[i+1]) 
					i=i+1;
			}
        
			h1=(x[i+1]-t[j])/s[i];
			h0=h1*h1;
			z[j]=(3.0*h0-2.0*h0*h1)*y[i];
			z[j]=z[j]+s[i]*(h0-h0*h1)*dy[i];
			dz[j]=6.0*(h0-h1)*y[i]/s[i];
			dz[j]=dz[j]+(3.0*h0-2.0*h1)*dy[i];
			ddz[j]=(6.0-12.0*h1)*y[i]/(s[i]*s[i]);
			ddz[j]=ddz[j]+(2.0-6.0*h1)*dy[i]/s[i];
			h1=(t[j]-x[i])/s[i];
			h0=h1*h1;
			z[j]=z[j]+(3.0*h0-2.0*h0*h1)*y[i+1];
			z[j]=z[j]-s[i]*(h0-h0*h1)*dy[i+1];
			dz[j]=dz[j]-6.0*(h0-h1)*y[i+1]/s[i];
			dz[j]=dz[j]+(3.0*h0-2.0*h1)*dy[i+1];
			ddz[j]=ddz[j]+(6.0-12.0*h1)*y[i+1]/(s[i]*s[i]);
			ddz[j]=ddz[j]-(2.0-6.0*h1)*dy[i+1]/s[i];
		}
    
		return(g);
	}
	
	/// <summary>
    /// 滤波
    /// </summary>
    /// <param name="x"></param>
    public static ArrayList<Double> MAfilter(ArrayList<Double> x)
    {
    	if(x.size()<4){
    		return null;
    	}
        double temp = 0;
        ArrayList<Double> y = new ArrayList<Double>(x.size());
        temp=((double)x.get(0) + (double)x.get(1) + (double)x.get(2)) / 3;
        y.add(temp);
        temp = ((double)x.get(0) + (double)x.get(1) + (double)x.get(2)+ (double)x.get(3)) / 4;
        y.add(temp);
        int i = 2;
        for (i = 2; i < x.size() - 2; ++i)
        {
            temp=((double)x.get(i-2) + (double)x.get(i-1) + (double)x.get(i) + (double)x.get(i+1) + (double)x.get(i+2)) / 5;
            y.add(temp);
        }
        temp= ((double)x.get(i-2) + (double)x.get(i-1) + (double)x.get(i) + (double)x.get(i+1)) / 4;
        y.add(temp);
        ++i;
        temp=((double)x.get(i-2) + (double)x.get(i-1) + (double)x.get(i)) / 3;
        y.add(temp);
        return y;
    }
}
