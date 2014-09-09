package com.mymos.Entity;


/**
 * Process entity. @author MyEclipse Persistence Tools
 */
public class Process implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private Integer completeSchedule;
	private String typeId;
	private String userId;

	// Constructors

	/** default constructor */
	public Process() {
	}

	/** minimal constructor */
	public Process(String id) {
		this.id = id;
	}

	/** full constructor */
	public Process(String id, String name, Integer completeSchedule,
			String typeId, String userId) {
		this.id = id;
		this.name = name;
		this.completeSchedule = completeSchedule;
		this.typeId = typeId;
		this.userId = userId;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCompleteSchedule() {
		return this.completeSchedule;
	}

	public void setCompleteSchedule(Integer completeSchedule) {
		this.completeSchedule = completeSchedule;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}