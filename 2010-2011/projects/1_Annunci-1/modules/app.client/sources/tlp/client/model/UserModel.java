package tlp.client.model;

import java.util.HashMap;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserModel implements Model
{
        private AbstractModel _impl = new AbstractModel() {};

        @Override
        public HashMap<String, Object> data()
        {
                return this._impl.data();
        }

        @Override
        public Object get(String key)
        {
                return this._impl.get(key);
        }

        @Override
        public <T> T get(Field<T> field)
        {
                return this._impl.get(field);
        }

        @Override
        public UserModel set(String key, Object value)
        {
                this._impl.set(key, value);

                return this;
        }

        @Override
        public <T> UserModel set(Field<T> field)
        {
                this._impl.set(field.key(), field.val());

                return this;
        }

        @Override
        public UserModel each(Visitor visitor)
        {
                this._impl.each(visitor);

                return this;
        }

        public static class Id implements Model.Id
        {
                private AbstractModel.Id _impl;

                public Id()
                {
                        this._impl = new AbstractModel.Id();
                }

                public Id(String value)
                {
                        this._impl = new AbstractModel.Id(value);
                }

                @Override
                public String key()
                {
                        return this._impl.key();
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

        public static class Username implements Field<String>
        {
                private AbstractField<String> _impl;

                public Username()
                {
                        this._impl = new AbstractField<String>() {
                                @Override
                                public String key() {
                                        return Username.this.key();
                                }
                        };
                }

                public Username(String value)
                {
                        this._impl = new AbstractField<String>(value) {
                                @Override
                                public String key() {
                                        return Username.this.key();
                                }
                        };
                }

                public Username(JTextField value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "username";
                }

                @Override
                public String val()
                {
                        return this._impl.val();
                }

                @Override
                public Username set(String value)
                {
                        this._impl.set(value);

                        return this;
                }

                public Username set(JTextField value)
                {
                        return this.set(value.getText());
                }
        }

        public static class Password implements Field<String>
        {
                private AbstractField<String> _impl;

                public Password()
                {
                        this._impl = new AbstractField<String>() {
                                @Override
                                public String key() {
                                        return Password.this.key();
                                }
                        };
                }

                public Password(String value)
                {
                        this._impl = new AbstractField<String>(value) {
                                @Override
                                public String key() {
                                        return Password.this.key();
                                }
                        };
                }

                public Password(JPasswordField value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "password";
                }

                @Override
                public String val()
                {
                        return this._impl.val();
                }

                @Override
                public Password set(String value)
                {
                        this._impl.set(value);

                        return this;
                }

                public Password set(JPasswordField value)
                {
                        // <code>new String(value.getPassword())</code>
                        // is different from:
                        // <code>value.getPassword().toString()</code>
                        // The second one doesn't work.
                        return this.set(new String(value.getPassword()));
                }
        }
}
