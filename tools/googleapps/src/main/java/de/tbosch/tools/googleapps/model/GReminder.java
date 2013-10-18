package de.tbosch.tools.googleapps.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.NotNull;

import com.google.gdata.data.extensions.Reminder;

@Entity
@Table(name = "reminder")
public class GReminder {

	@Id
	@GeneratedValue
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date time;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@NotNull
	private GCalendarEventEntry event;

	public GReminder() {
	}

	public GReminder(Reminder reminder, GCalendarEventEntry event) {
		this.event = event;
		if (reminder.getAbsoluteTime() != null) {
			this.time = new Date(reminder.getAbsoluteTime().getValue());
		}
		else if (reminder.getMinutes() != null) {
			this.time = DateUtils.addMinutes(event.getStartTime(), -1 * reminder.getMinutes());
		}
		else if (reminder.getHours() != null) {
			this.time = DateUtils.addHours(event.getStartTime(), -1 * reminder.getHours());
		}
		else if (reminder.getDays() != null) {
			this.time = DateUtils.addDays(event.getStartTime(), -1 * reminder.getDays());
		}
		else {
			throw new IllegalStateException("reminder must have a time");
		}
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the event
	 */
	public GCalendarEventEntry getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(GCalendarEventEntry event) {
		this.event = event;
	}

}
