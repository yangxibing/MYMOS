package com.mymos.Entity;


/**
 * Template entity. @author MyEclipse Persistence Tools
 */
public class Template implements java.io.Serializable {

	// Fields

	private String id;
	private String docId;
	private String name;
	private String context;

	// Constructors

	/** default constructor */
	public Template() {
	}

	/** minimal constructor */
	public Template(String id, String docId) {
		this.id = id;
		this.docId = docId;
	}

	/** full constructor */
	public Template(String id, String docId, String name, String context) {
		this.id = id;
		this.docId = docId;
		this.name = name;
		this.context = context;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocId() {
		return this.docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

}