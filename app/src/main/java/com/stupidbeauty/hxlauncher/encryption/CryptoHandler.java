package com.stupidbeauty.hxlauncher.encryption;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.stupidbeauty.hxlauncher.R;
import com.stupidbeauty.hxlauncher.VoiceCommandHitDataMessageProtos;

//import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author jp.lin
 * @since Mar 13, 2014
 * 
 */
public class CryptoHandler extends Activity
{
	String SecretKey = "zhishiyiweiyaoeryiweishenmeyaohe";

	String IV = "nvxingsheanishiz";

	// ================================
	// final fileds
	// ================================
	private static final String TAG = "CryptoHandler"; //!<调试时使用的标记。

	private RequestQueue mQueue; //!<Volley请求队列。
	
	// ================================
	// View Componets
	// ================================
	private EditText mLoginCountText; //!<登录用户名输入框。
	private ProgressBar mProgressBar;
	private HashMap<String, Integer> countryCodeIndexMap=new HashMap<String,Integer>(); //!<国家代码与列表下标之间的映射。
	private String[] FacePrices; //!<国家名字列表。
// 	@Bind (R.id.welcome_logon) EditText welcome_logon; //!<用户名字输入框。
// 	@Bind (R.id.countrytextView1) EditText countrytextView1; //!<邮箱地址输入框。
//	@Bind (R.id.passwordcountrytextView1) EditText passwordcountrytextView1; //!<密码输入框。

	/**
     * 加密消息体。
	 * @param imageFileNameString 要加密的消息内容。
	 * @return 加密后的内容。
	 */
	public byte[] encrypt(byte[] imageFileNameString)  throws NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException,
			UnsupportedEncodingException, InvalidAlgorithmParameterException
	{


		byte[] srcBuff = imageFileNameString;
		//here using substring because AES takes only 16 or 24 or 32 byte of key
		SecretKeySpec skeySpec = new
				SecretKeySpec(SecretKey.substring(0,32).getBytes(), "AES");
		IvParameterSpec ivSpec = new
				IvParameterSpec(IV.substring(0,16).getBytes());
		Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
		byte[] dstBuff = ecipher.doFinal(srcBuff);

//		String encodedToHex= Hex.encodeHexString(dstBuff); //转换成十六进制，以便于阅读比较。
//		String originalToHex=Hex.encodeHexString(srcBuff); //转换成十六进制，以便于阅读比较。

//		Log.i(TAG, "encrypt, encoded: "+ encodedToHex + ", original content: " + originalToHex); //Debug.

		String base64 = Base64.encodeToString(dstBuff, Base64.DEFAULT);
		return dstBuff;
	} //public byte[] encrypt(byte[] imageFileNameString)

	/**
     * 构造消息体，注册信息。
	 * @param map 参数映射。
	 * @return 构造得到的消息体。
	 */
	public byte[] constructRegisterInformationMessage(Map<String, String> map)
	{
		byte[] result; //结果。

		VoiceCommandHitDataMessageProtos.VoiceCommandHitDataMessage.Builder translateRequestBuilder = VoiceCommandHitDataMessageProtos.VoiceCommandHitDataMessage.newBuilder(); //创建消息构造器。


		result=translateRequestBuilder.build().toByteArray();; //序列化成字节数组。


		return result;
	} //public byte[] constructRegisterInformationMessage(Map<String, String> map)

	/**
	 * 修改活动标题为登录。
	 */
	private void changeTitleLogin() 
	{
		getActionBar().setTitle(R.string.registerInfo); //将标题修改为注册信息。
	
		return;
	} //private void changeTitleLogin()

	/**
	 * 初始化各个视图。
	 */
	private void onViewInit() 
	{
		mLoginCountText = (EditText) findViewById(R.id.welcome_logon); //登录用户名输入框。

		mProgressBar = (ProgressBar) findViewById(R.id.login_progress); //进度条。

	} //private void onViewInit()


	/**
	 * 切换是否要显示登录进度条。
	 * @param show 是否要显示。
	 */
	private void showProgressBar(boolean show) 
	{
		if (show) //要显示。 
		{
			mProgressBar.setVisibility(View.VISIBLE); //显示进度条。
		} //if (show) //要显示。
		else //隐藏进度条。 
		{
			mProgressBar.setVisibility(View.GONE); //隐藏进度条。
		} //else //隐藏进度条。
		
		return;
	} //private void showProgressBar(boolean show)

} //public class SimoLoginActivity extends BaseActivity implements OnClickListener
