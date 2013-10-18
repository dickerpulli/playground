/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tbosch.commons.database;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.DatabaseManager;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerConfiguration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Bean that will start an instance of an HSQL database. This class is primarily intended to be used in demo
 * applications. It allows for a self contained distribution including a database instance.
 * 
 * This is an example of a bean configuration:
 * 
 * <pre>
 *     &lt;bean id="dataBase" class="spring.kickstart.db.hsqldb.ServerBean" scope="singleton" lazy-init="false"&gt;
 *         &lt;property name="serverProperties"&gt;
 *             &lt;props&gt;
 *                 &lt;prop key="server.port"&gt;9101&lt;/prop&gt;
 *                 &lt;prop key="server.database.0"&gt;webapps/myapp/db/test&lt;/prop&gt;
 *                 &lt;prop key="server.dbname.0"&gt;test&lt;/prop&gt;
 *             &lt;/props&gt;
 *         &lt;/property&gt;
 *     &lt;/bean&gt;
 * </pre>
 * 
 * 
 * @author Thomas Risberg
 * @see org.hsqldb.Server
 */
public class HsqlServerBean implements InitializingBean, DisposableBean {

	private static final Log LOG = LogFactory.getLog(HsqlServerBean.class);

	private Properties serverProperties;

	private Server server;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		HsqlProperties configProps = new HsqlProperties(serverProperties);

		ServerConfiguration.translateDefaultDatabaseProperty(configProps);

		// finished setting up properties - set some important behaviors as well;
		server = new Server();
		server.setRestartOnShutdown(false);
		server.setNoSystemExit(true);
		server.setProperties(configProps);
		if (LOG.isInfoEnabled()) {
			LOG.info("HSQL Server Startup sequence initiated");
		}

		server.start();

		String portMsg = "port " + server.getPort();
		if (LOG.isInfoEnabled()) {
			LOG.info("HSQL Server listening on " + portMsg);
		}
	}

	/**
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() {
		// Do what it takes to shutdown -- this might need to be enhanced in the future
		if (LOG.isInfoEnabled()) {
			LOG.info("HSQL Server Shutdown sequence initiated");
		}
		server.signalCloseAllServerConnections();
		server.stop();
		DatabaseManager.closeDatabases(0);
		if (LOG.isInfoEnabled()) {
			LOG.info("HSQL Server Shutdown completed");
		}
		server = null;
	}

	/**
	 * @param serverProperties the serverProperties to set
	 */
	public void setServerProperties(Properties serverProperties) {
		this.serverProperties = serverProperties;
	}

}