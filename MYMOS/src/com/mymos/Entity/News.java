package com.mymos.Entity;



/**
 * News entity. @author MyEclipse Persistence Tools
 */
public class News implements java.io.Serializable {

	// Fields

	private String id;
	private String newsId;
	private String title;
	private Integer attribute;
	private String content;
	private boolean isComment;
	private String imgUrl;
	private boolean isTop;

	// Constructors

	/** default constructor */
	public News() {
	}

	/** minimal constructor */
	public News(String id, String newsId) {
		this.id = id;
		this.newsId = newsId;
	}

	/** full constructor */
	public News(String id, String newsId, String title, Integer attribute,
			String content, boolean isComment, String imgUrl, boolean isTop) {
		this.id = id;
		this.newsId = newsId;
		this.title = title;
		this.attribute = attribute;
		this.content = content;
		this.isComment = isComment;
		this.imgUrl = imgUrl;
		this.isTop = isTop;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getIsComment() {
		return this.isComment;
	}

	public void setIsComment(boolean isComment) {
		this.isComment = isComment;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public boolean getIsTop() {
		return this.isTop;
	}

	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}

}