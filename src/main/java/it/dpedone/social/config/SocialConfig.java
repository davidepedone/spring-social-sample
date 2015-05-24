package it.dpedone.social.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SocialConfig {

	@Value("${spring.social.facebook.appId}")
	private String appId;
	@Value("${spring.social.facebook.appSecret}")
	private String appSecret;
	
	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
	    ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
	    registry.addConnectionFactory(new FacebookConnectionFactory(appId,appSecret));
	    return registry;
	}
    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }
    
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        System.out.println("Username: "+authentication.getName());
        return usersConnectionRepository().createConnectionRepository(authentication.getName());
    }
    
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
    }

    @Inject
    private DataSource dataSource;

    @Inject
    private TextEncryptor textEncryptor;

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository connectionRepository, ConnectionFactoryLocator connectionFactoryLocator) throws Exception{
//        Connection<Facebook> facebook = connectionRepository.findPrimaryConnection(Facebook.class);
//        return facebook != null ? facebook.getApi() : new FacebookTemplate();
    	Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
    	if( connection != null){
    		if(connection.hasExpired()){
    			System.out.println("EXPIRED!!!");
    			connectionRepository.removeConnection(connection.getKey());
    		}else{
    			System.out.println("NOT EXPIRED, MOVE ON");
    		}
    		
    		Date d = new Date(1437591160286L);
    		Date d2 = new Date();
    		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    		System.out.println("@@@@@@@@@@@");
    		System.out.println(dateFormat.format(d));
    		System.out.println(dateFormat.format(d2));
    		System.out.println(d2.getTime());
    		System.out.println("@@@@@@@@@@@");
    		return connection.getApi();
    	}
    	throw new Exception("wattafuck!");
    	
    }
	
    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);
        controller.setApplicationUrl("http://davide.local:8080/social");
        //controller.setApplicationUrl(environment.getProperty("application.url");
        return controller;
    }

}
