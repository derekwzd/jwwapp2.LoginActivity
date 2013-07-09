package com.example.jwwapp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GridActivity extends Activity {

	private static ArrayList<String> news = new ArrayList<String>();
	private static ArrayList<String> newsfrom = new ArrayList<String>();
	private Button  LoginButton;
	ProgressDialog mydialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
      
        ListView list = (ListView) findViewById(R.id.ListView01);
       
        //���ɶ�̬���飬��������
    	mydialog = ProgressDialog.show(this, "���Ե�...", "���ڼ���...",true); 
		
                 
        try {
			getnews();
			handler.sendEmptyMessage(0);  
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//����
        int len = news.size();
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<len; i++)
        {
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("ItemTitle", news.get(i));
        	map.put("ItemText", newsfrom.get(i));
        	listItem.add(map);
        }
        
       
	
        
        //������������Item�Ͷ�̬�����Ӧ��Ԫ��
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//����Դ 
            R.layout.lists,//ListItem��XMLʵ��
            //��̬������ImageItem��Ӧ������        
            new String[] {"ItemTitle", "ItemText"}, 
            //ImageItem��XML�ļ������һ��ImageView,����TextView ID
            new int[] {R.id.ItemTitle,R.id.ItemText}
        );
       
        //��Ӳ�����ʾ
        list.setAdapter(listItemAdapter);
        if(mydialog!=null) mydialog.dismiss();
        
        //��ӵ��
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it1 = new Intent();
	 			it1.setClass(GridActivity.this,
	 					WebActivity.class);//��ת
	 			startActivity(it1);
			}
		});
    }

    private Handler handler = new Handler(){  
	   	  
        @Override  
        public void handleMessage(Message msg) {         
    		Toast.makeText(getApplicationContext(), "��ȡ�����б�ɹ�", 5).show();
    		//pd.dismiss();
    		 //txTextView.setText(data);
       }};  


       public void getnews() throws UnsupportedEncodingException, IOException{
    	URL homepage=new URL("http://jwbinfosys.zju.edu.cn/default2.aspx");
   		BufferedReader in=new BufferedReader(new InputStreamReader(homepage.openStream(),"GB2312"));//"utf-8"));
   		String str;
   		while ((str = in.readLine()) != null) {
   		String regEx="((?<=/>).*(?=</a>))";
   		String regEx1="((?<=<td>).*(?=</td><))";
   		Pattern p=Pattern.compile(regEx);
   		Pattern p1=Pattern.compile(regEx1);
   		Matcher m=p.matcher(str);
   		Matcher m1=p1.matcher(str);
   		//System.out.println(str);
   		if(m.find() && m1.find()) {
   			//System.out.println(m.group()+"  "+m1.group());
   			String s1,s2;
   			s1 = m.group();
   			s2=m1.group();
            news.add(s1);
   			if(s2.contains("nbsp")) newsfrom.add("������֪ͨ");
   			else newsfrom.add(s2);
   		}
   	}
 		
 	}
    	   
       


       @Override
   	public boolean onKeyDown(int keyCode, KeyEvent event) {

   		// ���¼����Ϸ��ذ�ť
   		if (keyCode == KeyEvent.KEYCODE_BACK) {

   			new AlertDialog.Builder(this)
   					.setMessage("ȷ���˳���")
   					.setNegativeButton("ȡ��",
   							new DialogInterface.OnClickListener() {
   								@Override
   								public void onClick(DialogInterface dialog,
   										int which) {
   								}
   							})
   					.setPositiveButton("ȷ��",
   							new DialogInterface.OnClickListener() {
   								@Override
   								public void onClick(DialogInterface dialog,
   										int whichButton) {
   									finish();
   								}
   							}).show();

   			return true;
   		} else {
   			return super.onKeyDown(keyCode, event);
   		}
   	}

   	@Override
   	protected void onDestroy() {
   		super.onDestroy();
   		finish();
   	}
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