package com.example.jwwapp2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends Activity {
	WebView wv ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        wv = (WebView)findViewById(R.id.wv); 
        try {   
        	wv.loadUrl("http://jwbinfosys.zju.edu.cn/default2.aspx");  
        	wv.getSettings().setJavaScriptEnabled(true);  
        } catch (Exception ex) 
        {   ex.printStackTrace();   }  
    }

}
