package de.tbosch.tools.googleapps.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.google.api.services.calendar.model.Event;

@Entity
@Table(name = "event", uniqueConstraints = @UniqueConstraint(columnNames = { "title", "startTime", "endTime" }))
public class GCalendarEvent implements Comparable<GCalendarEvent> {

	@Id
	@GeneratedValue
	private long id;

	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date startTime;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date endTime;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "event")
	@Column(name = "event")
	private Set<GReminder> reminders = new HashSet<GReminder>();

	public GCalendarEvent() {
	}

	public GCalendarEvent(Event event) {
		this.title = event.getSummary();
		this.startTime = new Date(event.getStart().getDateTime() != null ? event.getStart().getDateTime().getValue()
				: event.getStart().getDate().getValue());
		this.endTime = new Date(event.getEnd().getDateTime() != null ? event.getEnd().getDateTime().getValue() : event
				.getEnd().getDate().getValue());
	}

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the reminders
	 */
	public Set<GReminder> getReminders() {
		return reminders;
	}

	/**
	 * @param reminders
	 *            the reminders to set
	 */
	public void setReminders(Set<GReminder> reminders) {
		this.reminders = reminders;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return sdf.format(startTime) + " - " + sdf.format(endTime) + ": " + title;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GCalendarEvent o) {
		return this.getStartTime().compareTo(o.getStartTime());
	}

}