package com.example.jwwapp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jwwapp2.tool.JwwInfoFetcher;
import com.example.jwwapp2.tool.WrappedHttpClient;

public class ThirdActivity extends Activity {
	ArrayList<String> items = new ArrayList<String>();
	ArrayList<String> names2 = new ArrayList<String>();//1
	ArrayList<String> grades2 = new ArrayList<String>();	//2
	ArrayList<String> seasons = new ArrayList<String>();//5
	ArrayList<String> times = new ArrayList<String>();//6
	ArrayList<String> rooms = new ArrayList<String>();//7
	ArrayList<String> positions = new ArrayList<String>();//8
	ProgressDialog mydialog;
		private static String data;
		private static WrappedHttpClient whc = new WrappedHttpClient();
		TextView mTitleView;
		ListView list;
   @Override
    public void onCreate(Bundle savedInstanceState) {
	   this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    	mTitleView = (TextView) findViewById(R.id.title_text);
		mTitleView.setText(R.string.exam);
		list = (ListView) findViewById(R.id.ListViewex);
		_getScheduleThread.start();
		mydialog = ProgressDialog.show(this, "���Ե�...", "���ڼ���...",true); 
   }

   private Handler handler = new Handler(){  
   	  
   	        @Override  
   	        public void handleMessage(Message msg) {         
   	    		Toast.makeText(getApplicationContext(), "��ȡ������Ϣ�ɹ�", 8).show();
   	    		
   	    		 {
   	    		 	int f = 0;
    				String[] s = data.split("</tr><tr");
    				for(String str: s){
    						String regEx="((?<=<td>).*(?=</td>))";
    						Pattern p=Pattern.compile(regEx);
    						Matcher m=p.matcher(str);
    						if(m.find()) {
    							String  ts = m.group();
    							if(ts.contains("�γ�����")) continue;
    							ts = ts.replace("&nbsp;","   ");
    									items.add(ts);
    						}
    					}
    		}
    	   {
    		 
    		   names2.add("�γ�����");
					grades2.add("ѧ��");
					seasons.add("ѧ��");
					times.add("����ʱ��");
					rooms.add("����");
					positions.add("��λ��");
 				for(String str: items){
 	   					String [] ss = str.split("</td><td>");	   					
 	   						names2.add(ss[1]);
 	   						grades2.add(ss[2]);
 	   						seasons.add(ss[5]);
 	   						ss[6]=ss[6].replace('(','\0');
 	   						ss[6]=ss[6].replace(')','\0');
 	   						ss[6]=ss[6].replace(" ","");
 	   						times.add(ss[6]);
 	   						ss[7]=ss[7].replace('(','\0');
 	   						ss[7]=ss[7].replace(')','\0');
 	   						ss[7]=ss[7].replace("��","");
 	   						rooms.add(ss[7]);
 	   						positions.add(ss[8]);
 				}
    	   }
    	  
    		int len = names2.size();
    	    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    	    for(int i=0; i<len; i++)
    	    {
    	    	HashMap<String, String> map = new HashMap<String, String>();
    	    	map.put("names2", names2.get(i));
    	    	map.put("grades2", grades2.get(i));
    	    	map.put("seasons", seasons.get(i));
    	    	map.put("times", times.get(i));
    	    	map.put("rooms", rooms.get(i));
    	    	map.put("positions", positions.get(i));
    	    	listItem.add(map);
    	    }
    	    
    	   

    	    
    	    //������������Item�Ͷ�̬�����Ӧ��Ԫ��
    	    SimpleAdapter listItemAdapter = new SimpleAdapter(ThirdActivity.this,listItem,//����Դ 
    	        R.layout.examslists,//ListItem��XMLʵ��
    	        //��̬������ImageItem��Ӧ������        
    	        new String[] {"names2", "grades2" , "seasons","times","rooms","positions"}, 
    	        //ImageItem��XML�ļ������һ��ImageView,����TextView ID
    	        new int[] {R.id.names2,R.id.grades2,R.id.seasons,R.id.times,R.id.rooms,R.id.positions}
    	    );	
    	   
    	    //��Ӳ�����ʾ
    	    list.setAdapter(listItemAdapter);	 
    	    if(mydialog!=null) mydialog.dismiss();
   	        }};  

   
    Thread _getScheduleThread = new Thread() {

		@Override
		public void run() {
			try {
				data = JwwInfoFetcher.GetCurrentExamInfo();
				handler.sendEmptyMessage(0);  

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
					}

	};
	
	//����Menu�˵�    
	   public boolean onCreateOptionsMenu(Menu menu) {    
	       menu.add(0, Menu.FIRST, 0, "����"); 
	       //menu.add(0, Menu.FIRST+1, 0, "ȡ��"); 
	       return super.onCreateOptionsMenu(menu);    
	    }    
	   
	   //���Menu�˵�ѡ����Ӧ�¼�    
	    public boolean onOptionsItemSelected(MenuItem item) {    
	        switch(item.getItemId()){    
	       case 1:    
	            Toast.makeText(this, "����", Toast.LENGTH_SHORT).show();
	        	Intent it = new Intent();
				it.setClass(this,
						MoreActivity.class);//��ת
				startActivity(it);
	            break;    
	     //   case 2:    
	       //    Toast.makeText(this, "ȡ��", Toast.LENGTH_SHORT).show();
	           
	         //   break;    
	        }    
	        return super.onOptionsItemSelected(item);    
	    }    
}
