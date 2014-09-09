package com.mymos.Entity;


/**
 * Contacts entity. @author MyEclipse Persistence Tools
 */
public class Contacts implements java.io.Serializable {

	// Fields

	private String id;
	private String groupId;
	private String userId;

	// Constructors

	/** default constructor */
	public Contacts() {
	}

	/** minimal constructor */
	public Contacts(String id, String groupId) {
		this.id = id;
		this.groupId = groupId;
	}

	/** full constructor */
	public Contacts(String id, String groupId, String userId) {
		this.id = id;
		this.groupId = groupId;
		this.userId = userId;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}