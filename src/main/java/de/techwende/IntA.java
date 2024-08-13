package de.techwende;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.env.Environment;

@Log4j2
@ConfigurationPropertiesScan
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class IntA implements CommandLineRunner {

    private final Environment environment;
    private final ServerProperties serverProperties;

    @Autowired
    IntA(Environment environment, ServerProperties serverProperties) {
        this.environment = environment;
        this.serverProperties = serverProperties;
    }

    public static void main(final String[] args) {
        SpringApplication.run(IntA.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("""
                        \n
                        === IntA === \n
                        ******************************************
                           * Cmd arguments: {}
                           * Spring profile: {}
                           * Local page: http://localhost:{}{}
                        ******************************************
                        """, args, environment.getActiveProfiles(), serverProperties.getPort(),
                serverProperties.getServlet().getContextPath());
        LOG.info("IntA started successfully");
    }
}
