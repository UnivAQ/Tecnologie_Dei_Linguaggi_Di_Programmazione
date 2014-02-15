package tlp.server.core;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.common.core.Bootloader;

@Service
public class Main
{
        private final Logger _logger = LoggerFactory.getLogger(Main.class);

        private final Daemon _daemon;

        public static void main(String[] args)
        {
                Bootloader bootloader = new Bootloader("tlp/server");
                bootloader.start(Main.class);
        }

        @Autowired
        public Main(Daemon daemon)
                throws IOException
        {
                this._logger.info("Starting server...");

                this._daemon = daemon;
        }

        @PostConstruct
        public Main init()
        {
                return this;
        }
}
