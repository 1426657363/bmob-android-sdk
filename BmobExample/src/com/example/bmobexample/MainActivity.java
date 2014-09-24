package com.example.bmobexample;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.example.bmobexample.autoupdate.ActAutoUpdate;
import com.example.bmobexample.push.ActBmobPush;
import com.example.bmobexample.relationaldata.WeiboListActivity;

public class MainActivity extends BaseActivity {
	
	protected ListView mListview;
	protected BaseAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toast.makeText(this, "��ǵý�BaseActivity���е�APPID�滻Ϊ���appid", Toast.LENGTH_LONG).show();
		
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.tv_item, getResources().getStringArray(
				R.array.bmob_test_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				testBmob(position + 1);
			}
		});
		
		ChangeLogDialog changeLogDialog = new ChangeLogDialog(this);
		changeLogDialog.show();
	}
	
	private void testBmob(int pos) {
		switch (pos) {
			case 1:
				testinsertObject();
				break;
			case 2:
				testUpdateObjet();
				break;
			case 3:
				testDeleteObject();
				break;
			case 4:
				startActivity(new Intent(MainActivity.this, FindActivity.class));
				break;
			case 5:
				testSignUp();
				break;
			case 6:
				testLogin();
				break;
			case 7:
				testGetCurrentUser();
				break;
			case 8:
				testLogOut();
				break;
			case 9:
				updateUser();
				break;
			case 10:
				testResetPasswrod();
				break;
			case 11:
				emailVerify();
				break;
			case 12:
				testFindBmobUser();
				break;
			case 13:
				getServerTime();
				break;
			case 14:
				makeUpLoadFileRequest();
				break;
			case 15:
				cloudCode();
				break;
			case 16:
				// ��������
				startActivity(new Intent(this, WeiboListActivity.class));
				break;
			case 17:
				// ��������
				startActivity(new Intent(this, BatchActionActivity.class));
				break;
			case 18:
				// ���ͷ���
				startActivity(new Intent(this, ActBmobPush.class));
				break;
			case 19:
				// Ӧ���Զ�����
				startActivity(new Intent(this, ActAutoUpdate.class));
				break;
		}
	}
	
	/**
	 * �ļ��ϴ�
	 */
	private void makeUpLoadFileRequest() {
		String filePath = "sdcard/temp.jpg";
		final BmobFile bmobFile = new BmobFile(new File(filePath));
		bmobFile.upload(this, new UploadFileListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("�ϴ��ļ��ɹ�:"+bmobFile.getFileUrl());
				
				final Person p2 = new Person();
				p2.setName("�����P");
				p2.setAddress("����3��");
				p2.setPic(bmobFile);
				p2.setGpsAdd(new BmobGeoPoint(112.934755,24.52065));
				p2.save(MainActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						toast("�������ݳɹ���"+p2.getObjectId());
						Log.d("bmob","objectId = "+p2.getObjectId());
						Log.d("bmob","name ="+p2.getName());
						Log.d("bmob","age ="+p2.getAge());
						Log.d("bmob","address ="+p2.getAddress());
						Log.d("bmob","gender ="+p2.isGender());
						Log.d("bmob","createAt = "+p2.getCreatedAt());
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						toast("��������ʧ�ܣ�"+msg);
					}
				});
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("�ϴ��ļ�ʧ�ܣ�"+msg);
			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub
				// �ϴ����� arg0
			}
		});
	}
	
	
	/**
	 * �������
	 */
	private void testinsertObject(){
		final Person p2 = new Person();
		p2.setName("lucky");
		p2.setAddress("�����к�����");
		p2.setGpsAdd(new BmobGeoPoint(112.934755,24.52065));
		p2.setUploadTime(new BmobDate(new Date()));
		p2.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("�������ݳɹ���"+p2.getObjectId());
				Log.d("bmob","objectId = "+p2.getObjectId());
				Log.d("bmob","name ="+p2.getName());
				Log.d("bmob","age ="+p2.getAge());
				Log.d("bmob","address ="+p2.getAddress());
				Log.d("bmob","gender ="+p2.isGender());
				Log.d("bmob","createAt = "+p2.getCreatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("��������ʧ�ܣ�"+msg);
			}
		});
	}
	
	/**
	 * ���¶���
	 */
	private void testUpdateObjet(){
		final Person p2 = new Person();
		p2.setAddress("�����г�����12");
		p2.update(this, "05c753038f", new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("���³ɹ���"+p2.getUpdatedAt());
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("����ʧ�ܣ�"+msg);
			}
		});
		
	}
	
	/**
	 * ɾ������
	 */
	private void testDeleteObject(){
		Person p2 = new Person();
		p2.setObjectId("3a1c37451c");
		p2.delete(this, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("ɾ���ɹ�");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("ɾ��ʧ�ܣ�"+msg);
			}
		});
	}
	
	
	/**
	 * ע���û�
	 */
	private void testSignUp(){
		final MyUser myUser = new MyUser();
		myUser.setUsername("lucky3");
		myUser.setPassword("123456");
		myUser.setEmail("lucky@qq.com");
		myUser.setAge(23);
		myUser.setGender(false);
		myUser.signUp(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("ע��ɹ�:" + myUser.getUsername() + "-" + myUser.getObjectId()
						+ "-" + myUser.getCreatedAt() + "-"
						+ myUser.getSessionToken());
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("ע��ʧ��:" + msg);
			}
		});
	}
	
	/**
	 * ��½�û�
	 */
	private void testLogin(){
		
		final BmobUser bu2 = new BmobUser();
		bu2.setUsername("lucky3");
		bu2.setPassword("123456");
		bu2.login(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("��½�ɹ�:"+bu2.getSessionToken());
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("��½ʧ��:"+msg);
			}
		});
	}
	
	/**
	 * ��ȡ�����û�
	 */
	private void testGetCurrentUser() {
//		BmobUser bmobUser = BmobUser.getCurrentUser(this);
//		if(bmobUser != null){
//			toast("�����û���Ϣ" + bmobUser.getObjectId() + "-"
//					+ bmobUser.getUsername() + "-"
//					+ bmobUser.getSessionToken() + "-"
//					+ bmobUser.getCreatedAt() + "-"
//					+ bmobUser.getUpdatedAt());
//		}else{
//			toast("�����û�Ϊnull,���¼��");
//		}
		
		MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
		if(myUser != null){
			toast("�����û���Ϣ" + myUser.getObjectId() + "-"
					+ myUser.getUsername() + "-"
					+ myUser.getSessionToken() + "-"
					+ myUser.getCreatedAt() + "-"
					+ myUser.getUpdatedAt() + "-"
					+ myUser.getAge() + "-"
					+ myUser.getGender());
		}else{
			toast("�����û�Ϊnull,���¼��");
		}
		
	}
	
	/**
	 * ��������û�
	 */
	private void testLogOut(){
		BmobUser.logOut(this);
	}
	
	/**
	 * �����û�
	 */
	private void updateUser(){
		
		final BmobUser bmobUser = BmobUser.getCurrentUser(this, BmobUser.class);
		if(bmobUser != null){
			Log.d("bmob","getObjectId = "+bmobUser.getObjectId());
			Log.d("bmob","getUsername = "+bmobUser.getUsername());
			Log.d("bmob","getPassword = "+bmobUser.getPassword());
			Log.d("bmob","getEmail = "+bmobUser.getEmail());
			Log.d("bmob","getCreatedAt = "+bmobUser.getCreatedAt());
			Log.d("bmob","getUpdatedAt = "+bmobUser.getUpdatedAt());
			bmobUser.setEmail("xxxoo223o@163.com");
			bmobUser.update(this, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					toast("�����û���Ϣ�ɹ�:"+bmobUser.getUpdatedAt());
				}
				
				@Override
				public void onFailure(int code, String msg) {
					// TODO Auto-generated method stub
					toast("�����û���Ϣʧ��:"+msg);
				}
			});
		}else{
			toast("�����û�Ϊnull,���¼��");
		}
	}
	
	/**
	 * ��������
	 */
	private void testResetPasswrod(){
		final String email = "xxx@163.com";
		BmobUser.resetPassword(this, email, new ResetPasswordListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("������������ɹ����뵽"+email+"��������������ò���");
			}
			
			@Override
			public void onFailure(int code, String e) {
				// TODO Auto-generated method stub
				toast("��������ʧ��:"+e);
			}
		});
	}
	
	/**
	 * ��ѯ�û�
	 */
	private void testFindBmobUser(){
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo("username", "lucky");
		query.findObjects(this, new FindListener<MyUser>() {
			
			@Override
			public void onSuccess(List<MyUser> object) {
				// TODO Auto-generated method stub
				toast("��ѯ�û��ɹ���"+object.size());
				
			}
			
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("��ѯ�û�ʧ�ܣ�"+msg);
			}
		});
	}
	
	/**
	 * ��֤�ʼ�
	 */
	private void emailVerify(){
		final String email = "75727433@qq.com";
		BmobUser.requestEmailVerify(this, email, new EmailVerifyListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("������֤�ʼ��ɹ����뵽" + email + "�����н��м����˻���");
			}
			
			@Override
			public void onFailure(int code, String e) {
				// TODO Auto-generated method stub
				toast("������֤�ʼ�ʧ��:" + e);
			}
		});
	}
	
	/**
	 * ��ȡ������ʱ��
	 */
	private void getServerTime(){
		Bmob.getServerTime(MainActivity.this, new GetServerTimeListener() {
			
			@Override
			public void onSuccess(long time) {
				// TODO Auto-generated method stub
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				String times = formatter.format(new Date(time * 1000L));
				toast("��ǰ������ʱ��Ϊ:" + times);
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("��ȡ������ʱ��ʧ��:" + msg);
			}
		});
	}
	
	/**
	 * �ƶ˴���
	 */
	private void cloudCode(){
		AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
		ace.callEndpoint(MainActivity.this, "usertest",
				new CloudCodeListener() {

					@Override
					public void onSuccess(Object object) {
						// TODO Auto-generated method stub
						toast("�ƶ�usertest��������:" + object.toString());
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						toast("�����ƶ�usertest����ʧ��:" + msg);
					}
		});
	}

}
