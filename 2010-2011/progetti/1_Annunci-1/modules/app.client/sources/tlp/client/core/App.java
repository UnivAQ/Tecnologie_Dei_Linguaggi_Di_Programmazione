package tlp.client.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.client.config.ConfigManager;
import tlp.client.view.SignView;
import tlp.client.view.View;
import tlp.common.core.ContextFactory;

@Service
public class App
{
        private final Logger _logger = LoggerFactory.getLogger(App.class);

        private ConfigManager _config;

        @Autowired
        public App(ConfigManager config)
        {
                this._config = config;
        }

        public App show()
        {
                View view;

                // We are forcing the user to log in,
                // so we can have multiple instances of the application
                // running on the same machine with different user identity.
                view = ContextFactory.get(SignView.class);

                //if (this._config.exists(new UserIdProp())) {
                //        view = ContextFactory.get(MainView.class);
                //} else {
                //        view = ContextFactory.get(SignView.class);
                //}

                view.view();

                return this;
        }
}
