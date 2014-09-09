package com.mymos.Entity;

import java.util.Date;


/**
 * Announcement entity. @author MyEclipse Persistence Tools
 */
public class Announcement implements java.io.Serializable {

	// Fields

	private String id;
	private String title;
	private String content;
	private Integer attribute;
	private Date activeDate;
	private Date endDate;
	private boolean isAddAccessory;
	private Integer readCount;
	private String authorId;

	// Constructors

	/** default constructor */
	public Announcement() {
	}

	/** minimal constructor */
	public Announcement(String id) {
		this.id = id;
	}

	/** full constructor */
	public Announcement(String id, String title, String content,
			Integer attribute, Date activeDate, Date endDate,
			boolean isAddAccessory, Integer readCount, String authorId) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.attribute = attribute;
		this.activeDate = activeDate;
		this.endDate = endDate;
		this.isAddAccessory = isAddAccessory;
		this.readCount = readCount;
		this.authorId = authorId;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public Date getActiveDate() {
		return this.activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean getIsAddAccessory() {
		return this.isAddAccessory;
	}

	public void setIsAddAccessory(boolean isAddAccessory) {
		this.isAddAccessory = isAddAccessory;
	}

	public Integer getReadCount() {
		return this.readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public String getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

}