#include "mobi_xjtusei_touchcertifysystemffour_mtcollector_MultiTouchView.h"
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <linux/input.h>
#include <string.h>

static int event0_fd = -1;
struct input_event ev0[64];
int realx = 0;//�Ӵ�������x������� 
int realy = 0;//�Ӵ�������y������� 
int touch = 0;//�Ӵ��泤��
int width = 0;//���߳��� 
int id = 0;//�����ǵڼ�����ָ 
int distance = 0;//�Ӵ����豸���浽���ߵľ��� 
int toolx = 0;//center x tool position
int flag = 0;//��ʾ�Ƿ��Ѿ���¼���� 0��ʾ��û�м�¼ 1��ʾ�Ѿ���¼���� 

int realx1 = 0;
int realy1 = 0;
int id1 = 0;
int distance1 = 0;
int toolx1 = 0;
int flagid = 0;
int xpre=0;int ypre=0;int len=0;int len2=0;int qq = 0;int symb = 0;	long time1 = 0;


//��ʼ��
JNIEXPORT jint JNICALL Java_mobi_xjtusei_touchcertifysystemffour_mtcollector_MultiTouchView_Init(JNIEnv * env, jobject obj, jint file)
{	
    char *filename = "/dev/input/event1";
    if(file==0)
    {
         filename = "/dev/input/event0";
    }
    else if(file==2)
    {
         filename = "/dev/input/event2";
    }
    else if(file==3)
    {
             filename = "/dev/input/event3";
    }
    else if(file==8)
        {
                 filename = "/dev/input/event8";
        }
    
	event0_fd = open(filename, O_RDWR);

	if (event0_fd<0)
		return -1;
	return 1;
}


JNIEXPORT jint JNICALL Java_mobi_xjtusei_touchcertifysystemffour_mtcollector_MultiTouchView_UnInit(JNIEnv * env, jobject obj)
{
	if ( event0_fd > 0 ) {
		close(event0_fd);
		event0_fd = -1;
	}
	return 1;
}

