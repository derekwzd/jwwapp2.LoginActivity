package com.example.jwwapp2.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

import android.util.Log;

import com.example.jwwapp2.LoginActivity;


public class JwwInfoFetcher {
	/**
	 * @param args
	 */
	private static WrappedHttpClient whc = new WrappedHttpClient();
	public static String header = "http://jwbinfosys.zju.edu.cn/";
	private static String index = "default2.aspx";
	private static String _id, _ps;
	private static String _viewState;
	public static final String Spring = "春";
	public final static String Summer = "夏";
	public final static String Autumn = "秋";
	public final static String Winter = "冬";
	/**
	 * 
	 * @param in
	 *            传入网页内容
	 * @return 从页面中获得的viewstate
	 */
	private static String getViewState(String in) {
		String regex = "name=\"__VIEWSTATE\" value=\"([^\"]*)\" />";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(in);
		String id = null;
		if (matcher.find())
			id = (matcher.group(1));
		return id;
	}

	public static void setId(String id) {
		_id = id;
	}

	public static void setPs(String ps) {
		_ps = ps;
	}

	public static String getId() {
		return _id;
	}

	public static String getPs() {
		return _ps;
	}
	public static TaskResult LoginJww(String id, String ps, String ck)
			throws Exception {
		_id = id;
		_ps = ps;
		String indexpage;
		String ret = null;
		indexpage = whc.GetContent(header + index);
		whc.cleanPostContent();
		_viewState = getViewState(indexpage);
		whc.AddToPostContent("__EVENTTARGET", "Button1");
		whc.AddToPostContent("__EVENTARGUMENT", "");
		whc.AddToPostContent("__VIEWSTATE", _viewState);
		whc.AddToPostContent("TextBox1", id);
		whc.AddToPostContent("TextBox2", ps);
		// whc.AddToPostContent("TextBox3",
		// VerifyBreaker.GetImgNum(fetchCheckCodeByteArray()));
		whc.AddToPostContent("TextBox3", "");
		whc.AddToPostContent("RadioButtonList1", "学生");
		whc.AddToPostContent("Text1", "");
		ret = whc.PostContent(header + index);
		//System.out.println(header + ret);
		if (ret.contains("用户名不存在"))
			return TaskResult.LOGIN_USERNAME_NOT_EXSIT;
		else if (ret.contains("密码错误"))
			return TaskResult.LOGIN_PASSWORD_INCORRECT;
		else if (ret.contains("验证码不正确"))
			return TaskResult.LOGIN_CHECKCODE_INCORRECT;
		else if (ret.contains("writeDateInfo"))
			return TaskResult.LOGIN_SUCCESS;
		else
			return TaskResult.LOGIN_SERVER_INTERNAL_ERROR;
	}
	
	
	public static String GetCurrentGradeInfo() throws Exception {
		whc.cleanPostContent();
		_viewState = getViewState(whc.GetContent(header + "xscj.aspx?xh="
				+ LoginActivity.studentId));
		whc.AddToPostContent("__VIEWSTATE", _viewState);
		whc.AddToPostContent("xh", LoginActivity.studentId);
		whc.AddToPostContent("ddlXN", "");
		whc.AddToPostContent("ddlXQ", "");
		whc.AddToPostContent("txtQSCJ", "");
		whc.AddToPostContent("txtZZCJ", "");
		whc.AddToPostContent("Button2", "在校学习成绩查询");
		return whc.PostContent(header + "xscj.aspx?xh="
				+ LoginActivity.studentId);
	}

