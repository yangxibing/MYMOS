package com.mymos.Entity;

import java.util.Date;


/**
 * Message entity. @author MyEclipse Persistence Tools
 */
public class Message implements java.io.Serializable {

	// Fields

	private String id;
	private String attachmentId;
	private String sender;
	private String receiver;
	private Date time;
	private String content;
	private String senderName;

	// Constructors

	/** default constructor */
	public Message() {
	}

	/** minimal constructor */
	public Message(String id) {
		this.id = id;
	}

	/** full constructor */
	public Message(String id, String attachmentId, String sender,
			String receiver, Date time, String content, String senderName) {
		this.id = id;
		this.attachmentId = attachmentId;
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
		this.content = content;
		this.senderName = senderName;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderName() {
		return this.senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

}