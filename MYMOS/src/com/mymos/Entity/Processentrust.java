package com.mymos.Entity;


/**
 * Processentrust entity. @author MyEclipse Persistence Tools
 */
public class Processentrust implements java.io.Serializable {

	// Fields

	private String id;
	private String processId;
	private String subProId;
	private String userId;

	// Constructors

	/** default constructor */
	public Processentrust() {
	}

	/** minimal constructor */
	public Processentrust(String id) {
		this.id = id;
	}

	/** full constructor */
	public Processentrust(String id, String processId, String subProId,
			String userId) {
		this.id = id;
		this.processId = processId;
		this.subProId = subProId;
		this.userId = userId;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getSubProId() {
		return this.subProId;
	}

	public void setSubProId(String subProId) {
		this.subProId = subProId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}