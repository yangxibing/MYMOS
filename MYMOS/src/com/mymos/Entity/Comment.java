package com.mymos.Entity;

import java.util.Date;

/**
 * Comment entity. @author MyEclipse Persistence Tools
 */
public class Comment implements java.io.Serializable {

	// Fields

	private String id;
	private String newsId;
	private Date time;
	private String content;
	private String authorId;

	// Constructors

	/** default constructor */
	public Comment() {
	}

	/** minimal constructor */
	public Comment(String id, String newsId) {
		this.id = id;
		this.newsId = newsId;
	}

	/** full constructor */
	public Comment(String id, String newsId, Date time, String content,
			String authorId) {
		this.id = id;
		this.newsId = newsId;
		this.time = time;
		this.content = content;
		this.authorId = authorId;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewsId() {
		return this.newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

}