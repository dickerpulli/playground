package de.tbosch.web.springboot;

import java.io.FileNotFoundException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableScheduling
@PropertySource("classpath:environment.properties")
public class ApplicationConfiguration {

	@Value("${keystore.file}")
	private String keystoreFile;

	@Value("${keystore.pass}")
	private String keystorePass;

	@Value("${server.port}")
	private String serverPort;

	@Autowired
	private Environment env;

	@Profile("default")
	@Bean
	public String bean1() {
		System.out.println("profile:default");
		return "";
	}

	@Profile("test")
	@Bean
	public String bean2() {
		System.out.println("profile:test");
		return "";
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() throws FileNotFoundException {
		final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();

		// Enable SSL per HTTPS

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer factory) {
				if (env.getProperty("tomcat.https", Boolean.class, false)) {
					if (factory instanceof TomcatEmbeddedServletContainerFactory) {
						TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
						containerFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
							@Override
							public void customize(Connector connector) {
								connector.setPort(env.getProperty("server.port", Integer.class));
								connector.setSecure(true);
								connector.setScheme("https");
								Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
								proto.setSSLEnabled(true);
								proto.setKeystoreFile(absoluteKeystoreFile);
								proto.setKeystorePass(keystorePass);
								proto.setKeystoreType("PKCS12");
								proto.setKeyAlias("tomcat");
							}
						});
					}
				}
			}
		};
	}

}
