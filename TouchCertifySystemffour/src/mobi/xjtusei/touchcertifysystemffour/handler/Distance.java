package mobi.xjtusei.touchcertifysystemffour.handler;

import java.util.ArrayList;

public class Distance {
	
	public static ArrayList<Double> distance(ArrayList<ArrayList<Double>> a , ArrayList<ArrayList<Double>> b,int[] offIndex){
		ArrayList<Double> res = new ArrayList<Double>();
		int al = a.size();
		
		for(int i = 0;i<al;i++)
        {
			ArrayList<Double> temp1 = a.get(i);
			ArrayList<Double> temp2 = b.get(i);
			
			if(find(offIndex,i)){
//				double d = dtw(temp1,temp2);
//				if(Double.isNaN(d)){
//					System.out.println("����NaN����������ά��Ϊ"+i);
//					return null;
//				}
//				res.add(d);
				//�������ά���̶� ����Ҫʹ��DTW ����ֱ������ķ�ʽ
//				for(int j = 0;j<temp1.size();j++){
//					double temp = Math.abs(temp1.get(j)-temp2.get(j));
//					res.add(temp);
//				}
				//�������ά���̶� ����Ҫʹ��DTW ���ü���ŷ������ķ�ʽ
				if(temp1.size()!=temp2.size()){
					System.out.println("�������������������ŷ���������������ά�Ȳ�һ��");
					return null;
				}
				double result = 0;
				for(int j = 0;j<temp1.size();j++){
					double temp = temp1.get(j)-temp2.get(j);
					result+=temp*temp;
				}
				res.add(result);
			}
			else{
				double d = dtw(temp1,temp2);
				if(Double.isNaN(d)){
					System.out.println("����NaN����������ά��Ϊ"+i);
					return null;
				}
				res.add(d);
			}
        }
		
		return res;		
	}
	//�ж�ĳ�����Ƿ���������
	public static boolean find(int[] source,int target){
		for(int i = 0;i<source.length;i++){
			if(target == source[i]){
				return true;
			}
		}
		return false;
	}
	
	public static double euclidean(ArrayList<Double> a)
    {
        double rel = 0;
        for (int i = 0; i < a.size(); i++)
        {
            double temp = (double)a.get(i);
            rel += temp*temp;
        }
        return Math.sqrt(rel);
    }
	
	public static double dtw(ArrayList<Double> a,ArrayList<Double> b){
		int al = a.size();
        int bl = b.size();
        double[][] d = new double[al][bl];
        double[][] D = new double[al][bl];

        for (int i = 0; i < al; i++)
        {
            for (int j = 0; j < bl; j++)
            {
                d[i][j] = Math.sqrt((a.get(i)-b.get(j))*(a.get(i)-b.get(j))); 
            }
        }
        
        for (int i = 0; i < al; i++)
        {
            for (int j = 0; j < bl; j++)
            {
                D[i][j] = 0; 
            }
        }
        D[0][0] = d[0][0];
        /*for (int p1 = 0; p1 < D.GetLength(0); p1++)
            for (int p2 = 0; p2 < D.GetLength(1); p2++)
                Console.WriteLine(D[p1, p2]);*/
        for (int j=1;j<bl;j++)
        {
            D[0][j] = d[0][j]+D[0][j-1];
        }
        
        double [] temp = new double[3];
        for (int i=1;i<al;i++)
        {
            for (int j=0;j<bl;j++)
            {
              temp[0] = D[i-1][j];
              if(j>0)
              {
                  temp[1] = D[i][j - 1];
                  temp[2] = D[i - 1][j - 1];
              }
              else
              {
	               temp[1] = Double.MAX_VALUE; // matlab �ܱ�ʾ����������˴�Ϊ�˱�ʾ���������double�����ֵ
	               temp[2] = Double.MAX_VALUE;
              }
              double tempSmall = temp[0];
              //int tempIndex = 0;
              for (int p = 1; p < 3; p++)
              {
                  if (temp[p] < tempSmall)
                  {
                      tempSmall = temp[p];
                      //tempIndex = p;
                  }
              }
              D[i][j] = d[i][j] + tempSmall;
             
            }
        }
        return  D[al-1][bl-1];
	}
}
