package com.mymos.Entity;



/**
 * Processtype entity. @author MyEclipse Persistence Tools
 */
public class Processtype implements java.io.Serializable {

	// Fields

	private String id;
	private String name;

	// Constructors

	/** default constructor */
	public Processtype() {
	}

	/** minimal constructor */
	public Processtype(String id) {
		this.id = id;
	}

	/** full constructor */
	public Processtype(String id, String name) {
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