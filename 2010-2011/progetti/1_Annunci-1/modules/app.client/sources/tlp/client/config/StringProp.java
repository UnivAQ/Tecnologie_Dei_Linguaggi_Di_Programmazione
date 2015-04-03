package tlp.client.config;

public abstract class StringProp implements Prop<String>
{
        private String _value = new String("");

        public StringProp()
        {}

        public StringProp(String value)
        {
                this.key(value);
        }

        @Override
        public StringProp key(String value)
        {
                this._value = value;

                return this;
        }

        @Override
        public String val()
        {
                return this._value;
        }

        @Override
        public String string()
        {
                return this.val();
        }
}
