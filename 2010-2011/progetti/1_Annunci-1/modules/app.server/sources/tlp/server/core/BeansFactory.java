package tlp.server.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

@Configuration
public class BeansFactory
{
        @Autowired
        private GenericApplicationContext _context;

        /*
        @Bean
        public TYPE makeCLASS()
        {
                return new CLASS();
        }
        */
}
