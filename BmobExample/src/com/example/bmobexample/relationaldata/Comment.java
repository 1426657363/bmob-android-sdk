package com.example.bmobexample.relationaldata;

import com.example.bmobexample.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * 
 * @ClassName: Comment
 * @Description: ����ʵ��
 * @author Queen
 * @date 2014��4��17�� ����11:29:41
 *
 */
public class Comment extends BmobObject {
	private String content;
	private MyUser author;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MyUser getAuthor() {
		return author;
	}
	public void setAuthor(MyUser author) {
		this.author = author;
	}
}