	/**
	 * @deprecated
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	static Date date = new Date();
	public static int getIntegerSemesterProperty()
	{
		
		if(date.getMonth() >=9 || date.getMonth() <2 )
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	public static String getBigSemesterProperty()
	{
		if(date.getMonth() >=5 &&  date.getMonth() < 11)
		{
			return "1|秋、冬";
		}
		else
		{
			return "2|春、夏";
		}
	}
	public static String GetCoursePageContent() throws Exception {
		String resString = whc.GetContent(header + "xskbcx.aspx?xh=" + _id);
		_viewState = getViewState(resString);
		whc.cleanPostContent();
		whc.AddToPostContent("__VIEWSTATE", _viewState);
		whc.AddToPostContent("__EVENTTARGET", "xqd");
		whc.AddToPostContent("__EVENTARGUMENT", "");
		whc.AddToPostContent("xxms", "列表");
		whc.AddToPostContent("xqd", getBigSemesterProperty());
		whc.AddToPostContent("kcxx", "");
		resString = whc.PostContent(header + "xskbcx.aspx?xh=" + _id);
		return resString;
		
	}
	public static String GetCurrentExamInfo() throws Exception {
		// fetch grade of this semester
		String semString;
		if(date.getMonth() >=9 || date.getMonth() <2 )
		{
			semString = "秋";
		}
		else
		{
			semString = "春";
		}
		String cnt = whc.GetContent(header + "xsdhqt.aspx?dl=iconxsks");
		int y = Calendar.YEAR;
		whc.cleanPostContent();
		// fetch grade of next semester
		whc.AddToPostContent("__EVENTTARGET", "xqd");
		whc.AddToPostContent("__EVENTARGUMENT", "");
		whc.AddToPostContent("__VIEWSTATE", getViewState(cnt));
		whc.AddToPostContent(
				"xnd",
				"");//String.valueOf(y) + "-"
						//+ String.valueOf(y + 1));
		String nextxqd = "";

		if (semString.equals(Spring)) {
			nextxqd = "2|夏";
		} else if (semString.equals(Autumn)) {
			nextxqd = "1|冬";
		}
		/*
		 * */
		
		
		whc.AddToPostContent("xqd", nextxqd);
		cnt += whc.PostContent(header + "xskscx.aspx?xh="
				+ LoginActivity.studentId);
		
		return cnt;

	}

	public static String GetPageContent(String Url) throws Exception {
		String resString = whc.GetContent(Url);
		
		return resString;
	}

	public static ArrayList<Byte> fetchCheckCodeByteArray() throws Exception {
		return whc.DownloadByteArray(header + "CheckCode.aspx");
	}


	public static ArrayList<String> LiftTable(String content, boolean hrefFlag) {
		ArrayList<String> reslist = new ArrayList<String>();
		String res = null;
		String regex = "<table class=\"datagridstyle\" [^>]*>.*?</table>";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			res = matcher.group();
			res = res.replace("</a>", "");
			if (!hrefFlag)
				res = res.replaceAll("<a href=([^>]*)>", "<a>");
			else
				res = res
						.replaceAll(
								"<a href=\"#\" onclick=\"window.open\\('([^']*)'[^>]*>",
								"$1</td><td>");
			res = res.replace("<a>", "");
			res = res.replace("<tr class=\"datagrid1212\">", "<tr>");
			res = res.replace("\n", "");
			res = res.replace("\t", "");
			res = res.replace("\r", "");
			// res = res.replaceAll("\\s*", "");
			res = res.replace("<br>", "@");
			reslist.add(res);
			Log.d("TTTTNNNNNTTTT",res);
		}
		return reslist;
	}

	public static String FilterRawTable(String res) {
		res = res.replace("</a>", "");
		res = res.replaceAll("<a href=([^>]*)>", "<a>");
		res = res.replace("<a>", "");
		res = res.replace("<tr class=\"datagrid1212\">", "<tr>");
		res = res.replace("\n", "");
		res = res.replace("\t", "");
		res = res.replace("\r", "");
		res = res.replace("<br>", "@");
		return res;
	}

	public static ArrayList<String> LiftGradeTable(String content,
			boolean hrefFlag) {
		ArrayList<String> reslist = new ArrayList<String>();
		String res = null;
		String regex = "<table [^>]*>.*?</table>";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			res = matcher.group();
			res = res.replace("</a>", "");
			if (!hrefFlag)
				res = res.replaceAll("<a href=([^>]*)>", "<a>");
			else
				res = res
						.replaceAll(
								"<a href=\"#\" onclick=\"window.open\\('([^']*)'[^>]*>",
								"$1</td><td>");
			res = res.replace("<a>", "");
			res = res.replace("<tr class=\"datagrid1212\">", "<tr>");
			res = res.replace("\n", "");
			res = res.replace("\t", "");
			res = res.replace("\r", "");
			// res = res.replaceAll("\\s*", "");
			res = res.replace("<br>", "@");
			Log.d("TAGLIFTGRADTABLE","RES  "+res);
			reslist.add(res);
		}
		return reslist;
	}


	public static void main(String[] args) {
	}
}
