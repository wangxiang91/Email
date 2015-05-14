package com.wx.email;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View v = findViewById(R.id.send_email);
		v.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				new Thread(){
					public void run(){
						Mail m = new Mail()
						.setUser("你的邮箱账号")
						.setPwd("你的邮箱密码")
						.setHost("你的邮箱发送服务器地址")
						.setPort("端口号")
						.setSocketPort("socket端口号")
						.setSubject("邮件标题")
						.setBody("邮件正文")
						.setToUser(new String[]{"邮件接收者1","邮件接收者2"});
						try {
							m.addAttachment(new File(Environment.getExternalStorageDirectory(),"/DCIM/Camera/IMG_20150313_211205.jpg").toString());
							m.addAttachment(new File(Environment.getExternalStorageDirectory(),"/DCIM/Camera/IMG_20150313_211233.jpg").toString());
							if (m.send()) {
								Log.e(MainActivity.class.getSimpleName(),"Email was sent successfully.");
							} else {
								Log.e(MainActivity.class.getSimpleName(),"Email was not sent.");
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.e(MainActivity.class.getSimpleName(),"There was a problem sending the email.");
						}
					}
				}.start();
			}
		});
	}
}
