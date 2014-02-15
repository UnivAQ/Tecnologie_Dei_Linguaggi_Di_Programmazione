package tlp.client.model;

public interface Field<T>
{
        public String key();

        public T val();

        public Field<T> set(T value);
}
