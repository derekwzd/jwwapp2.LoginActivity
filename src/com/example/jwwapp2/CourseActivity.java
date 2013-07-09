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

public class CourseActivity extends Activity {
	ArrayList<String> items = new ArrayList<String>();
	ArrayList<String> names3 = new ArrayList<String>();//1
	ArrayList<String> teaches = new ArrayList<String>();//2
	ArrayList<String> seasons3 = new ArrayList<String>();//3
	ArrayList<String> times3 = new ArrayList<String>();//4
	ArrayList<String> rooms3 = new ArrayList<String>();//5
		private static String data;
		private TextView mTitleView;
		ListView list;
		ProgressDialog mydialog;
   @Override
    public void onCreate(Bundle savedInstanceState) {
	   this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去除
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
		list = (ListView) findViewById(R.id.ListViewco);
    	mTitleView = (TextView) findViewById(R.id.title_text);
		mTitleView.setText(R.string.course);
		_getScheduleThread.start();
		mydialog = ProgressDialog.show(CourseActivity.this, "请稍等...", "正在加载...",true); 
		
		
	}

   private Handler handler = new Handler(){  
   	  
   	        @Override  
   	        public void handleMessage(Message msg) {         
   	    		Toast.makeText(getApplicationContext(), "获取课表成功", 8).show();
   	    		{
   	    			Log.d("TAG2","dat4");
   	    			String[] s = data.split("</tr><tr");
   	    			names3.add("课程名称");
   	    			teaches.add("教师");
   	    			seasons3.add("学期");
   	    			times3.add("上课时间");
   	    			rooms3.add("教室");
   	    			for(String str: s){
				   //System.out.println(str);
						String regEx="((?<=<td>).*(?=</td>))";
						Pattern p=Pattern.compile(regEx);
						Matcher m=p.matcher(str);
						if(m.find()) {
							String  ts = m.group();	
							String [] stmp = ts.split("</td><td>");
							Log.d("T0", "A0");
						//	for(String str1: stmp){
							//System.out.println(stmp.length);
							for(int i =0; i< stmp.length;i++)	
							{
								//System.out.println(i);
								if(i==1){	//stmp[i]
									String regEx1="((?<=red>).*(?=</font>))";
									Pattern p1=Pattern.compile(regEx1);
									Matcher m1=p1.matcher(stmp[i]);
									if(m1.find()) {
										String s1 = m1.group();
										//System.out.println(s1);
										names3.add(s1);
									}
									else {
										regEx1="((?<=>).*(?=</A>))";
										p1=Pattern.compile(regEx1);
										m1=p1.matcher(stmp[i]);
										if(m1.find()) {
											String s1 = m1.group();
										//	System.out.println(s1);
											names3.add(s1);
											}
										}
									}
								 if(i==2)
									{	//stmp[i]
									 	String regEx1="((?<=red>).*(?=</font>))";
									 	Pattern p1=Pattern.compile(regEx1);
									 	Matcher m1=p1.matcher(stmp[i]);
										if(m1.find()) {
											String s1 = m1.group();
											//System.out.println(s1);
											s1=s1.replace("#", "");		
											s1=s1.replace("<br>", " ");
											teaches.add(s1);
										}
										else {
											regEx1="((?<=>).*(?=</a>))";
											p1=Pattern.compile(regEx1);
											m1=p1.matcher(stmp[i]);
											if(m1.find()) {
												String s1 = m1.group();
											//	System.out.println(s1);
												s1=s1.replace("#", "");		
												s1=s1.replace("<br>", " ");
												teaches.add(s1);
											}											
										}
									}
									if(i==3) {
										//System.out.println("3"+stmp[i]);
										seasons3.add(stmp[i]);
										}
										if(i==4) {stmp[i]=stmp[i].replace("#", "");		
										stmp[i]=stmp[i].replace("<br>", " ");
										times3.add(stmp[i]);	
										//System.out.println(stmp[i]);
									}
									if(i==5) {
										stmp[i]=stmp[i].replace("#", "");		
										stmp[i]=stmp[i].replace("<br>", " ");
										stmp[i]=stmp[i].replace("(多)","");
										stmp[i]=stmp[i].replace('(','\0');
										stmp[i]=stmp[i].replace(')','\0');
										 
										rooms3.add(stmp[i]);
										//System.out.println(stmp[i]);
									}
							}
							
							Log.d("T1", "A1");
						}
					}
		}
   	    		
   	        
   	    		int len = names3.size();
   	 	    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
   	 	    for(int i=0; i<len; i++)
   	 	    {
   	 	    	HashMap<String, String> map = new HashMap<String, String>();
   	 	    	map.put("names3", names3.get(i));
   	 	    	map.put("teaches", teaches.get(i));
   	 	    	map.put("seasons", seasons3.get(i));
   	 	    	map.put("times3", times3.get(i));
   	 	    	map.put("rooms3", rooms3.get(i));
   	 	    	listItem.add(map);
   	 	    }
   	 	    
   	 	   

   	 	    
   	 	    //生成适配器的Item和动态数组对应的元素
   	 	    SimpleAdapter listItemAdapter = new SimpleAdapter(CourseActivity.this,listItem,//数据源 
   	 	        R.layout.courselists,//ListItem的XML实现
   	 	        //动态数组与ImageItem对应的子项        
   	 	        new String[] {"names3", "teaches" , "seasons3","times3","rooms3"}, 
   	 	        //ImageItem的XML文件里面的一个ImageView,两个TextView ID
   	 	        new int[] {R.id.names3,R.id.teaches,R.id.seasons3,R.id.times3,R.id.rooms3}
   	 	    );	
   	 	    //添加并且显示
   	 	    list.setAdapter(listItemAdapter);	 
   	 	    if(mydialog!=null) mydialog.dismiss();
   	         }  
   	     };

    Thread _getScheduleThread = new Thread() {

		@Override
		public void run() {
			try {
				data = JwwInfoFetcher.GetCoursePageContent();
				handler.sendEmptyMessage(0);  
				 Log.d("TAG","dat4");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
					}

	};
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
