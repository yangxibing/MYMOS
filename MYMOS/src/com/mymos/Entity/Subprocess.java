package com.mymos.Entity;


/**
 * Subprocess entity. @author MyEclipse Persistence Tools
 */
public class Subprocess implements java.io.Serializable {

	// Fields

	private String id;
	private String subProId;
	private String proTypeId;
	private String name;
	private boolean isCompleted;

	// Constructors

	/** default constructor */
	public Subprocess() {
	}

	/** minimal constructor */
	public Subprocess(String id, String proTypeId) {
		this.id = id;
		this.proTypeId = proTypeId;
	}

	/** full constructor */
	public Subprocess(String id, String subProId, String proTypeId,
			String name, boolean isCompleted) {
		this.id = id;
		this.subProId = subProId;
		this.proTypeId = proTypeId;
		this.name = name;
		this.isCompleted = isCompleted;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubProId() {
		return this.subProId;
	}

	public void setSubProId(String subProId) {
		this.subProId = subProId;
	}

	public String getProTypeId() {
		return this.proTypeId;
	}

	public void setProTypeId(String proTypeId) {
		this.proTypeId = proTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsCompleted() {
		return this.isCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

}