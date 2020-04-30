package rs.ac.uns.ftn.eventsbackend.config;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;


@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	private final Logger log = LoggerFactory.getLogger(SocialConfig.class);

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
    	// Facebook configuration
        String facebookClientId = environment.getProperty("spring.social.facebook.appId");	//TODO: test this config
        String facebookClientSecret = environment.getProperty("spring.social.facebook.appSecret");
        if (facebookClientId != null && facebookClientSecret != null) {
            log.debug("Configuring FacebookConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new FacebookConnectionFactory(
                    facebookClientId,
                    facebookClientSecret
                )
            );
        } else {
            log.error("Cannot configure FacebookConnectionFactory: id or secret null");
        }
    }

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new UsersConnectionRepository() {
        	// TODO: WTF
			@Override
			public List<String> findUserIdsWithConnection(Connection<?> connection) {
				
				return null;
			}
			
			@Override
			public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
				
				return null;
			}
			
			@Override
			public ConnectionRepository createConnectionRepository(String userId) {
				
				return null;
			}
		};
    }

}
