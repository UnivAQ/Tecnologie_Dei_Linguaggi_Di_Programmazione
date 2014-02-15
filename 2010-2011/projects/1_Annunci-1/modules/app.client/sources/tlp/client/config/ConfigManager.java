package tlp.client.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConfigManager
{
        private Logger _logger = LoggerFactory.getLogger(ConfigManager.class);

        protected String     _confDir;
        protected File       _confFile;
        protected Properties _props = new Properties();

        public ConfigManager()
        {
                try {
                        InputStream defaultConf = ConfigManager.class.getResourceAsStream("/tlp/client/conf/config.properties");
                        this._props.load(defaultConf);
                        defaultConf.close();

                        this._props = new Properties(this._props);

                        String ds = System.getProperty("file.separator");

                        String homeDir = System.getProperty("user.home");
                        this._confDir = homeDir + ds + ".config";
                        this._confDir += ds + "Tlp";

                        if (! (new File(this._confDir)).isDirectory()) {
                                new File(this._confDir).mkdir();
                        }

                        this._confFile = new File(this._confDir + ds + "config.properties");

                        if (! this._confFile.exists()) {
                                this._confFile.createNewFile();
                        }

                        FileInputStream customConf = new FileInputStream(this._confFile);
                        this._props.load(customConf);
                        customConf.close();
                } catch (IOException e) {
                        _logger.error(e.getMessage());
                        _logger.error(e.getStackTrace().toString());
                }
        }

        public ConfigManager store()
        {
                try {
                        this._props.store(new FileWriter(this._confFile), "TLP configuration file.");
                } catch (IOException e) {
                        _logger.error(e.getMessage());
                        _logger.error(e.getStackTrace().toString());
                }

                return this;
        }

        public String getDir()
        {
                return this._confDir;
        }

        protected ConfigManager _setProperty(String key, String value)
        {
                this._props.setProperty(key, value);

                /*
                * We need this code here:
                *  - to ensure that the config is stored
                *    even if the app is not closed properly.
                */
                this.store();

                return this;
        }

        public String get(String key)
        {
                return this._props.getProperty(key);
        }

        public <T> T get(Prop<T> prop)
        {
                return prop.key(this._props.getProperty(prop.key())).val();
        }

        public ConfigManager set(String key, String value)
        {
                this._setProperty(key, value);

                return this;
        }

        public <T> ConfigManager set(Prop<T> prop)
        {
                this.set(prop.key(), prop.string());

                return this;
        }

        public ConfigManager toggle(BooleanProp prop)
        {
                this.set(prop.key(), prop.key(! prop.val()).string());

                return this;
        }

        public <T> Boolean exists(Prop<T> prop)
        {
                this.get(prop);

                return prop.val() != null;
        }
}
