package mobi.xjtusei.touchcertifysystemffour.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ExtractFeature {
	String fileRootPath;//���������ļ���Ŀ¼
	int count;//�����ܶ���
	int[] count4singTouch;//���㴥���Ķ���
	int[] count4multiTouch;//��㴥���Ķ���
	int[] axisrArray;//��Ҫ����任�Ķ�	
		
	/*
	 *  ��ʼ�� ��ȡ����
	 */
	public ArrayList<ArrayList<DataType>> init(String fileName){
		ArrayList<ArrayList<DataType>> data = new ArrayList<ArrayList<DataType>>();
		ArrayList<DataType> dataSeg= new ArrayList<DataType>();
		
		// ��ȡԭʼ�ļ�
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ����һ��
            	String[] ss = tempString.split(" ");
//            	float press = Float.parseFloat(ss[3].trim());
//            	if(press==0&&!ss[0].equals("-1")){
//            		//���ѹ��Ϊ0���Ҳ��Ƿֶα�־ ��ʾ�õ��ʾ���� Ӧȥ���õ�
//            		continue;
//            	}
            	if(ss[0].trim().equals("-1")){
    				//�µ�һ���ƶ���ʼ
    				data.add(dataSeg);
    				dataSeg = new ArrayList<DataType>();
    				continue;
    			}
    			DataType d = new DataType(
		    					Double.parseDouble(ss[0].trim()),
		    					Double.parseDouble(ss[1].trim()),
		    					Long.parseLong(ss[2].trim()),
    							Float.parseFloat(ss[3].trim()),
    							Integer.parseInt(ss[4].trim())
    							//Integer.parseInt(ss[5].trim()),
    							//Integer.parseInt(ss[6].trim())
    							);
    			dataSeg.add(d);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                	} catch (IOException e1) {
                }
            }
        }
        return data;
	}
	
	/*
	 *  ��ȡ����
	 */
	public ArrayList<ArrayList<Double>> extractFea(String fileName){
		return null;
	}
	
	/*
	 *  ��ʾdata
	 */
	public static void display(ArrayList<ArrayList<DataType>> data){
		for(int i =2;i<4;i++){
			System.out.println("��"+i+"�Σ�"+"\n");
			ArrayList<DataType> d = data.get(i);
			for(int j = 0;j<d.size();j++){
				DataType t = d.get(j);
				System.out.print(t.x+" "+t.y+"\n");
			}
		}
	}
	/*
	 *  ����任
	 */
	public void axisrot(ArrayList<ArrayList<DataType>> data){
		for(int i = 0;i<axisrArray.length;i++){
			ArrayList<DataType> d = data.get(axisrArray[i]);
			for(int j=0;j<d.size();j++){
				DataType dd = d.get(j);
				double x = dd.getX();
				double y = dd.getY();
				double[] res = AnalystUtil.axisrotate(dd.getX(), dd.getY());
				dd.setX(res[0]);
				dd.setY(res[1]);				
			}
		}
	}
	/*
	 *  ��ȡ���㴥��������
	 */
	public boolean feature4singTouch(List<ArrayList<DataType>> list,ArrayList<ArrayList<Double>> fea){
		if(list.size()!=count4singTouch.length)
			return false;		
		ArrayList<DataType> dataSeg;
		ArrayList<Double> rateSeg;//ÿһ�ε��ٶ�
		//ArrayList<Double> acceSeg;//ÿһ�εļ��ٶ�
		ArrayList<Double> pressSeg;//ÿһ�ε�ѹ��ֵ
		
		double off = 0;
		double pressvari = 0;//ѹ���ı�׼��
		ArrayList<ArrayList<Double>> rate4s = new ArrayList<ArrayList<Double>>();//���浥�㴥�����ٶ�ֵ
		//ArrayList<ArrayList<Double>> acce4s = new ArrayList<ArrayList<Double>>();//���浥�㴥���ļ��ٶ�ֵ
		ArrayList<Double> offset4s = new ArrayList<Double>();//���浥�㴥����ƫ����
		ArrayList<Double> press4s = new ArrayList<Double>();//���浥�㴥����ѹ��ֵ��׼��
		//ArrayList<ArrayList<Double>> press4s = new ArrayList<ArrayList<Double>>();//���浥�㴥����ѹ��ֵ
		
		for(int i=0;i<list.size();i++)
		{
			dataSeg = list.get(i);
			rateSeg = new ArrayList<Double>();
			//acceSeg = new ArrayList<Double>();
			pressSeg = new ArrayList<Double>();
			
			off = 0;
			pressvari = 0;
			int l = dataSeg.size();
//			Log.i("whyc:l",l+"");
			if(l<=0){
				return false;
			}
			
            double[] x = new double[l];
            double[] y = new double[l];
            double[] time = new double[l];
            //float[] pressure = new float[l];
            
            DataType d ;
            for (int q = 0; q < l; ++q)
            {
                d = dataSeg.get(q);
            	x[q] = d.getX();
                y[q] = d.getY();
                time[q] = d.getTime(); 
                //pressure[q] = d.getPressure();
                pressSeg.add((double)d.getPressure());
            }
            
            //��ֵ
            ArrayList<Double> t_new = new ArrayList<Double>();
            long t_interval = 11000;//��ֵ��ʱ����
            for (double t = time[0]; t <= time[l - 1]; t += t_interval)
            	t_new.add(t);			
            if(t_new.size()<=2){
            	System.out.println("��ֵ���ʱ��㲻������");
            	return false;
            }
            int l_new = (t_new.size() > 2) ? t_new.size() : 2;
//            Log.i("whyc:l_new",l_new+"");
            double[] dx = new double[l];
            double[] ddx = new double[l];
            double[] x_new = new double[l_new];//��ֵ���x��
            double[] y_new = new double[l_new];//��ֵ���y��
            double[] dz = new double[l_new];
            double[] ddz = new double[l_new];
            double[] time_new = new double[l_new];
            for(int ii=0;ii<l_new;ii++){
            	time_new[ii] = t_new.get(ii);
            }
            AnalystUtil.GetValueSpline1(l, time, x, dx, ddx, l_new, time_new, x_new, dz, ddz);//x��Ĳ�ֵ
            AnalystUtil.GetValueSpline1(l, time, y, dx, ddx, l_new, time_new, y_new, dz, ddz);//y��Ĳ�ֵ
            
            //��������
            double[] xArray = x_new;
            double[] yArray = y_new;
            double[] timeArray = time_new;
            int s = l;
//            Log.i("whyc:xArray",xArray.length+"");
//			Log.i("whyc:s",s+"");            
            //Ϊ�˼���ˮƽ�ٶȺʹ�ֱ�ٶ�
            /*
            for (int t = 0; t < s - 1; ++t){
            	double xrate = 1000000 * (xArray[t + 1] - xArray[t]) / (timeArray[t + 1] - timeArray[t]);            	
            	double yrate = 1000000 * (yArray[t + 1] - yArray[t]) / (timeArray[t + 1] - timeArray[t]);
            	System.out.println(xrate+"  "+yrate);
            }
		}
            return true;
	}
	*/
            
			if (Math.abs(xArray[s-1]-xArray[0]) > Math.abs(yArray[s-1]-yArray[0]))
            {
				//����ˮƽ�ٶȺʹ�ֱƫ����
                for (int t = 0; t < s - 1; ++t)
                {
                    double v = 1000000 * (xArray[t + 1] - xArray[t]) / (timeArray[t + 1] - timeArray[t]);
                    rateSeg.add(v);
                    off +=  Math.abs(yArray[t + 1] - yArray[t]);
                }
            }
            else
            {
            	//���㴹ֱ�ٶȺ�ˮƽƫ����
                for (int t = 0; t < s - 1; ++t)
                {
                	double v = 1000000 * (yArray[t + 1] - yArray[t]) / (timeArray[t + 1] - timeArray[t]);
                    rateSeg.add(v);
                    off +=  Math.abs(xArray[t + 1] - xArray[t]);
                }
            }
			//�ٶ� �˲�
			rateSeg = AnalystUtil.MAfilter(rateSeg);
			if(rateSeg == null){
				return false;
			}
			
			//������ٶ�
//			for (int p = 1; p < rateSeg.size(); ++p)
//            {
//                double a = (rateSeg.get(p) - rateSeg.get(p-1)) / (timeArray[p + 1] - timeArray[p]);
//                acceSeg.add(a);
//            }
			
			//���ٶ� �˲�
			//acceSeg = AnalystUtil.MAfilter(acceSeg);
//			if(acceSeg == null){
//				return false;
//			}
			//ѹ���ı�׼��
			pressvari = AnalystUtil.variance(pressSeg);
			
			rate4s.add(rateSeg);
			//acce4s.add(acceSeg);
			offset4s.add(off);
			press4s.add(pressvari);
			//press4s.add(pressSeg);
		}
		for(int i = 0;i<count4singTouch.length;i++){
			fea.add(rate4s.get(i));
		}
//		for(int i = 0;i<count4singTouch.length;i++){
//			fea.add(acce4s.get(i));
//		}
		fea.add(offset4s);
		fea.add(press4s);
//		for(int i = 0;i<count4singTouch.length;i++){
//			fea.add(press4s.get(i));
//		}

		return true;
	}
	
	/*
	 *  ��ȡ��㴥��������
	 */
	public boolean feature4multiTouch(List<ArrayList<DataType>> list,ArrayList<ArrayList<Double>> fea){
		if(list.size()!=count4multiTouch.length)
			return false;
		ArrayList<Double> instanceSeg;//����һ����������ֵ
		ArrayList<Double> degreeSeg;//����һ�������Ƕ��������ֵ
		ArrayList<Double> pressDiffSeg;//����һ�ε�ѹ��ֵ
		ArrayList<DataType> dataSeg;
		ArrayList<ArrayList<Double>> instance4m = new ArrayList<ArrayList<Double>>();//�����㴥������������ֵ 
		ArrayList<ArrayList<Double>> degree4m = new ArrayList<ArrayList<Double>>();//�����㴥���������Ƕ������ 
		ArrayList<ArrayList<Double>> pressDiff4m = new ArrayList<ArrayList<Double>>();//�����㴥����ѹ��ֵ��
		
		for(int i=0;i<list.size();i++){
			dataSeg = list.get(i);
			int length = dataSeg.size();
			if(length<2){
				return false;
			}
			instanceSeg = new ArrayList<Double>();
			degreeSeg = new ArrayList<Double>(); 
			pressDiffSeg = new ArrayList<Double>();
			
			for(int j=0;j<length-1;j++){
				DataType d1 = dataSeg.get(j);//��ȡ��һ����
				DataType d2 = dataSeg.get(j+1);//��ȡ�ڶ�����
				
				int d1ID = d1.getId();
				int d2ID = d2.getId();
				
				if(Math.abs(d1.getTime() - d2.getTime())<10000){
					if(d1ID == d2ID){
						System.out.println("��㴥�������㲻��ͬʱ�����ģ�");
						return false;
					}else{
						//���ڶ�㴥����������
						//����������֮���ŷ������
						double instance = Math.sqrt(Math.pow((d1.getX()-d2.getX()), 2)+Math.pow((d1.getY()-d2.getY()), 2));
						instanceSeg.add(instance);
						
						//����������֮��ĽǶ������
						double degree = Math.atan((d2.y-d1.y)/(d2.x-d1.x))*180/Math.PI;
						if( (0 > degree) || (0 == degree) || (0 < degree)){
							degreeSeg.add(degree);
						}
						//�����������ѹ����
						double pressDiff = 0;
						if(d1ID > d2ID){
							pressDiff = d1.getPressure()-d2.getPressure();
						}else{
							pressDiff = d2.getPressure()-d1.getPressure();
						}
						pressDiffSeg.add(pressDiff);
					}
				}				
			}
			if(pressDiffSeg.size()<=0){
				return false;
			}
			instance4m.add(instanceSeg);
			degree4m.add(degreeSeg);
			pressDiff4m.add(pressDiffSeg);
		}
		for(int i = 0;i<count4multiTouch.length;i++){
			fea.add(instance4m.get(i));
		}
		for(int i = 0;i<count4multiTouch.length;i++){
			fea.add(degree4m.get(i));
		}
		for(int i = 0;i<count4multiTouch.length;i++){
			fea.add(pressDiff4m.get(i));
		}
		return true;
	}
	/*
	 *  ��ȡ��㴥�������� ʹ�ò�ֵ
	 */
	public boolean feature4multiTouch_interpolation(List<ArrayList<DataType>> list,ArrayList<ArrayList<Double>> fea){
		if(list.size()!=count4multiTouch.length)
			return false;
		ArrayList<DataType> dataSeg;//����һ������
		ArrayList<DataType> dataSeg1;//����һ����ָ(idΪ0)��һ������
		ArrayList<DataType> dataSeg2;//��������һ����ָ(idΪ1)��һ������
		ArrayList<Double> instanceSeg;//����һ����������ֵ
		ArrayList<Double> degreeSeg;//����һ�������Ƕ��������ֵ
		ArrayList<Double> pressSeg1;//����һ����ָ��idΪ0�������ݵ�ѹ��ֵ
		ArrayList<Double> pressSeg2;//����һ����ָ��idΪ1�������ݵ�ѹ��ֵ
		//ArrayList<Double> pressDiffSeg;//����һ�ε�ѹ����ֵ
		ArrayList<ArrayList<Double>> instance4m = new ArrayList<ArrayList<Double>>();//�����㴥������������ֵ 
		ArrayList<ArrayList<Double>> degree4m = new ArrayList<ArrayList<Double>>();//�����㴥���������Ƕ������ 
		//ArrayList<ArrayList<Double>> press4m = new ArrayList<ArrayList<Double>>();//�����㴥���������ѹ���� 
		ArrayList<Double> press4m = new ArrayList<Double>();//�����㴥����ѹ��ֵ��׼�� �ֱ���ÿһ�ε�һ����ָ��ѹ����׼���һ����ָ��ѹ����׼��
		
		for(int i=0;i<list.size();i++){
			//����ÿһ���ε�����
			dataSeg = list.get(i);			
			int length = dataSeg.size();
			if(length<2){
				System.out.println("��㴥������һ������ԭʼ������С��2");
				return false;
			}
			//����ͬ��ָ�����ݷֿ��洢
			dataSeg1 = new ArrayList<DataType>();
			dataSeg2 = new ArrayList<DataType>();
			for(int j=0;j<length;j++){
				DataType data = dataSeg.get(j);
				if(data.getId()==0){
					dataSeg1.add(data);
				}else if(data.getId()==1){
					dataSeg2.add(data);
				}
			}
			if(dataSeg1.size()<=0||dataSeg2.size()<=0){
				System.out.println("��㴥������һ������û�ж�㴥��������");
				return false;
			}
			//��ֵ
			pressSeg1 = new ArrayList<Double>();
			pressSeg2 = new ArrayList<Double>();
			int l1 = dataSeg1.size();
			int l2 = dataSeg2.size();
			double[] x1 = new double[l1];
            double[] y1 = new double[l1];
            double[] time1 = new double[l1];
            double[] x2 = new double[l2];
            double[] y2 = new double[l2];
            double[] time2 = new double[l2];
            
            DataType d ;
            for (int t1 = 0; t1 < l1; ++t1)
            {
                d = dataSeg1.get(t1);
            	x1[t1] = d.getX();
                y1[t1] = d.getY();
                time1[t1] = d.getTime();
                pressSeg1.add((double)d.getPressure());
            }
            for (int t2 = 0; t2 < l2; ++t2)
            {
                d = dataSeg2.get(t2);
            	x2[t2] = d.getX();
                y2[t2] = d.getY();
                time2[t2] = d.getTime();
                pressSeg2.add((double)d.getPressure());
            }
            
            long t_interval = 15000;//��ֵ��ʱ����
            double timestart = time1[0]>time2[0]?time1[0]:time2[0];//��ֵ�ĵ�һ��timeֵ
            double timeend = time1[l1-1]>time2[l2-1]?time2[l2-1]:time1[l1-1];//��ֵ�����һ��timeֵ
            
            ArrayList<Double> t1_new = new ArrayList<Double>();           
            for (double temp1 = timestart; temp1 <= timeend; temp1 += t_interval)
            	t1_new.add(temp1);
            ArrayList<Double> t2_new = new ArrayList<Double>();           
            for (double temp2 = timestart; temp2 <= timeend; temp2 += t_interval)
            	t2_new.add(temp2);
            if(t1_new.size()<=2||t2_new.size()<=2){
            	System.out.println("��㴥������һ�����ݲ�ֵ���������С��2");
            	return false;
            }
            
            int l1_new = (t1_new.size() > 2) ? t1_new.size() : 2;
            double[] dx1 = new double[l1];
            double[] ddx1 = new double[l1];
            double[] x1_new = new double[l1_new];//��ֵ���x��
            double[] y1_new = new double[l1_new];//��ֵ���y��
            double[] dz1 = new double[l1_new];
            double[] ddz1 = new double[l1_new];
            double[] time1_new = new double[l1_new];
            for(int ii=0;ii<l1_new;ii++){
            	time1_new[ii] = t1_new.get(ii);
            }
            AnalystUtil.GetValueSpline1(l1, time1, x1, dx1, ddx1, l1_new, time1_new, x1_new, dz1, ddz1);//x��Ĳ�ֵ
            AnalystUtil.GetValueSpline1(l1, time1, y1, dx1, ddx1, l1_new, time1_new, y1_new, dz1, ddz1);//y��Ĳ�ֵ
            int l2_new = (t2_new.size() > 2) ? t2_new.size() : 2;
            double[] dx2 = new double[l2];
            double[] ddx2 = new double[l2];
            double[] x2_new = new double[l2_new];//��ֵ���x��
            double[] y2_new = new double[l2_new];//��ֵ���y��
            double[] dz2 = new double[l2_new];
            double[] ddz2 = new double[l2_new];
            double[] time2_new = new double[l2_new];
            for(int ii=0;ii<l2_new;ii++){
            	time2_new[ii] = t2_new.get(ii);
            }
            AnalystUtil.GetValueSpline1(l2, time2, x2, dx2, ddx2, l2_new, time2_new, x2_new, dz2, ddz2);//x��Ĳ�ֵ
            AnalystUtil.GetValueSpline1(l2, time2, y2, dx2, ddx2, l2_new, time2_new, y2_new, dz2, ddz2);//y��Ĳ�ֵ
            
            //�����ֵ�������
            instanceSeg = new ArrayList<Double>();
			degreeSeg = new ArrayList<Double>(); 
            double[] x1Array = x1_new;
            double[] y1Array = y1_new;
            double[] time1Array = time1_new;
            double[] x2Array = x2_new;
            double[] y2Array = y2_new;
            double[] time2Array = time2_new;
            
            if(time1Array.length!=time2Array.length){
            	System.out.println("��ֵ����:��ֵ�������ʱ�䲻һ��");
            	return false;
            }           	
            for(int p = 0;p<time1Array.length;p++){
				//����������֮���ŷ������
				double instance = Math.sqrt(Math.pow((x1Array[p]-x2Array[p]), 2)+Math.pow((y1Array[p]-y2Array[p]), 2));
				instanceSeg.add(instance);
				
				//����������֮��ĽǶ������
				double degree = Math.atan((y2Array[p]-y1Array[p])/(x2Array[p]-x1Array[p]))*180/Math.PI;
				if( (0 > degree) || (0 == degree) || (0 < degree)){
					degreeSeg.add(degree);
				}				
            }
            
            //ѹ������
            double pressvari1 = AnalystUtil.variance(pressSeg1);
            double pressvari2 = AnalystUtil.variance(pressSeg2);
            //����ָ��ѹ��DTW����
            //double pressDistance = Distance.dtw(pressSeg1, pressSeg2);
            
            instance4m.add(instanceSeg);
			degree4m.add(degreeSeg);
			press4m.add(pressvari1);
			press4m.add(pressvari2);
		}
		for(int i = 0;i<count4multiTouch.length;i++){
			fea.add(instance4m.get(i));
		}
		for(int i = 0;i<count4multiTouch.length;i++){
			fea.add(degree4m.get(i));
		}
		fea.add(press4m);
		return true;
	}
}
