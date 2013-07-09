package com.example.jwwapp2;


import java.util.ArrayList;

import com.example.jwwapp2.tool.GenericTask;
import com.example.jwwapp2.tool.JwwInfoFetcher;
import com.example.jwwapp2.tool.TaskAdapter;
import com.example.jwwapp2.tool.TaskListener;
import com.example.jwwapp2.tool.TaskParams;
import com.example.jwwapp2.tool.TaskResult;
import com.example.jwwapp2.tool.VerifyBreaker;

import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Bitmap _checkCodeBitmap;
	private ImageView _checkcodeImg;
	private EditText _CheckcodeEdit;
	private EditText _UsernameEdit;
	private EditText _PasswordEdit;
	private String _Checkcode;
	private CheckBox _rememberPasswordBox;
	private TextView _checkCodeLabel;
	private CheckBox _autoCheckCodeBox;
	private String _Password;
	private String _Username;
	private TextView mTitleView;
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	public static final String REMEMBERPWD = "REMEMBERPWD";
	public static String studentId;
	private GenericTask _LoginTask;
	private SharedPreferences setting;
	private boolean hasNetwork = false;
	private Button _SigninButton;
	Intent intent = new Intent();
	Dialog dialog;
	private Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				_checkcodeImg.setImageBitmap(_checkCodeBitmap);
				_CheckcodeEdit.setText(_autoCheckCodeString);
				break;
			case 1:
				dialog.dismiss();
				onMsg("联网失败，无法获得验证码");
				break;
			}
			super.handleMessage(msg);
		}

	};
	private String _autoCheckCodeString = "";
	Thread _getCheckCodeThread = new Thread() {

		@Override
		public void run() {
			ArrayList<Byte> al = null;
			try {
				al = JwwInfoFetcher.fetchCheckCodeByteArray();
				byte b[] = new byte[al.size()];
				for (int i = 0; i < al.size(); i++)
					b[i] = al.get(i);
				_checkCodeBitmap = Bitmap.createScaledBitmap(BitmapFactory
						.decodeByteArray(b, 0, b.length), 300, 100, false);
				_autoCheckCodeString = VerifyBreaker.GetImgNum(al);
				handle.sendEmptyMessage(0);
			} catch (Exception e) {
				if (al == null)
					handle.sendEmptyMessage(1);
				e.printStackTrace();
			}
		}
	};
	private void onMsg(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	private void findViews() {
		
		_UsernameEdit = (EditText) findViewById(R.id.username_edit);
		_PasswordEdit = (EditText) findViewById(R.id.password_edit);
		_SigninButton = (Button) findViewById(R.id.signin_button);
		_SigninButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogin();
			}
		});
		_rememberPasswordBox = (CheckBox) findViewById(R.id.rememberBox);
		if (_networkOption == 2) {
			_CheckcodeEdit.setVisibility(View.GONE);
			_checkcodeImg.setVisibility(View.GONE);
			_checkCodeLabel.setVisibility(View.GONE);
			_autoCheckCodeBox.setVisibility(View.GONE);
		}
	}

	private void checkNetworkState() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (wifi == State.CONNECTED || mobile == State.CONNECTED) {
			//有网络
			this.hasNetwork = true;
			
		}
		else {
			onMsg("请检查网络连接!");
		}
	}

	private TaskListener mLoginTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			onLoginBegin();
		}


		
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.LOGIN_SUCCESS) {
				onLoginSuccess();
			} else if (result == TaskResult.LOGIN_CHECKCODE_INCORRECT) {
				onLoginFailure("验证码不正确");
			} else if (result == TaskResult.LOGIN_PASSWORD_INCORRECT) {
				onLoginFailure("密码错误");
			} else if (result == TaskResult.LOGIN_USERNAME_NOT_EXSIT) {
				onLoginFailure("用户名不存在");
			} else if (result == TaskResult.LOGIN_SERVER_INTERNAL_ERROR) {
				onLoginFailure("服务器状态异常");
			} else if (result == TaskResult.LOGIN_NETWORK_ERROR) {
				onLoginFailure("联网错误");
			}
			else {
				onLoginFailure("未知网络错误");
			}
		}

		public String getName() {
			return "LoginTask";
		}
	};
	private void onLoginFailure(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	private void onLoginBegin() {
		dialog = ProgressDialog.show(LoginActivity.this, "", "正在登陆", true);
		dialog.setCancelable(true);
	}

	private void doLogin() {
			
		_Username = _UsernameEdit.getText().toString();
		_Password = _PasswordEdit.getText().toString();
		if (_LoginTask != null
				&& _LoginTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			if (!(_Username.equals("")) & !(_Password.equals(""))) {
				_LoginTask = new LoginTask();
				_LoginTask.setListener(mLoginTaskListener);

				TaskParams params = new TaskParams();
				params.put("username", _Username);
				params.put("password", _Password);
				params.put("checkcode", _Checkcode);
				_LoginTask.execute(params);
			} else {
				onLoginFailure("学号与密码不能为空！");
			}
		}
	}

	private class LoginTask extends GenericTask {

	
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];

			try {
				String username = param.getString("username");
				String password = param.getString("password");
				String checkcode = param.getString("checkcode");
				//先确保联网
				if (hasNetwork)
					return JwwInfoFetcher.LoginJww(username, password,
							checkcode);
				else {
					
					SharedPreferences setting = getSharedPreferences(USERINFO,0);
					if (username.equals(setting.getString(USERNAME, ""))
						&& password.equals(setting.getString(PASSWORD, "")))
					return TaskResult.LOGIN_SUCCESS;
					else
					return TaskResult.LOGIN_PASSWORD_INCORRECT;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return TaskResult.LOGIN_NETWORK_ERROR;
			}
		}
	}

	private void onLoginSuccess() {
		dialog.dismiss();
		Editor editor = getSharedPreferences(USERINFO, 0).edit();
		if (_rememberPasswordBox.isChecked()) {

			editor.putString(USERNAME, _Username)
					.putString(PASSWORD, _Password).putBoolean(REMEMBERPWD,
							true);

		} else {
			editor.putString(USERNAME, "").putString(PASSWORD, "").putBoolean(
					REMEMBERPWD, false);
		}
		editor.putBoolean(FIRST_LOGIN, false);
		editor.commit();
		Toast.makeText(this, "登陆成功", Toast.LENGTH_LONG)
				.show();
		studentId = _Username;
		Bundle bundle = new Bundle();
		bundle.putString("_id", _Username);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		finish();
	}
	private int _networkOption = 0;
	private final static String FIRST_LOGIN = "FIRST_LOGIN";
    @Override
    
    
    
    
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去除
        super.onCreate(savedInstanceState);
        setting = getSharedPreferences(USERINFO, 0);
        setContentView(R.layout.activity_login);
        mTitleView = (TextView) findViewById(R.id.title_text);
		mTitleView.setText(R.string.login);
		findViews();
		checkNetworkState();
		
		if (setting.getBoolean(REMEMBERPWD, false)) {
			_UsernameEdit.setText(setting.getString(USERNAME, ""));
			_PasswordEdit.setText(setting.getString(PASSWORD, ""));
			_rememberPasswordBox.setChecked(true);
		}
		
		intent.setClass(LoginActivity.this, MainActivity.class);
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
}
