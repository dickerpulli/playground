package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.IFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.service.GCalendarService;

@Service
@Transactional
public class GCalendarServiceImpl implements GCalendarService {

	private final CalendarService calendarService;

	@Autowired
	public GCalendarServiceImpl(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	@Override
	public <F extends IFeed> F getFeed(Query query, Class<F> feedClass) throws IOException, ServiceException {
		return calendarService.getFeed(query, feedClass);
	}

	@Override
	public void setUserCredentials(String username, String password) throws AuthenticationException {
		calendarService.setUserCredentials(username, password);
	}

}
