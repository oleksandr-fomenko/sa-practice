package com.epam.data.handler.config;

import com.epam.data.handler.controller.ProductController;
import com.fasterxml.jackson.databind.Module;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.form.spring.converter.SpringManyMultipartFilesReader;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gcp.autoconfigure.datastore.DatastoreProvider;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTransactionManager;
import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreAuditing;
import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableFeignClients
@EnableDatastoreAuditing
@SpringBootApplication
@EnableWebSecurity
@ComponentScan("com.epam.data.handler")
@EntityScan("com.epam.data.handler.entities")
@EnableTransactionManagement
@EnableDatastoreRepositories("com.epam.data.handler.repositories")
public class SpringConfig extends WebSecurityConfigurerAdapter {

	@Value("${feign.url}")
	private String feignUrl;

	private static final String ROLE = "ADMIN";

	@Value("${admin.login}")
	private String login;
	@Value("${admin.password}")
	private String password;

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser(login).password(encoder().encode(password)).roles(ROLE);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.and().exceptionHandling()
				.and().authorizeRequests()
				.antMatchers("/**").authenticated()
				.antMatchers("/**").hasRole(ROLE)
				.and().formLogin()
				.successHandler(successHandler)
				.failureHandler(new SimpleUrlAuthenticationFailureHandler())
				.and().httpBasic()
				.and().logout();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ObjectFactory<HttpMessageConverters> objectFactory() {
		return () -> new HttpMessageConverters(
				new FormHttpMessageConverter(),
				new MappingJackson2HttpMessageConverter(),
				new SourceHttpMessageConverter(),
				new ResourceHttpMessageConverter(),
				new SpringManyMultipartFilesReader(4096),
				new StringHttpMessageConverter()
		);
	}

	@Bean
	public Decoder feignDecoder(@Autowired ObjectFactory<HttpMessageConverters> messageConverters) {
		return new ResponseEntityDecoder(new SpringDecoder(messageConverters));
	}

	@Bean
	public Encoder feignEncoder(@Autowired ObjectFactory<HttpMessageConverters> messageConverters) {
		return new FormEncoder(new SpringEncoder(messageConverters));
	}

	@Bean
	public Contract.BaseContract baseContract() {
		return new SpringMvcContract();
	}

	@Bean
	public Module pageJacksonModule() {
		return new PageJacksonModule();
	}

	@Bean
	public Module sortJacksonModule() {
		return new SortJacksonModule();
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ProductController productControllerFeign(@Autowired Encoder encoder, @Autowired Decoder decoder,
													@Autowired Contract.BaseContract baseContract) {
		return Feign.builder()
				.contract(baseContract)
				.requestInterceptor(rt -> new BasicAuthRequestInterceptor(login, password).apply(rt))
				.encoder(encoder)
				.decoder(decoder)
				.logLevel(Logger.Level.BASIC)
				.decode404()
				.target(ProductController.class, feignUrl);
	}

	@Bean
	public DatastoreTransactionManager datastoreTransactionManager(DatastoreProvider datastore) {
		return new DatastoreTransactionManager(datastore);
	}

}
