package com.example.bmobexample.push;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class ActBmobPush extends BaseActivity {

	BmobPushManager bmobPush;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_push);
		
		BmobPush.startWork(this, APPID);	
		
		bmobPush = new BmobPushManager(this);
		
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, getResources().getStringArray(
						R.array.bmob_push_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				testBmob(position + 1);
			}
		});
		
		BmobInstallation.getCurrentInstallation(this).save();
	}
	
	private void testBmob(int pos) {
		switch (pos) {
			case 1:
				BmobInstallation installation = BmobInstallation.getCurrentInstallation(this);
				installation.subscribe("aaa");
				installation.subscribe("bbb");
				installation.save();
				break;
			case 2:
				BmobInstallation installation2 = BmobInstallation.getCurrentInstallation(this);
				installation2.unsubscribe("bbb");
				installation2.save();
				break;
			case 3:
				// �������ն�����
				pushMessage("���Ǹ������ն����͵�һ����Ϣ");
				break;
			case 4:
				// ��ĳ��Android�ն�����
				pushAndroidMessage("���Ǹ�ָ��Android�ն����͵�һ����Ϣ", "DBCF9F679D2F39E29D145F73A4145177");
				break;
			case 5:
				// ��ĳ��IOS�ն�����
				pushIOSMessage("���Ǹ�ָ��IOS�ն����͵�һ����Ϣ", "e2d4869619f61e0266561ce956e5d3cda153fef844242c6bf3f2c52d48fe98d4");
				break;
			case 6:
				// ��ĳĳ��������
				pushChannelMessage("���Ǹ�ָ���������͵�һ����Ϣ", "QQQ");
				break;
			case 7:
				// ������Ծ�û�������Ϣ
				pushToInactive("������Ծ�û����͵���Ϣ");
				break;
			case 8:
				pushToAndroid("��Androidƽ̨���͵���Ϣ");
				break;
			case 9:
				pushToIOS("��IOSƽ̨���͵���Ϣ");
				break;
			case 10:
				pushToGeoPoint("���ݵ�����Ϣλ�����͵���Ϣ");
				break;
		}
	}
	
	/**
	 * ��������������Ϣ
	 */
	private void pushMessage(String message){
//		bmobPush.pushMessage(message);
		bmobPush.pushMessageAll(message);
	}
	
	/**
	 * ��ָ��Android�û�������Ϣ
	 * @param message
	 * @param installId
	 */
	private void pushAndroidMessage(String message, String installId){
//		bmobPush.pushMessage(message, installId);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("installationId", installId);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	/**
	 * ��ָ��IOS�û�����
	 * @param message
	 * @param deviceToken
	 */
	private void pushIOSMessage(String message, String deviceToken){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("deviceToken", deviceToken);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	/**
	 * ��ָ������������Ϣ
	 * @param message
	 * @param channel
	 */
	private void pushChannelMessage(String message, String channel){
//		bmobPush.pushChannelMessage(message, channel);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		
//		JSONArray channels = new JSONArray();
//		channels.put(channel);
		List<String> channels = new ArrayList<String>();
		channels.add(channel);
		
		query.addWhereEqualTo("channels", channels);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	/**
	 * ������Ծ�û�������Ϣ
	 * @param message 
	 */
	private void pushToInactive(String message){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereLessThan("updatedAt", new BmobDate(new Date()));
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	/**
	 * ��androidƽ̨�ն�����
	 * @param message
	 */
	private void pushToAndroid(String message){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("deviceType", "android");
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	/**
	 * ��iosƽ̨�ն�����
	 * @param message
	 */
	private void pushToIOS(String message){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("deviceType", "ios");
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	/**
	 * ���ݵ�����Ϣλ��������
	 * @param message
	 */
	private void pushToGeoPoint(String message){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereWithinRadians("location", new BmobGeoPoint(112.934755, 24.52065), 1.0);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
}
