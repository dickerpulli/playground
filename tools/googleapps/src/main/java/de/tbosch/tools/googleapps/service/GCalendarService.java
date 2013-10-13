package de.tbosch.tools.googleapps.service;

import java.io.IOException;

import com.google.gdata.client.Query;
import com.google.gdata.data.IFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public interface GCalendarService {

	public <F extends IFeed> F getFeed(Query query, Class<F> feedClass) throws IOException, ServiceException;

	public void setUserCredentials(String username, String password) throws AuthenticationException;

}
