package tlp.client.core;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.common.core.Bootloader;

@Service
public class Main
{
        private final Logger _logger = LoggerFactory.getLogger(Main.class);

        private final App _app;

        public static void main(String[] args)
        {
                Bootloader bootloader = new Bootloader("tlp/client");
                bootloader.start(Main.class);

                // The thread comes here after the ui creation,
                // so don't destroy the world, please!
                // System.exit(0); // Secure close.
        }

        @Autowired
        public Main(App app)
                throws IOException
        {
                this._logger.info("Starting client...");

                this._app = app;
        }

        @PostConstruct
        public Main init()
        {
                SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                                Main.this._app.show();
                        }
                });

                return this;
        }
}
