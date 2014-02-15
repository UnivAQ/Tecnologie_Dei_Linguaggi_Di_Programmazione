package tlp.client.core;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

@Configuration
public class BeansFactory
{
        private Logger _logger = LoggerFactory.getLogger(BeansFactory.class);

        @Value("#{clientConfig['mongodb.host']}")
        private String _mongodbHost;

        @Value("#{clientConfig['mongodb.port']}")
        private String _mongodbPort;

        @Autowired
        private GenericApplicationContext _context;

        @Bean
        public DB newMongoDb()
        {
                DB db = null;

                try {
                        Mongo mongo = new Mongo(this._mongodbHost, Integer.parseInt(this._mongodbPort));
                        db = mongo.getDB("tlp");
                } catch (UnknownHostException e) {
                        _logger.error(e.getMessage());
                        _logger.error(e.getStackTrace().toString());
                } catch (MongoException e) {
                        _logger.error(e.getMessage());
                        _logger.error(e.getStackTrace().toString());
                }

                return db;
        }
}
