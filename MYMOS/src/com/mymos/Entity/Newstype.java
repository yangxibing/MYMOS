package com.mymos.Entity;


/**
 * Newstype entity. @author MyEclipse Persistence Tools
 */
public class Newstype implements java.io.Serializable {

	// Fields

	private String id;
	private Integer sortNumber;
	private String name;

	// Constructors

	/** default constructor */
	public Newstype() {
	}

	/** minimal constructor */
	public Newstype(String id) {
		this.id = id;
	}

	/** full constructor */
	public Newstype(String id, Integer sortNumber, String name) {
		this.id = id;
		this.sortNumber = sortNumber;
		this.name = name;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSortNumber() {
		return this.sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}