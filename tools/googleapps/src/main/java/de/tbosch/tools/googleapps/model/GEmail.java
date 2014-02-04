package de.tbosch.tools.googleapps.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "email")
public class GEmail implements Comparable<GEmail> {

	@Id
	@GeneratedValue
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDate;

	private String content;

	private String subject;

	@NotEmpty
	private String fromAddress;

	private String fromName;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the sentDate
	 */
	public Date getSentDate() {
		return sentDate;
	}

	/**
	 * @param sentDate
	 *            the sentDate to set
	 */
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = StringUtils.abbreviate(content, 255);
	}

	/**
	 * @return the fromAddress
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * @param fromAddress
	 *            the fromAddress to set
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName
	 *            the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return sdf.format(sentDate) + " - " + (fromName != null ? fromName : fromAddress) + " - " + subject;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GEmail o) {
		return sentDate.compareTo(o.getSentDate());
	}

}
