package tlp.client.model;

public abstract class AbstractField<T> implements Field<T>
{
        private T _value;

        public AbstractField()
        {}

        public AbstractField(T value)
        {
                this.set(value);
        }

        @Override
        public T val()
        {
                return this._value;
        }

        @Override
        public AbstractField<T> set(T value)
        {
                this._value = value;

                return this;
        }
}
