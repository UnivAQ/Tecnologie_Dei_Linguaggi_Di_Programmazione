package tlp.client.config;

public abstract class IntegerProp implements Prop<Integer>
{
        private Integer _value = new Integer(0);

        public IntegerProp()
        {}

        public IntegerProp(Integer value)
        {
                this.key(value);
        }

        @Override
        public IntegerProp key(Integer value)
        {
                this._value = value;

                return this;
        }

        @Override
        public IntegerProp key(String value)
        {
                this._value = Integer.valueOf(value);

                return this;
        }

        @Override
        public Integer val()
        {
                return this._value;
        }

        @Override
        public String string()
        {
                return String.valueOf(this._value);
        }
}
