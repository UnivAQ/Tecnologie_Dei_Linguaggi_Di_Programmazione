package tlp.client.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;

public class AdModel implements Model
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
        public AdModel set(String key, Object value)
        {
                this._impl.set(key, value);

                return this;
        }

        @Override
        public <T> AdModel set(Field<T> field)
        {
                this._impl.set(field.key(), field.val());

                return this;
        }

        @Override
        public AdModel each(Visitor visitor)
        {
                this._impl.each(visitor);

                return this;
        }

        public enum TypeType
        {
                SALE   { @Override public String toString() { return "sale"; } },
                WANTED { @Override public String toString() { return "wanted"; } };
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

        public static class Uid implements Field<String>
        {
                private AbstractField<String> _impl;

                public Uid()
                {
                        this._impl = new AbstractField<String>() {
                                @Override
                                public String key() {
                                        return Uid.this.key();
                                }
                        };
                }

                public Uid(String value)
                {
                        this._impl = new AbstractField<String>(value) {
                                @Override
                                public String key() {
                                        return Uid.this.key();
                                }
                        };
                }

                @Override
                public String key()
                {
                        return "uid";
                }

                @Override
                public String val()
                {
                        return this._impl.val();
                }

                @Override
                public Uid set(String value)
                {
                        this._impl.set(value);

                        return this;
                }
        }

        public static class Type implements Field<TypeType>
        {
                private AbstractField<TypeType> _impl;

                public Type()
                {
                        this._impl = new AbstractField<TypeType>() {
                                @Override
                                public String key() {
                                        return Type.this.key();
                                }
                        };
                }

                public Type(TypeType value)
                {
                        this._impl = new AbstractField<TypeType>(value) {
                                @Override
                                public String key() {
                                        return Type.this.key();
                                }
                        };
                }

                public Type(ButtonGroup value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "type";
                }

                @Override
                public TypeType val()
                {
                        return this._impl.val();
                }

                @Override
                public Type set(TypeType value)
                {
                        this._impl.set(value);

                        return this;
                }

                public Type set(ButtonGroup value)
                {
                        return this.set(TypeType.valueOf(
                                value.getSelection().getActionCommand().toUpperCase()
                        ));
                }
        }

        public static class Title implements Field<String>
        {
                private AbstractField<String> _impl;

                public Title()
                {
                        this._impl = new AbstractField<String>() {
                                @Override
                                public String key() {
                                        return Title.this.key();
                                }
                        };
                }

                public Title(String value)
                {
                        this._impl = new AbstractField<String>(value) {
                                @Override
                                public String key() {
                                        return Title.this.key();
                                }
                        };
                }

                public Title(JTextField value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "title";
                }

                @Override
                public String val()
                {
                        return this._impl.val();
                }

                @Override
                public Title set(String value)
                {
                        this._impl.set(value);

                        return this;
                }

                public Title set(JTextField value)
                {
                        return this.set(value.getText());
                }
        }

        public static class Price implements Field<String>
        {
                private AbstractField<String> _impl;

                public Price()
                {
                        this._impl = new AbstractField<String>() {
                                @Override
                                public String key() {
                                        return Price.this.key();
                                }
                        };
                }

                public Price(String value)
                {
                        this._impl = new AbstractField<String>(value) {
                                @Override
                                public String key() {
                                        return Price.this.key();
                                }
                        };
                }

                public Price(JTextField value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "price";
                }

                @Override
                public String val()
                {
                        return this._impl.val();
                }

                @Override
                public Price set(String value)
                {
                        this._impl.set(value);

                        return this;
                }

                public Price set(JTextField value)
                {
                        return this.set(value.getText());
                }
        }

        public static class Expiry implements Field<Date>
        {
                private AbstractField<Date> _impl;

                public Expiry()
                {
                        this._impl = new AbstractField<Date>() {
                                @Override
                                public String key() {
                                        return Expiry.this.key();
                                }
                        };
                }

                public Expiry(Date value)
                {
                        this._impl = new AbstractField<Date>(value) {
                                @Override
                                public String key() {
                                        return Expiry.this.key();
                                }
                        };
                }

                public Expiry(String value)
                {
                        this();

                        this.set(value);
                }

                public Expiry(JTextField value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "expiry";
                }

                @Override
                public Date val()
                {
                        return this._impl.val();
                }

                public String valStr()
                {
                        return new SimpleDateFormat("dd/MM/yyyy").format(this.val());
                }

                @Override
                public Expiry set(Date value)
                {
                        this._impl.set(value);

                        return this;
                }

                public Expiry set(String value)
                {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                        try {
                                this.set(format.parse(value));
                        } catch (ParseException e) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(
                                        Calendar.getInstance().get(Calendar.YEAR),
                                        Calendar.getInstance().get(Calendar.MONTH),
                                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 90
                                );
                                this.set(calendar.getTime());
                        }

                        return this;
                }

                public Expiry set(JTextField value)
                {
                        return this.set(value.getText());
                }
        }

        public static class Tags implements Field<List<String>>
        {
                private AbstractField<List<String>> _impl;

                public Tags()
                {
                        this._impl = new AbstractField<List<String>>() {
                                @Override
                                public String key() {
                                        return Tags.this.key();
                                }
                        };
                }

                public Tags(List<String> value)
                {
                        this._impl = new AbstractField<List<String>>(value) {
                                @Override
                                public String key() {
                                        return Tags.this.key();
                                }
                        };
                }

                public Tags(String value)
                {
                        this();

                        this.set(value);
                }

                public Tags(JTextField value)
                {
                        this();

                        this.set(value);
                }

                @Override
                public String key()
                {
                        return "tags";
                }

                @Override
                public List<String> val()
                {
                        return this._impl.val();
                }

                public String valStr()
                {
                        String str = "";
                        for (String s: this.val()) {
                                str += ", " + s;
                        }
                        str = str.substring(2);

                        return str;
                }

                @Override
                public Tags set(List<String> value)
                {
                        this._impl.set(value);
                        return this;
                }

                // The "value" param should be a concatenation of strings separed by commas.
                public Tags set(String value)
                {
                        List<String> list = new ArrayList<String>();
                        for (String s: value.split(",")) {
                                list.add(s.trim());
                        }
                        this._impl.set(list);
                        return this;
                }

                public Tags set(JTextField value)
                {
                        return this.set(value.getText());
                }
        }
}
