package tlp.client.config;

public abstract class BooleanProp implements Prop<Boolean>
{
        private Boolean _value = false;

        public BooleanProp()
        {}

        public BooleanProp(Boolean value)
        {
                this.key(value);
        }

        @Override
        public BooleanProp key(Boolean value)
        {
                this._value = value;

                return this;
        }

        @Override
        public BooleanProp key(String value)
        {
                String lcValue = value.toLowerCase();

                if (value.equals("true")
                        || value.equals("yes")
                        || value.equals("on")
                        || value.equals("1")) {
                        this._value = true;
                } else {
                        this._value = false;
                }

                return this;
        }

        @Override
        public Boolean val()
        {
                return this._value;
        }

        @Override
        public String string()
        {
                return String.valueOf(this._value);
        }
}
