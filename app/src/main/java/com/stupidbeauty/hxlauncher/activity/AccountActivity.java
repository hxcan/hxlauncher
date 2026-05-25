package com.stupidbeauty.hxlauncher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import com.stupidbeauty.hxlauncher.R;

public class AccountActivity extends Activity
{
	private static final String TAG = "AccountActivity"; //!<输出调试信息时使用的标记。
	private static final int TIMEOUT = 30000;
	private static final int SHOW_PROGRESS_DIALOG = 0;
	private static final int DISMISS_PROGRESS_DIALOG = 1;
	private static final int SHOW_CUSTOM_404_VIEW = 2;
	
	@BindView(R.id.load_progress) ProgressBar mProgressBar; //!<载入网页时的进度条。
	
	private boolean mIsRequestError = false;
		
	/**
	 * handler处理消息机制
	 */
	protected Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case SHOW_PROGRESS_DIALOG:
				mProgressBar.setVisibility(View.VISIBLE);
					
				break;
			case DISMISS_PROGRESS_DIALOG:
				mProgressBar.setVisibility(View.GONE);
				break;
			case SHOW_CUSTOM_404_VIEW:
				mProgressBar.setVisibility(View.GONE);
				mIsRequestError = true;
				
				break;
			}
		}
	};
	
	@BindView(R.id.forward_nav_imageView4) ImageView mForwardButton; //!<网页视图导航条的前进按钮。


	/**
	 * 跳转到登录界面。
	 */
	@OnClick(R.id.loginbutton)
	public void gotoLoginActivity()
	{
		Log.d(TAG, "gotoLoginActivity, 119."); //Debug.
//		Intent launchIntent=new Intent(this, EmailLoginActivity.class); //启动意图。
//		startActivity(launchIntent); //启动活动。

		Log.d(TAG, "gotoLoginActivity, 122."); //Debug.
	} //public void gotoLoginActivity()

	@Override
	/**
	 * 活动被创建。
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState); //创建超类。
		
		
		setContentView(R.layout.stupidbeauty_account_tab_web); //设置显示内容。

		ButterKnife.bind(this); //依赖注入。



		String url = ""; //账户URL。
		
	} //protected void onCreate(Bundle savedInstanceState)
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
}
