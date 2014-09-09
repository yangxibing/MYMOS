package com.mymos.Entity;

import java.util.Date;

/**
 * Document entity. @author MyEclipse Persistence Tools
 */
public class Document implements java.io.Serializable {

	// Fields

	private String id;
	private String docId;
	private String attachmentId;
	private Date modifyTime;
	private String catlog;
	private String name;
	private String content;
	private String appendix;
	private String folder;
	private String authorId;

	// Constructors

	/** default constructor */
	public Document() {
	}

	/** minimal constructor */
	public Document(String id, String docId) {
		this.id = id;
		this.docId = docId;
	}

	/** full constructor */
	public Document(String id, String docId, String attachmentId,
			Date modifyTime, String catlog, String name, String content,
			String appendix, String folder, String authorId) {
		this.id = id;
		this.docId = docId;
		this.attachmentId = attachmentId;
		this.modifyTime = modifyTime;
		this.catlog = catlog;
		this.name = name;
		this.content = content;
		this.appendix = appendix;
		this.folder = folder;
		this.authorId = authorId;
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

	public String getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCatlog() {
		return this.catlog;
	}

	public void setCatlog(String catlog) {
		this.catlog = catlog;
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

	public String getAppendix() {
		return this.appendix;
	}

	public void setAppendix(String appendix) {
		this.appendix = appendix;
	}

	public String getFolder() {
		return this.folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

}