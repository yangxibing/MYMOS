package com.mymos.Entity;


/**
 * Grouping entity. @author MyEclipse Persistence Tools
 */
public class Grouping implements java.io.Serializable {

	// Fields

	private String id;
	private String name;

	// Constructors

	/** default constructor */
	public Grouping() {
	}

	/** minimal constructor */
	public Grouping(String id) {
		this.id = id;
	}

	/** full constructor */
	public Grouping(String id, String name) {
		this.id = id;
		this.name = name;
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

}