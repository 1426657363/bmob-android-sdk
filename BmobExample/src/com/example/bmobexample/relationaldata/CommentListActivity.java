package com.example.bmobexample.relationaldata;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.MyUser;
import com.example.bmobexample.R;

public class CommentListActivity extends BaseActivity {
	
	ListView listView;
	EditText et_content;
	Button btn_publish;
	
	static List<Comment> comments = new ArrayList<Comment>();
	MyAdapter adapter;
	Weibo weibo = new Weibo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		setTitle("�����б�");
		weibo.setObjectId(getIntent().getStringExtra("objectId"));
		
		adapter = new MyAdapter(this);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_publish = (Button) findViewById(R.id.btn_publish);
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		
		btn_publish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				publishComment(et_content.getText().toString());
			}
		});
		
		findComments();
	}
	
	private void findComments(){
		
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		// ��ѯ���΢������������,ע�⣺����ĵ�һ��������Weibo���е�comment�ֶ�
		query.addWhereRelatedTo("comment", new BmobPointer(weibo));		
		query.include("author");
		query.findObjects(this, new FindListener<Comment>() {
			
			@Override
			public void onSuccess(List<Comment> object) {
				// TODO Auto-generated method stub
				comments = object;
				adapter.notifyDataSetChanged();
				et_content.setText("");
			}
			
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("��ѯʧ��:"+msg);
			}
		});
	}
	
	private void publishComment(String content){
		MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
		if(user == null){
			toast("��������ǰ���ȵ�½");
			return;
		}else if(TextUtils.isEmpty(content)){
			toast("�������۲���Ϊ��");
			return;
		}
		
		final Comment comment = new Comment();
		comment.setContent(content);
//		comment.setPost(post);
		comment.setAuthor(user);
		comment.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				findComments();
				et_content.setText("");
				toast("���۳ɹ�");
				
				// ������ӳɹ��󽫸���������weibo����
				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				weibo.setComment(relation);
				weibo.update(CommentListActivity.this, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Log.d(TAG, "����΢����Ϣ�ɹ�");
					}
					
					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						Log.d(TAG, "����΢����Ϣʧ��:"+msg);
					}
				});
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("����ʧ��");
			}
		});
	}
	
	private static class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		
		private Context mContext;

		public MyAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
		}

		static class ViewHolder {
			TextView tv_content;
			TextView tv_author;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comments.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_weibo, null);

				holder = new ViewHolder();
				holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
				holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final Comment comment = comments.get(position);
			
			if(comment.getAuthor() != null){
				holder.tv_author.setText("�����ˣ�"+comment.getAuthor().getUsername());
			}
			
			final String str = comment.getContent();

			holder.tv_content.setText(str);

			return convertView;
		}
	}

}
