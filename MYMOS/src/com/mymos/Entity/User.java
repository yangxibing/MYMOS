package com.mymos.Entity;

import java.util.Date;


/**
 * User entity. @author MyEclipse Persistence Tools
 */
public class User implements java.io.Serializable {

	// Fields

	private String id;
	private String lastName;
	private String firstName;
	private String password;
	private String phoneNumber;
	private String email;
	private Integer priority;
	private Date birthday;
	private String city;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(String id, String lastName, String firstName,
			String password, String phoneNumber, String email,
			Integer priority, Date birthday, String city) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.priority = priority;
		this.birthday = birthday;
		this.city = city;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}