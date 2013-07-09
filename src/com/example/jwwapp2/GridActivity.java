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
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去除
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
      
        ListView list = (ListView) findViewById(R.id.ListView01);
       
        //生成动态数组，加入数据
    	mydialog = ProgressDialog.show(this, "请稍等...", "正在加载...",true); 
		
                 
        try {
			getnews();
			handler.sendEmptyMessage(0);  
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//新闻
        int len = news.size();
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<len; i++)
        {
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("ItemTitle", news.get(i));
        	map.put("ItemText", newsfrom.get(i));
        	listItem.add(map);
        }
        
       
	
        
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.lists,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"ItemTitle", "ItemText"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.ItemTitle,R.id.ItemText}
        );
       
        //添加并且显示
        list.setAdapter(listItemAdapter);
        if(mydialog!=null) mydialog.dismiss();
        
        //添加点击
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it1 = new Intent();
	 			it1.setClass(GridActivity.this,
	 					WebActivity.class);//跳转
	 			startActivity(it1);
			}
		});
    }

    private Handler handler = new Handler(){  
	   	  
        @Override  
        public void handleMessage(Message msg) {         
    		Toast.makeText(getApplicationContext(), "获取新闻列表成功", 5).show();
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
   			if(s2.contains("nbsp")) newsfrom.add("教务网通知");
   			else newsfrom.add(s2);
   		}
   	}
 		
 	}
    	   
       


       @Override
   	public boolean onKeyDown(int keyCode, KeyEvent event) {

   		// 按下键盘上返回按钮
   		if (keyCode == KeyEvent.KEYCODE_BACK) {

   			new AlertDialog.Builder(this)
   					.setMessage("确定退出？")
   					.setNegativeButton("取消",
   							new DialogInterface.OnClickListener() {
   								@Override
   								public void onClick(DialogInterface dialog,
   										int which) {
   								}
   							})
   					.setPositiveButton("确定",
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
  //创建Menu菜单    
    public boolean onCreateOptionsMenu(Menu menu) {    
        menu.add(0, Menu.FIRST, 0, "关于"); 
        //menu.add(0, Menu.FIRST+1, 0, "取消"); 
        return super.onCreateOptionsMenu(menu);    
     }    
    
    //点击Menu菜单选项响应事件    
     public boolean onOptionsItemSelected(MenuItem item) {    
         switch(item.getItemId()){    
        case 1:    
             Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
         	Intent it = new Intent();
 			it.setClass(this,
 					MoreActivity.class);//跳转
 			startActivity(it);
             break;    
      //   case 2:    
        //    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
            
          //   break;    
         }    
         return super.onOptionsItemSelected(item);    
     }    
 }