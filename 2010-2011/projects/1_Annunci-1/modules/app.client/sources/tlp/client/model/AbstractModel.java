package tlp.client.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModel implements Model
{
        private HashMap<String, Object> _data = new HashMap<String, Object>();

        @Override
        public HashMap<String, Object> data()
        {
                return this._data;
        }

        @Override
        public AbstractModel set(String key, Object value)
        {
                this._data.put(key, value);

                return this;
        }

        @Override
        public <T> AbstractModel set(Field<T> field)
        {
                this.set(field.key(), field.val());

                return this;
        }

        @Override
        public Object get(String key)
        {
                return this._data.get(key);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Field<T> field)
        {
                return (T) this.get(field.key());
        }

        @Override
        public AbstractModel each(Visitor visitor)
        {
                for (Map.Entry<String, Object> e: this._data.entrySet()) {
                        visitor.visit(e.getKey(), e.getValue());
                }

                return this;
        }

        public static class Id implements Model.Id
        {
                private AbstractField<String> _impl;

                public Id()
                {
                        this._impl = new AbstractField<String>() {
                                @Override
                                public String key() {
                                        return Id.this.key();
                                }
                        };
                }

                public Id(String value)
                {
                        this._impl = new AbstractField<String>(value) {
                                @Override
                                public String key() {
                                        return Id.this.key();
                                }
                        };
                }

                @Override
                public String key()
                {
                        return "id";
                }

                @Override
                public String val()
                {
                        return this._impl.val();
                }

                @Override
                public Id set(String value)
                {
                        this._impl.set(value);

                        return this;
                }
        }
}
