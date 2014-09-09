package com.mymos.Entity;


/**
 * Attachment entity. @author MyEclipse Persistence Tools
 */
public class Attachment implements java.io.Serializable {

	// Fields

	private String id;
	private String announcementId;
	private String url;
	private String fileType;

	// Constructors

	/** default constructor */
	public Attachment() {
	}

	/** minimal constructor */
	public Attachment(String id) {
		this.id = id;
	}

	/** full constructor */
	public Attachment(String id, String announcementId, String url,
			String fileType) {
		this.id = id;
		this.announcementId = announcementId;
		this.url = url;
		this.fileType = fileType;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnnouncementId() {
		return this.announcementId;
	}

	public void setAnnouncementId(String announcementId) {
		this.announcementId = announcementId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}