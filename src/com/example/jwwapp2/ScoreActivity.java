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

import com.example.jwwapp2.R.menu;
import com.example.jwwapp2.tool.JwwInfoFetcher;
public class ScoreActivity extends Activity {
	ArrayList<String> items = new ArrayList<String>();
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> scores = new ArrayList<String>();
	ArrayList<String> grades = new ArrayList<String>();	
	ArrayList<String> gpas = new ArrayList<String>();
		private static String data;
		private TextView mTitleView;
		ProgressDialog mydialog;
		ListView list;
   @Override
    public void onCreate(Bundle savedInstanceState) {
	   this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        list = (ListView) findViewById(R.id.ListViewsc);
    	mTitleView = (TextView) findViewById(R.id.title_text);
		mTitleView.setText(R.string.score);
        _getScheduleThread.start();
        mydialog = ProgressDialog.show(this, "���Ե�...", "���ڼ���...",true); 
        
    }
        
   	
   
   private Handler handler = new Handler(){  
   	  
   	        @Override  
   	        public void handleMessage(Message msg) {         
   	    		Toast.makeText(getApplicationContext(), "��ȡ�ɼ��ɹ�", Toast.LENGTH_LONG).show();
   	     {Log.d("TAG2","dat4");
   				String[] s = data.split("</tr><tr");
   				for(String str: s){
   						String regEx="((?<=<td>).*(?=</td>))";
   						Pattern p=Pattern.compile(regEx);
   						Matcher m=p.matcher(str);
   						if(m.find()) {
   							String  ts = m.group();
							//if(ts.contains("�γ�����")) f++;
							if(ts.contains("�γ�����")) continue;
							items.add(ts);
   						}
   					}
   		}
   	   {
   		names.add("�γ�����");
		grades.add("ѧ��");
		gpas.add("����");
		scores.add("�ɼ�");
				for(String str: items){
	   					String [] ss = str.split("</td><td>");
	   						names.add(ss[1]);
	   						scores.add(ss[2]);
	   						grades.add(ss[3]);
	   						gpas.add(ss[4]);
	   						//Log.d("TAG4","dat4");
				}
   	   }
   	int len = names.size();
    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    for(int i=0; i<len; i++)
    {
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("names", names.get(i));
    	map.put("scores", scores.get(i));
    	map.put("grades", grades.get(i));
    	map.put("gpas", gpas.get(i));
    	listItem.add(map);
    }
    
   

    
    //������������Item�Ͷ�̬�����Ӧ��Ԫ��
    SimpleAdapter listItemAdapter = new SimpleAdapter(ScoreActivity.this,listItem,//����Դ 
        R.layout.scorelists,//ListItem��XMLʵ��
        //��̬������ImageItem��Ӧ������        
        new String[] {"names","scores", "grades" , "gpas"}, 
        //ImageItem��XML�ļ������һ��ImageView,����TextView ID
        new int[] {R.id.names,R.id.scores,R.id.grades,R.id.gpas}
    );
   
    //��Ӳ�����ʾ
    list.setAdapter(listItemAdapter);
    if(mydialog!=null) mydialog.dismiss();
   	   }
   	  };  

   
    Thread _getScheduleThread = new Thread() {

		@Override
		public void run() {
			try {
				data = JwwInfoFetcher.GetCurrentGradeInfo();
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
