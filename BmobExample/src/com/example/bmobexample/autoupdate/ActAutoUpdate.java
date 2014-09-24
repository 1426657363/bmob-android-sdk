package com.example.bmobexample.autoupdate;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;

public class ActAutoUpdate extends BaseActivity {
	
	String [] arr = {"�Զ�����","�ֶ�����","��Ĭ���ظ���","ɾ���ļ�"};
	
	UpdateResponse ur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//������Ҫ�����Զ����¹���֮ǰ�Ƚ��г�ʼ���������
		BmobUpdateAgent.initAppVersion(this);
				
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.tv_item, arr);
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				testAutoUpdate(position + 1);
			}
		});
		
		BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
			
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				// TODO Auto-generated method stub
				if (updateStatus == UpdateStatus.Yes) {
					ur = updateInfo;
				}else if(updateStatus==UpdateStatus.IGNORED){//�������԰汾����
					Toast.makeText(ActAutoUpdate.this, "�ð汾�Ѿ������Ը���", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void testAutoUpdate(int pos){
		switch (pos) {
		case 1:
			BmobUpdateAgent.update(this);
			break;
		case 2:
			BmobUpdateAgent.forceUpdate(this);
			break;
		case 3:
			BmobUpdateAgent.silentUpdate(this);
			break;
		case 4:
			if(ur != null){
				File file = new File(Environment.getExternalStorageDirectory(), ur.path_md5 + ".apk");
				if (file != null) {
					if (file.delete()) {
						Toast.makeText(ActAutoUpdate.this, "ɾ�����",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ActAutoUpdate.this, "ɾ��ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ActAutoUpdate.this, "ɾ�����", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ActAutoUpdate.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
}