//collect data
JNIEXPORT jstring JNICALL Java_mobi_xjtusei_touchcertifysystemffour_mtcollector_MultiTouchView_CollectData
(JNIEnv * env, jobject obj)   
{   
	int button = 0, i, rd ;
	long long time = 0;

	float pressure = 0;
	float pressure1 = 0;
	char* result =  (char*)malloc(sizeof(char)*500);
	int size = 0;
	int leftSize = 0;
	sprintf(result, "");

	rd = read(event0_fd, ev0, sizeof(struct input_event) * 64);
	if ( rd < sizeof(struct input_event) ) 
		return (*env)->NewStringUTF(env, (char *)"");//return empty string
	
	
	/*
	//����������Ϣ 
	for (i = 0; i < rd / sizeof(struct input_event); i++){
		sprintf(result, "%st %d c %d v %d\n",result,ev0[i].type,ev0[i].code,ev0[i].value);
	}
	jstring str = (*env)->NewStringUTF(env, result);
	free(result);
	return str;
	*/
/*
   //����С�ֻ��Ĳɼ���ʽ
  for (i = 0; i < rd / sizeof(struct input_event); i++) {
		size = 0;leftSize = 0;
		if (ev0[i].type == 3 && ev0[i].code == 53)
			realx = ev0[i].value;
		else if (ev0[i].type == 3 && ev0[i].code == 54)
			realy = ev0[i].value;
		
		
        if(ev0[i].type == 3&&(ev0[i].code==53||ev0[i].code==54))
		{           
			time = (long long)ev0[i].time.tv_sec*(long long)1000000 + ev0[i].time.tv_usec;
			sprintf(result, "%s%d %d %lld\n",result,realx,realy,time); 
        }
	}
*/
	//����code=57������Ҫ����Ϣ �������note Android2.3�汾
	for (i = 0; i < rd / sizeof(struct input_event); i++) {
				size = 0;leftSize = 0;
				 if(ev0[i].type == 3){
		                 if ( ev0[i].code == 53 )//x������
					        realx = ev0[i].value;
				         else if ( ev0[i].code == 54 )//y������
				            realy = ev0[i].value;
				         else if ( ev0[i].code == 48 )//���豸�Ӵ������򣨳��ᣩ
			                touch = ev0[i].value;
		                 else if ( ev0[i].code == 50 )//���߱�������򣨳��ᣩ
			                width = ev0[i].value;
		                 else if( ev0[i].code == 57 )//�����ǵڼ�����ָ
		                 {
		                	 id = ev0[i].value;
		                	 if(touch == 0 || width == 0){
		                		 pressure = 0;
		                	 }
		                	 else{
		                		 pressure = (float)touch/width;
		                	 }
		                	 time = (long long)ev0[i].time.tv_sec*(long long)1000000 + ev0[i].time.tv_usec;
		                	 sprintf(result, "%s%d %d %lld %.2f %d %d %d\n",result,realx,realy,time,pressure,id,touch,width);
		                 }
		          }
		}
	
/*
	//������Ҫ����Ϣ
	for (i = 0; i < rd / sizeof(struct input_event); i++) {
		size = 0;leftSize = 0;
		 if(ev0[i].type == 3){
                 flag = 0;//����һ���µ�����
                 if ( ev0[i].code == 53 )//x������
			        realx = ev0[i].value;
		         else if ( ev0[i].code == 54 )//y������
		            realy = ev0[i].value;
		         else if ( ev0[i].code == 48 )//���豸�Ӵ������򣨳��ᣩ
	                touch = ev0[i].value;
                 else if ( ev0[i].code == 50 )//���߱�������򣨳��ᣩ
	                width = ev0[i].value;
                 else if( ev0[i].code == 57 )//�����ǵڼ�����ָ
                    id = ev0[i].value;
                 else if( ev0[i].code == 59 )//�����豸���浽���ߵľ���
                    distance = ev0[i].value;
                 else if( ev0[i].code == 60 )//center x tool position
                    toolx = ev0[i].value;
          }
		else{

                if( flag == 0 && ev0[i].code == 2 && ev0[i].type == 0 && ev0[i].value == 0 )
        		{
                	if(touch == 0 || width == 0){
                		pressure = 0;
                	}
                	else{
                		pressure = (float)touch/width;
                	}
        			time = (long long)ev0[i].time.tv_sec*(long long)1000000 + ev0[i].time.tv_usec;
        			sprintf(result, "%s%d %d %lld %.2f %d %d %d\n",result,realx,realy,time,pressure,id,distance,toolx);
        			flag = 1;//��ʾ�Ѿ���¼������
                }
                if( flag == 0 && ev0[i].code == 0 && ev0[i].type == 0 && ev0[i].value == 0 )
        		{
                    if(touch == 0 || width == 0){
                    	pressure = 0;
                    }
                    else{
                    	pressure = (float)touch/width;
                    }
        			time = (long long)ev0[i].time.tv_sec*(long long)1000000 + ev0[i].time.tv_usec;
        			sprintf(result, "%s%d %d %lld %.2f %d %d %d\n",result,realx,realy,time,pressure,id,distance,toolx);
        			flag = 1;//��ʾ�Ѿ���¼������
                }
        }
}

/*
		if( ev0[i].code == 2 && ev0[i].type == 0 && ev0[i].value == 0 ){
			if(touch==0||width==0){
				pressure = 0;
			}else{
				pressure = (float)touch/width;
			}
			if( flag == 0 ){
				realx1 = realx;
				realy1 = realy;
				pressure1 = pressure;
				id1 = id;
				distance1 = distance;
				toolx1 = toolx;
				flag = 1;
			}
		}
		if( ev0[i].code == 0 && ev0[i].type == 0 && ev0[i].value == 0 ){
			time = (long long)ev0[i].time.tv_sec*(long long)1000000 + ev0[i].time.tv_usec;
			sprintf(result, "%s%d %d %lld %.2f %d %d %d\n",result,realx1,realy1,time,pressure1,id1,distance1,toolx1);
			if(realx1==realx && realy1==realy){

			}else{
				sprintf(result, "%s%d %d %lld %.2f %d %d %d\n",result,realx,realy,time,pressure,id,distance,toolx);
			}
			flag = 0;
		}
	}
}
*/

	jstring str = (*env)->NewStringUTF(env, result);
	free(result);
	return str;

	
}   
