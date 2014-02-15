package tlp.client.config;

import java.util.HashMap;
import org.springframework.stereotype.Service;

@Service
public class SessionManager
{
        private HashMap<String, Object> _data = new HashMap<String, Object>();

        public Object get(String key)
        {
                return this._data.get(key);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(Prop<T> key)
        {
                return (T) this.get(key.key());
        }

        public SessionManager set(String key, Object val)
        {
                this._data.put(key, val);

                return this;
        }

        public <T> SessionManager set(Prop<T> prop)
        {
                this.set(prop.key(), prop.val());

                return this;
        }
}
