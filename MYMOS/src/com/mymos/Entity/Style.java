package com.mymos.Entity;


/**
 * Style entity. @author MyEclipse Persistence Tools
 */
public class Style implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String font;
	private Integer fontsize;
	private String color;

	// Constructors

	/** default constructor */
	public Style() {
	}

	/** full constructor */
	public Style(String id, String name, String font, Integer fontsize,
			String color) {
		this.id = id;
		this.name = name;
		this.font = font;
		this.fontsize = fontsize;
		this.color = color;
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

	public String getFont() {
		return this.font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public Integer getFontsize() {
		return this.fontsize;
	}

	public void setFontsize(Integer fontsize) {
		this.fontsize = fontsize;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}