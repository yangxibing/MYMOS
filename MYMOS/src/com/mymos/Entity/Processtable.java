package com.mymos.Entity;


/**
 * Processtable entity. @author MyEclipse Persistence Tools
 */
public class Processtable implements java.io.Serializable {

	// Fields

	private String id;
	private String proTypeId;
	private String name;
	private String content;

	// Constructors

	/** default constructor */
	public Processtable() {
	}

	/** minimal constructor */
	public Processtable(String id, String proTypeId) {
		this.id = id;
		this.proTypeId = proTypeId;
	}

	/** full constructor */
	public Processtable(String id, String proTypeId, String name, String content) {
		this.id = id;
		this.proTypeId = proTypeId;
		this.name = name;
		this.content = content;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}